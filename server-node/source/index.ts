
import express from "express";
import expressWs from "express-ws";
import { User } from "./user";

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
        
    })
    
    // Handle for websocket closing
    ws.on("close",(code,reason) => {

    })
})

console.log("Listening...");
// Start the webserver
app.listen(5555);
