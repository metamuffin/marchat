import { createHash } from "crypto"
import { User, loadedUsers } from "./user"
import { dbcon } from "./database"
import { loadedChannels, Channel } from "./channel"
import WebSocket from "ws"
import { sendPacketRaw } from './packets';

export function SHA256(data:string): string {
    var hash = createHash("sha256")
    hash.update(data)
    return hash.digest("hex")
}

export var timestamp:()=>number = () => Math.floor(Date.now() / 1000)

export async function getUser(username:string): Promise<User | undefined> {
    var user: User | undefined = loadedUsers.find(u => u.username == username)
    if (!user) {
        var dbuser = await dbcon.collection("user").findOne({username: username})
        if (!dbuser) return
        user = new User(username)
        await user.initialize()
        return user
    }
}

export async function getChannel(name:string): Promise<Channel | undefined> {
    var channel: Channel | undefined = loadedChannels.find(c => c.name == name)
    if (!channel) {
        var dbuser = await dbcon.collection("channel").findOne({name: name})
        if (!dbuser) return
        channel = new Channel(name)
        await channel.initialize()
        return channel
    }
}

// Returns true if there is an error
export async function dataAssertType(ws: WebSocket | undefined, data:any, type: string, error: string){
    if (data && typeof data == type) {
        return false
    }
    s_error(ws, error)
    return true
}

export function s_ok(ws: WebSocket | undefined){
    if (!ws) return console.log(`Skipped ok for offline user.`)
    sendPacketRaw(ws,"ok",{})
}

export function s_error(ws: WebSocket | undefined, msg: string){
    if (!ws) return console.log(`Skipped error for offline user: "${msg}"`)
    sendPacketRaw(ws,"message",{message: msg})
}
