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

export async function userLogin(ws: WebSocket, data:any): Promise<undefined | User | void> {
    
    if (await dataAssertType(ws,data.username,"string","You need to specify a username to log in with.")) return
    if (await dataAssertType(ws,data.password,"string","You need to specify a password to log in with.")) return
    if (await dataAssertType(ws,data.anti_replay,"string","You need to specify the anti-replay hash.")) return
    if (await dataAssertType(ws,data.timestamp,"number","You need to specify the timestamp that you hashed your password hash with.")) return

    var user = await dbcon.collection("user").findOne({username: data.username})
    if (!user) {
        return s_error(ws, "User not found.")
    }
    if (!((timestamp() - 10) <= data.timestamp)) {
        return s_error(ws, "Your timestamp is too old.")
    }
    if (user.password != data.password) {
        return s_error(ws, "Your password does not match.")
    }
    if (SHA256(user.password + " " + data.timestamp.toString()) != data.anti_replay) {
        return s_error(ws, "Replay protection cant let you through.")
    }
    var newuser = new User(data.username)
    await newuser.initializeOnline(ws)
    await newuser.sendChannelList();
    return newuser
}

export async function userRegister(ws: WebSocket, data:any){
    if (dataAssertType(ws,data.username,"string","You need to specify a username to log in with.")) return
    if (dataAssertType(ws,data.username,"string","You need to specify a password to log in with.")) return
    if (data.password.length < 8){
        return s_error(ws,"Your password is way too short.")
    }
    if (data.password.length > 50){
        return s_error(ws,"Your password is too secure.")
    }
    var ex_user = await dbcon.collection("user").findOne({username: data.username})
    if (ex_user?.username) {
        return s_error(ws,"This user is already existing.")
    }
    dbcon.collection("user").insertOne({
        username: data.username,
        password: data.password,
        channels: [],
    }, (err) => {
        if (err)
            return s_error(ws,"Database stuff failed somehow... please report this!");
        return s_ok(ws);
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
        if(dataAssertType(user.ws, data.message, "string","Please send a message not nothing")) return
        user.sendMessage(data.message)
    },
    channel_create: async (user, data) => {
        if (dataAssertType(user.ws,data.name,"string","A name for the channel is required.")) return
        var ex_channel = await dbcon.collection("channel").findOne({name: data.name})
        if (ex_channel) return s_error(user.ws,"A channel with this name already exists")
        await dbcon.collection("channel").insertOne({
            name: data.name,
            users: [user.username]
        })
        var ch = await getChannel(data.name)
        ch?.addUser(user)
        user.joinChannel(data.name)
        await user.sendChannelList()
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
        s_ok(user.ws)
    },
    channel_user_add: async (user, data) => {
        if (dataAssertType(user.ws,data.username,"string","A username for the user to add to the channel is required.")) return
        if (dataAssertType(user.ws,data.channel,"string","A channel name is required.")) return
        var ch = await getChannel(data.channel)
        if (!ch) return s_error(user.ws, "The channel cannot be found.")
        var u = await getUser(data.username)
        if (!u) return s_error(user.ws, "The user cannot be found")
        ch.addUser(u)
        if (u.ws) u.sendChannelList()
        s_ok(user.ws)
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
        ch.removeUser(u)
        if (u.ws) u.sendChannelList()
        s_ok(user.ws)
    },
    login: async (user,data) => {}, // Just a placeholder. userLogin will be called instead because a User object needs to be created to handle a normal packet
    register: async (user,data) => {}
}
