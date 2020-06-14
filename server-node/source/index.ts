
import express from "express";
import expressWs from "express-ws";
import { User } from "./user";
import { connectDB } from "./database";
import { packets } from "./packets";

export var packet_split_regex:RegExp = /(?<pname>.+):(?<pdata>.+)/ig

// Create a express webserver
var _app = express();
// Add websocket support
var app = expressWs(_app).app

// Handle requests to /ws and upgrade the connection to websockets
app.ws("/ws",(ws,req) => {
    
    // Receive data
    ws.on("message",(data) => {
        var sdata:string = data.toString()
        var matches:RegExpMatchArray|null = (packet_split_regex).exec(sdata)
        var packet_name:string = matches?.groups?.pname || ""
        var packet_data:string = matches?.groups?.pdata || "" 
        if (packets.hasOwnProperty(packet_name)){
            var j:Object = {};
            var packet_data_decoded = new Buffer(packet_data, "base64").toString()
            try {
                j = JSON.parse(packet_data_decoded);
            } catch (e) {
                console.log(`Invalid JSON: ${packet_data}`);
            } finally {
                packets[packet_name](j);
            }
        }
    })
    
    // Handle for websocket closing
    ws.on("close",(code,reason) => {

    })
})

console.log("Listening...");
// Start the webserver
async function main(){
    await connectDB()
    app.listen(5555);
}
main()
