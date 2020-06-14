import { User } from "./user"
import { dbcon } from "./database"
import { Message } from "./message"



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

    public appendMessage(user: User, text: string) {
        this.messageHistory.push({
            username: user.username,
            text: text,
            number: this.messageHistory.length
        })
    }
}