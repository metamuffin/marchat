import { dbcon } from "./database";
import { Channel } from "./channel";
import { broadcastPacket, sendPacket } from "./packets";
import WebSocket from "ws"
import { getChannel } from "./utils";
import { Console } from "console";

export var onlineUsers:Array<User> = []
export var loadedUsers:Array<User> = []



export class User {
    public ws: WebSocket | undefined = undefined;
    public username: string = "";
    public password: string = "";
    public currentChannel: Channel | undefined; 
    public channels: Array<string> = []
    public status:number = 0;

    public constructor(username: string) {
        this.username = username;
    }
    
    public async initializeOnline(ws: WebSocket) {
        await this.initialize()
        onlineUsers.push(this);
        this.ws = ws;
    }

    public async initialize(){
        console.log(`Loading user: ${this.username}`);
        loadedUsers.push(this);
        var data = await dbcon.collection("user").findOne({username: this.username})
        this.username = data.username
        this.password = data.password
        this.channels = data.channels
    }

    public dump():any {
        return {
            username: this.username,
            password: this.password,
            channels: this.channels,
        }
    }

    public async unload(){
        onlineUsers.splice(onlineUsers.findIndex(u => u.username == this.username))
        loadedUsers.splice(loadedUsers.findIndex(u => u.username == this.username))
        await this.currentChannel?.unjoinActiveUser(this);
        if ((this.currentChannel?.activeUsers?.length || 0) < 1) {
            if (this.currentChannel) this.currentChannel?.unload()
        }
        var j = this.dump()
        console.log(j);
        dbcon.collection("user").replaceOne({username: this.username}, j)
        console.log(`Unloading user: ${this.username}`);
        return
    }

    public async sendMessage(text:string) {
        text = text.trim()
        this.currentChannel?.appendMessage(this,text)
    }

    public async joinChannel(channel:Channel) {
        if (this.currentChannel) {
                await this.currentChannel.unjoinActiveUser(this);
            if (this.currentChannel.activeUsers.length < 1) {
                this.currentChannel.unload()
            }
        }
        this.currentChannel = channel
        this.currentChannel?.activeUsers.push(this)
        console.log(this.currentChannel.activeUsers.length  + " active users in channel " + this.currentChannel.name + "..................................................................")
    }

    public async sendChannelUpdate(count:number, offset: number){
        await this.currentChannel?.sendUpdate(this,count,offset)
    }

    public async sendChannelList(){
        console.log(`New channel list for ${this.username}`);
        sendPacket(this,"channel-list",{
            channels: this.channels
        })
    }

}