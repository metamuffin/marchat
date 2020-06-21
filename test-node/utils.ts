
import * as websocket from "websocket"
import { EventEmitter } from "events";
import { SHA256, timestamp } from '../server-node/source/utils';
import { resolve } from "path";

import * as readline from 'readline';

let test_rli = readline.createInterface({
  input: process.stdin,
  output: process.stdout
});

export type TestWebsocketConnection = websocket.connection

export type TestUserCredentials = {username: string, password: string}


export interface TestUserConnection {
    ws:websocket.connection,
    name:string
}

export var test_wse = new EventEmitter();
export var test_oke = new EventEmitter();

test_wse.on("ok",(data) => {
    test_oke.emit(data.packet)
})

export const test_users:Array<TestUserCredentials> = [
    {
        username: "test-1",
        password: "test"
    },
    {
        username: "test-2",
        password: "test"
    }
]


export function test_waitForInput(){
    return new Promise((resolve, reject) => {
        test_rli.question("ENTER to start!", resolve)
    })
}

export function test_userLogin(con:TestUserConnection, creds: TestUserCredentials){
    return new Promise((resolve,reject) => {
        test_sendPacket(con, "login",{
            username: creds.username,
            password: SHA256(creds.password),
            anti_replay: SHA256(SHA256(creds.password) + " " + timestamp().toString()),
            timestamp: timestamp()
        })
        test_oke.once("login",() => {
            resolve()
        })

    })
}

export function test_connect(name:string): Promise<TestUserConnection> {
    return new Promise((resolve, reject) => {
        var client = new websocket.client();

        
        client.on(`connectFailed`, function(error) {
            console.log(`${name}: Connect Error: ` + error.toString());
        });
        
        client.on(`connect`, function(connection) {
            console.log(`${name}: WebSocket Client Connected`);
            connection.on(`error`, function(error) {
                console.log(`${name}: Connection Error: ` + error.toString());
            });
            connection.on(`close`, function() {
                console.log(`Connection Closed`);
            });
            connection.on(`message`, function(message) {
                if (message.type === 'utf8') {
                    console.log(`${name}: Received: '${message.utf8Data}'`);
                    const [packet_name, packet_data] = message?.utf8Data?.split(":")
                    const packet_data_decoded = Buffer.from(packet_data,"base64").toString()
                    const packet_data_json = JSON.parse(packet_data_decoded)
                    console.log(`${name}: RECV: ${packet_name} -> ${JSON.stringify(packet_data_json)}`);
                    test_wse.emit(packet_name,packet_data_json)
                }
            });
            
            resolve({ws: connection, name: name});

        });

        client.connect('ws://localhost:5555/ws');
    })
}

export function test_sendPacket(con: TestUserConnection, name:string, data:any){
    const packet_data_json = JSON.stringify(data)
    const packet_data_encoded = Buffer.from(packet_data_json).toString("base64")
    const packet = `${con.name}: SEND: ${name}: ${packet_data_encoded}`
    con.ws.sendUTF(packet);
}

export function test_sendPacketAsync(con: TestUserConnection, name: string, data: any){
    return new Promise((resolve,reject) => {
        test_sendPacket(con,name,data);
        test_oke.once(name,resolve)
    })
}
