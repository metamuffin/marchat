
import express from "express";
import expressWs from "express-ws";
import { User } from "./user";
import { connectDB } from "./database";
import { packets, userLogin } from "./packets";

export var packet_split_regex:RegExp = /(?<pname>.+):(?<pdata>.+)/ig

// Create a express webserver
var _app = express();
// Add websocket support
var app = expressWs(_app).app

// Handle requests to /ws and upgrade the connection to websockets
app.ws("/ws",(ws,req) => {
    console.log("[CON_OPEN]");
    var user: User | undefined = undefined;
    // Receive data
    ws.on("message",async (data) => {
        var sdata:string = data.toString()
        console.log(sdata);

        /*var matches:RegExpMatchArray|null = (packet_split_regex).exec(sdata)
        console.log(matches);
        
        var packet_name:string = matches?.groups?.pname || ""
        var packet_data:string = matches?.groups?.pdata || ""
        */
        var [packet_name, packet_data] = sdata.split(":")
        console.log(`[P] ${packet_name} -> ${packet_data}`);
        

        if (packets.hasOwnProperty(packet_name)){
            var j:Object = {};
            var packet_data_decoded = Buffer.from(packet_data, "base64").toString()
            try {
                j = JSON.parse(packet_data_decoded);
                console.log(j);
            } catch (e) {
                console.log(`Invalid JSON: ${packet_data}`);
            } finally {
                if (!user && packet_name == "login") {
                    user = await userLogin(ws,j)
                    if (!user) return
                    console.log(`Logging in user: ${user.username}`);
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
})

app.use(express.static(__dirname + "/../../client-web/"))

// Start the webserver
async function main(){
    await connectDB()
    console.log("Listening...");
    app.listen(5555);
}
main()
