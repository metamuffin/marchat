import { User, onlineUsers } from "./user"
import { dbcon } from "./database";
import WebSocket from "ws"
import { timestamp, SHA256, getChannel } from "./utils";
import { Channel } from "./channel";

export function sendPacketRaw(ws: WebSocket, pname: string, pdata: Object){
    var j = JSON.stringify(pdata)
    var b64 = Buffer.from(j).toString("base64")
    if (!ws) console.log(`Skipped packet for offline user: ${pname}`);
    ws.send(pname + ":" + b64)
}

export function sendPacket(user: User, pname: string, pdata: Object){
    var j = JSON.stringify(pdata)
    var b64 = Buffer.from(j).toString("base64")
    if (!user.ws) console.log(`Skipped packet for offline user: ${pname}`);
    user.ws?.send(pname + ":" + b64)
}

export function broadcastPacket(users:Array<User>, pname: string, pdata: Object){
    var j = JSON.stringify(pdata)
    var b64 = Buffer.from(j).toString("base64")
    for (const user of onlineUsers) {
        if (!user.ws) console.log(`Skipped packet for offline user: ${pname}`);
        user.ws?.send(pname + ":" + b64)
    }
}

export async function userLogin(ws: WebSocket, data:any): Promise<undefined | User> {
    
    
    if (!data.username || typeof data.username != "string"){
        sendPacketRaw(ws, "error", {message: "You need to specify a username to log in with."})
        return
    }
    if (!data.password || typeof data.password != "string"){
        sendPacketRaw(ws, "error", {message: "You need to specify a password to log in with."})
        return
    }
    if (!data.anti_replay || typeof data.anti_replay != "string"){
        sendPacketRaw(ws, "error", {message: "You need to specify the anti-replay hash."})
        return
    }
    if (!data.timestamp && typeof data.timestamp == "number"){
        sendPacketRaw(ws, "error", {message: "You need to specify the timestamp that you hashed your password hash with."})
        return
    }
    var user = await dbcon.collection("user").findOne({username: data.username})
    if (!user) {
        sendPacketRaw(ws, "error", {message: "User not found."})
        return
    }
    if (!((timestamp() - 10) <= data.timestamp)) {
        sendPacketRaw(ws, "error", {message: "Your timestamp is too old."})
        return
    }
    if (user.password != data.password) {
        sendPacketRaw(ws, "error", {message: "Your password does not match."})
        return
    }
    if (SHA256(user.password + " " + data.timestamp.toString()) != data.anti_replay) {
        sendPacketRaw(ws, "error", {message: "Replay protection cant let you through."})
        return
    }
    var newuser = new User(data.username)
    await newuser.initialize()
    return newuser
}

export var packets:{[key: string]: (user: User, data:any) => Promise<void>} = {
    channel: async (user:User, data:any) => {
        if (!data.name || typeof data.name != "string"){
            sendPacket(user,"error",{message:"Channel name must be provided"})
            return
        }
        if (!data.count || typeof data.count != "number") {
            sendPacket(user,"error",{message:"Message count must be provided"})
            return
        }
        if (!data.offset || typeof data.offset != "number") {
            sendPacket(user,"error",{message:"Message count must be provided"})
            return
        }
        if (user.currentChannel?.name != data.name){
            var ch:Channel|undefined = await getChannel(data.name)
            if (!ch){
                sendPacket(user,"error",{message:"Channel not found"})
                return
            }
            user.joinChannel(ch);
        }
        await user.sendChannelUpdate(data.count,data.offset)
    },
    message: async (user, data) => {
        if (typeof data.message == "string") {
            sendPacket(user,"error",{message:"Please send a message not nothing"})
            return
        }
        user.sendMessage(data.message)
    },
    login: async (user,data) => {} // Just a placeholder. userLogin will be called instead because a User object needs to be created to handle a normal packet
}
