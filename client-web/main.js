var ws;


function sendPacket(name,data){
    ws.send(name + ":" + btoa(JSON.stringify(data)))
}

async function sha256(message) {
    // encode as UTF-8
    const msgBuffer = new TextEncoder('utf-8').encode(message);                    

    // hash the message
    const hashBuffer = await crypto.subtle.digest('SHA-256', msgBuffer);

    // convert ArrayBuffer to Array
    const hashArray = Array.from(new Uint8Array(hashBuffer));

    // convert bytes to hex string                  
    const hashHex = hashArray.map(b => ('00' + b.toString(16)).slice(-2)).join('');
    return hashHex;
}


window.onload = () => {
    ws = new WebSocket("ws://localhost:5555/ws")
    ws.onmessage = (ev) => {
        console.log(ev.data.toString())
        var f = ev.data.toString()
        console.log(f.split(":")[0]);
        console.log(JSON.parse(atob(f.split(":")[1])));
        
    }
    ws.onopen = () => console.log("WS_OPEN")
    ws.onclose = () => console.log("WS_CLOSE")
    ws.onerror = (ev) => console.log(`WS_ERROR: ${ev}`)
}