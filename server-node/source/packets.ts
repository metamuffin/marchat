import { User, onlineUsers } from "./user"
import { dbcon } from "./database";
import WebSocket from "ws"
import { timestamp, SHA256, getChannel, dataAssertType, s_ok, s_error, getUser } from './utils';
import { Channel } from './channel';

export function sendPacketRaw(ws: WebSocket, pname: string, pdata: Object){
    var j = JSON.stringify(pdata)
    console.log(`[SEND_RAW] ${pname} -> ${j}`);
    var b64 = Buffer.from(j).toString("base64")
    if (!ws) console.log(`Skipped packet for offline user: ${pname}`);
    ws.send(pname + ":" + b64)
}

export function sendPacket(user: User, pname: string, pdata: Object){
    var j = JSON.stringify(pdata)
    console.log(`[SEND] ${user.username}: ${pname} -> ${j}`);
    var b64 = Buffer.from(j).toString("base64")
    if (!user.ws) console.log(`Skipped packet for offline user: ${pname}`);
    user.ws?.send(pname + ":" + b64)
}

export function broadcastPacket(users:Array<User>, pname: string, pdata: Object){
    var j = JSON.stringify(pdata)
    console.log(`[SEND_BROADCAST] ${pname} -> ${j}`);
    var b64 = Buffer.from(j).toString("base64")
    for (const user of onlineUsers) {
        if (!user.ws) console.log(`Skipped packet for offline user: ${pname}`);
        user.ws?.send(pname + ":" + b64)
    }
}

export async function userLogin(ws: WebSocket, data:any): Promise<undefined | User> {
    
    if (dataAssertType(ws,data.username,"string","You need to specify a username to log in with.")) return
    if (dataAssertType(ws,data.password,"string","You need to specify a password to log in with.")) return
    if (dataAssertType(ws,data.anti_replay,"string","You need to specify the anti-replay hash.")) return
    if (dataAssertType(ws,data.timestamp,"number","You need to specify the timestamp that you hashed your password hash with.")) return

    var user = await dbcon.collection("user").findOne({username: data.username})
    if (!user) {
        s_error(ws, "User not found.")
        return
    }
    if (!((timestamp() - 10) <= data.timestamp)) {
        s_error(ws, "Your timestamp is too old.")
        return
    }
    if (user.password != data.password) {
        s_error(ws, "Your password does not match.")
        return
    }
    if (SHA256(user.password + " " + data.timestamp.toString()) != data.anti_replay) {
        s_error(ws, "Replay protection cant let you through.")
        return
    }
    var newuser = new User(data.username)
    await newuser.initializeOnline(ws)
    await newuser.sendChannelList();
    s_ok(ws, "login")
    return newuser
}

export async function userRegister(ws: WebSocket, data:any){
    if (dataAssertType(ws,data.username,"string","You need to specify a username to log in with.")) return
    if (dataAssertType(ws,data.username,"string","You need to specify a password to log in with.")) return
    if (data.password.length < 64){
        return s_error(ws,"Your password hash is way too short.")
    }
    if (data.password.length > 64){
        return s_error(ws,"Your password hash is way too long.")
    }
    var ex_user = await dbcon.collection("user").findOne({username: data.username})
    if (ex_user?.username) {
        return s_error(ws,"This user is already existing.")
    }
    console.log(`Username: ${data.username} Password: ${data.password}`);
    
    dbcon.collection("user").insertOne({
        username: data.username,
        password: data.password,
        channels: [],
    }, (err) => {
        if (err)
            return s_error(ws,"Database stuff failed somehow... please report this!");
        console.log("Database transaction done!");
            
        return s_ok(ws, "register");
    })
}

export var packets:{[key: string]: (user: User, data:any) => Promise<void>} = {
    channel: async (user:User, data:any) => {
        if (dataAssertType(user.ws,data.name,"string","Channel name must be provided")) return
        if (dataAssertType(user.ws,data.count,"number","Message count must be provided")) return
        if (dataAssertType(user.ws,data.offset,"number","Message offset  must be provided")) return

        if (user.currentChannel?.name != data.name){
            var ch:Channel|undefined = await getChannel(data.name)
            if (!ch){
                s_error(user.ws,"Channel not found")
                return
            }
            await user.joinChannel(ch);
        }
        await user.sendChannelUpdate(data.count,data.offset)
    },
    message: async (user, data) => {
        if(dataAssertType(user.ws, data.text, "string","You have to send something.")) return
        user.sendMessage(data.text)
        s_ok(user.ws, "message")
    },
    channel_create: async (user, data) => {
        if (dataAssertType(user.ws,data.name,"string","A name for the channel is required.")) return
        var ex_channel = await dbcon.collection("channel").findOne({name: data.name})
        if (ex_channel) return s_error(user.ws,"A channel with this name already exists")
        await dbcon.collection("channel").insertOne({
            name: data.name,
            users: []
        })
        var ch = await getChannel(data.name)
        ch?.addUser(user)
        ch?.adminUsers.push(user.username)
        if (ch) user.joinChannel(ch)
        await user.sendChannelList()
        s_ok(user.ws,"channel_create")
    },
    channel_remove: async (user, data) => {
        if (dataAssertType(user.ws,data.name,"string","A name for the channel is required.")) return
        var ch_ex = await dbcon.collection("channel").findOne({name: data.name})
        if (!ch_ex) return s_error(user.ws,"A channel with this name does not exist")
        var ch = await getChannel(data.name)
        if (ch?.adminUsers.findIndex(u => u == user.username) == -1){
            return s_error(user.ws, "You need to be an admin in this channel, in order to delete it.")
        }
        if (!ch?.users) {
            return s_error(user.ws, "This channel is not initialized the right way... Muffin probably forgot a await somewhere again. :(")
        }
        for (const uname of ch?.users) {
            var u = await getUser(uname)
            if (!u) continue
            ch.removeUser(u)
        }
        ch?.unload() // Force unload
        await dbcon.collection("channel").deleteOne({name: data.name})
        s_ok(user.ws, "channel_remove")
    },
    channel_user_add: async (user, data) => {
        if (dataAssertType(user.ws,data.username,"string","A username for the user to add to the channel is required.")) return
        if (dataAssertType(user.ws,data.channel,"string","A channel name is required.")) return
        var ch = await getChannel(data.channel)
        console.log(ch?.name);
        console.log(ch?.users);
        if (!ch) return s_error(user.ws, "The channel cannot be found.")
        var u = await getUser(data.username)
        console.log(u?.username);
        console.log(u?.channels);
        if (!u) return s_error(user.ws, "The user cannot be found")
        if (ch.users.includes(u.username)) return s_error(user.ws, "The User is already in this channel.")
        await ch.addUser(u)
        if (u.ws) await u.sendChannelList()
        s_ok(user.ws, "channel_user_add")
    },
    channel_user_remove: async (user, data) => {
        if (dataAssertType(user.ws,data.username,"string","A username for the user to add to the channel is required.")) return
        if (dataAssertType(user.ws,data.channel,"string","A channel name is required.")) return
        var ch = await getChannel(data.channel)
        if (!ch) return s_error(user.ws, "The channel cannot be found.")
        var u = await getUser(data.username)
        if (!u) return s_error(user.ws, "The user cannot be found")
        if (!ch.users.includes(data.username)) return s_error(user.ws,"The user is not in this channel.")
        if (!(ch.adminUsers.includes(data.username) || user.username == u.username)) return s_error(user.ws,"You are not allowed to do this. You need to be an admin to remove anybody except for youself")
        await ch.removeUser(u)
        if (u.ws) u.sendChannelList()
        s_ok(user.ws, "channel_user_remove")
    },
    login: async (user,data) => {}, // Just a placeholder. userLogin will be called instead because a User object needs to be created to handle a normal packet
    register: async (user,data) => {}
}
