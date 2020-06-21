
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
    wse:EventEmitter,
    oke:EventEmitter
}


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

export async function test_userLogin(con:TestUserConnection, creds: TestUserCredentials){
    await test_sendPacketAsync(con, "login",{
        username: creds.username,
        password: SHA256(creds.password),
        anti_replay: SHA256(SHA256(creds.password) + " " + timestamp().toString()),
        timestamp: timestamp()
    })
}

export function test_connect(name:string): Promise<TestUserConnection> {
    return new Promise((resolve, reject) => {
        var client = new websocket.client();

        
        client.on(`connectFailed`, function(error) {
            console.log(`${name}: Connect Error: ` + error.toString());
        });
        
        client.on(`connect`, function(connection) {
            var ucon = {
                ws: connection,
                name: name,
                wse: new EventEmitter(),
                oke: new EventEmitter()
            }
            ucon.wse.on("ok", (data) => {
                ucon.oke.emit(data.packet)
            })

            console.log(`${name}: WebSocket Client Connected`);
            connection.on(`error`, function(error) {
                console.log(`${name}: Connection Error: ` + error.toString());
            });
            connection.on(`close`, function() {
                console.log(`Connection Closed`);
            });
            connection.on(`message`, function(message) {
                if (message.type === 'utf8') {
                    const [packet_name, packet_data] = message?.utf8Data?.split(":")
                    const packet_data_decoded = Buffer.from(packet_data,"base64").toString()
                    const packet_data_json = JSON.parse(packet_data_decoded)
                    console.log(`${name}: RECV: ${packet_name} -> ${JSON.stringify(packet_data_json)}`);
                    ucon.wse.emit(packet_name,packet_data_json)
                }
            });
            
            resolve(ucon);

        });

        client.connect('ws://localhost:5555/ws');
    })
}

export function test_sendPacket(con: TestUserConnection, name:string, data:any){
    const packet_data_json = JSON.stringify(data)
    const packet_data_encoded = Buffer.from(packet_data_json).toString("base64")
    console.log(`${con.name}: SEND: ${name}: ${packet_data_json}`)
    const packet = `${name}:${packet_data_encoded}`
    con.ws.sendUTF(packet);
}

export function test_sendPacketAsync(con: TestUserConnection, name: string, data: any){
    return new Promise((resolve,reject) => {
        test_sendPacket(con,name,data);
        con.oke.once(name,resolve)
    })
}
