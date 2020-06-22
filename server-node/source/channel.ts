import { User } from "./user"
import { dbcon } from "./database"
import { Message } from "./message"
import { broadcastPacket, sendPacket } from "./packets"

export var loadedChannels:Array<Channel> = []

export class Channel {
    public name: string = ""
    public users: Array<string> = []
    public adminUsers: Array<string> = []
    public activeUsers: Array<User> = []
    public messageHistory: Array<Message> = []

    constructor(name: string) {
        this.name = name
    }

    public async initialize() {
        console.log(`Loading channel: ${this.name}`);
        var data = await dbcon.collection("channel").findOne({name: this.name})
        this.name = data.name || "Ohnoerror"
        this.users = data.users || []
        this.adminUsers = data.adminUsers || []
        this.messageHistory = data.messageHistory || []
        
        loadedChannels.push(this);
    }

    public dump():any {
        return {
            name: this.name,
            users: this.users,
            adminUsers: this.adminUsers,
            messageHistory: this.messageHistory
        }
    }

    public unload(){
        if(this.activeUsers.length >= 1) return;
        console.log(`Unloading channel: ${this.name}`);
        loadedChannels.splice(loadedChannels.findIndex(u => u.name == this.name))
        var j = this.dump()
        console.log(j);
        dbcon.collection("channel").replaceOne({name: this.name}, j)
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
        console.log(`Reading Messages from ${start} to ${end}.`);
        return this.messageHistory.slice(start,end + 1)
    }

    public async addUser(user: User) {
        this.users.push(user.username);
        user.channels.push(this.name)
    }

    public async removeUser(user: User) {
        user.channels.splice(user.channels.findIndex(c => c == this.name))
        this.users.splice(this.users.findIndex(u => u == user.username))
        this.adminUsers.splice(this.adminUsers.findIndex(u => u == user.username))
    }

    public async unjoinActiveUser(user:User){
        this.activeUsers.splice(this.activeUsers.findIndex(c => {
            if(c.username == user.username) console.log("unjoined user " + c.username +  " from channel " + this.name);
            c.username == user.username
        }))
        
        console.log(this.activeUsers + "active users in " + this.name + "------------------------------------------------------------------------------")
        return
    }

    public async sendUpdate(user: User, count: number, offset: number){
        var msgs = await (await this.fetchMessages(count,offset)).map((m) => ({
            number: m.number,
            text: m.text,
            username: m.username
        }))
        sendPacket(user,"channel",{
            current_message_number: this.messageHistory.length,
            history: msgs,
            users: this.users,
            admin_users: this.adminUsers,
        })
    }
}