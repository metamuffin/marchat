import { User } from "./user"
import { dbcon } from "./database"
import { Message } from "./message"
import { broadcastPacket } from "./packets"

export var loadedChannels:Array<Channel> = []

export class Channel {
    public name: string = ""
    public users: Array<string> = []
    public activeUsers: Array<User> = []
    public messageHistory: Array<Message> = []

    constructor(name: string) {
        this.name = name
    }

    public async initialize() {
        var data = await dbcon.collection("user").findOne({username: this.name})
        this.name = data.name
        this.users = data.users
    }

    public dump():string {
        return JSON.stringify({
            name: this.name,
            users: this.users,
            messageHistory: this.messageHistory
        })
    }

    public unload(){
        loadedChannels.splice(loadedChannels.findIndex(u => u.name == this.name))
        var j = this.dump()
        dbcon.collection("user").replaceOne({name: this.name}, j)
    }

    public appendMessage(user: User, text: string) {
        var newmsg = {
            username: user.username,
            text: text,
            number: this.messageHistory.length
        }
        this.messageHistory.push(newmsg)
        broadcastPacket(this.activeUsers,"message",newmsg)
    }

    public async fetchMessages(count: number, offset:number):Promise<Array<Message>> {
        if (offset < 0) offset = this.messageHistory.length
        var start = Math.max(0,Math.min(this.messageHistory.length,offset - count))
        var end = Math.max(0,Math.min(this.messageHistory.length,offset))
        return this.messageHistory.slice(start,end)
    }
}