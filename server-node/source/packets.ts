import { User, onlineUsers } from "./user"


export function sendPacket(user: User, pname: string, pdata: Object){
    var j = JSON.stringify(pdata)
    var b64 = new Buffer(j).toString("base64")
    if (!user.ws) console.log(`Skipped packet for offline user: ${pname}`);
    user.ws?.send(b64)
}

export function broadcastPacket(pname: string, pdata: Object){
    var j = JSON.stringify(pdata)
    var b64 = new Buffer(j).toString("base64")
    for (const user of onlineUsers) {
        if (!user.ws) console.log(`Skipped packet for offline user: ${pname}`);
        user.ws?.send(b64)
    }
}


export var packets:{[key: string]: (data:Object) => void} = {
    login: (data) => {
        // Jetzt kommt MongoDB
    }
}
// Ich auch nicht. klingt aber cool
