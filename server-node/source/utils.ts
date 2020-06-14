import { createHash } from "crypto"
import { User, loadedUsers } from "./user"
import { dbcon } from "./database"
import { loadedChannels, Channel } from "./channel"

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
        user.initialize()
        return user
    }
}

export async function getChannel(name:string): Promise<Channel | undefined> {
    var channel: Channel | undefined = loadedChannels.find(c => c.name == name)
    if (!channel) {
        var dbuser = await dbcon.collection("channel").findOne({name: name})
        if (!dbuser) return
        channel = new Channel(name)
        channel.initialize()
        return channel
    }
}


