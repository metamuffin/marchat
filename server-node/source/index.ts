
import express from "express";
import expressWs from "express-ws";
import { User } from "./user";
import { connectDB } from "./database";
import { packets, userLogin, userRegister } from "./packets";
import WebSocket from "ws"

export var packet_split_regex:RegExp = /(?<pname>.+):(?<pdata>.+)/ig



// Create a express webserver
var _app = express();
// Add websocket support
var app = expressWs(_app).app

const wsHandler = (ws: WebSocket,req: any) => {
    console.log("[CON_OPEN]");
    var user: User | undefined = undefined;
    // Receive data
    ws.on("message",async (data) => {
        var sdata:string = data.toString()
        
        // Split packet to name and data
        var [packet_name, packet_data] = sdata.split(":")
        
        // Check if there is a handler for this packet
        if (packets.hasOwnProperty(packet_name)){
            var j:any = {};
            if (!packet_data) {
                console.log("No packet data.");
            }   
            // Decode base 64
            var packet_data_decoded = Buffer.from(packet_data, "base64").toString()
            try {
                // Parse JSON
                j = JSON.parse(packet_data_decoded);
                console.log(`[RECV] ${packet_name} -> ${JSON.stringify(j)}`);
            } catch (e) {
                console.log(`Invalid JSON: ${packet_data}`);
            } finally {
                // Is the user logged in?
                if (!user) {
                    if (packet_name == "login"){
                        user = await userLogin(ws,j)
                        if (!user) return
                        console.log(`Logging in user: ${user.username}`);
                    } else if (packet_name == "register"){
                        await userRegister(ws,j)
                        console.log(`Registered a user.`);
                    }
                } else {
                    // the if here is only for ts
                    if (user) await packets[packet_name](user,j);
                }
            }
        } else {
            console.log(`Unknown packet: ${packet_name}`);
        }
    })
    
    // Handle for websocket closing
    ws.on("close",(code,reason) => {
        if (user) {
            user.unload()
            console.log(`Unloading user ${user.username}`);
        }
        console.log("[CON_CLOSE]");
        
    })
}

// Handle requests to /ws and upgrade the connection to websockets
app.ws("/ws", wsHandler)
app.ws("/",wsHandler)

app.use(express.static(__dirname + "/../../client-web/"))

// Start the webserver
async function main(){
    await connectDB()
    console.log("Listening...");
    app.listen(5555);
}
main()
