import { dbcon } from "./database";
import { Channel } from "./channel";


export var onlineUsers:Array<User> = []
export var loadedUsers:Array<User> = []



export class User {
    public ws: WebSocket | undefined = undefined;
    public username: string = "";
    public currentChannel: Channel | undefined; 
    public channels: Array<string> = []

    public User(username: string) {
        this.username = username;
    }
    
    public async initialize(){
        onlineUsers.push(this);
        loadedUsers.push(this);
        var data = await dbcon.collection("user").findOne({username: this.username})
        this.username = data.username
    }

    public dump():string {
        return JSON.stringify({
            username: this.username
        })
    }

    public unload(){
        onlineUsers.splice(onlineUsers.findIndex(u => u.username == this.username))
        loadedUsers.splice(loadedUsers.findIndex(u => u.username == this.username))
        var j = this.dump()
        dbcon.collection("user").replaceOne({username: this.username}, j)
    }

    public sendMessage(text:string) {
        this.currentChannel?.appendMessage(this,text)
    }

}