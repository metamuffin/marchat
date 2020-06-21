import { test_connect, test_userLogin, test_users, test_waitForInput, test_sendPacket, test_sendPacketAsync } from './utils';
import { r_id } from '../server-node/source/utils';

async function blub(){
    console.log("Connecting in Users");
    const ws1 = await test_connect("User 1")
    const ws2 = await test_connect("User 2")
    const ch1 = `test-${r_id()}`

    console.log("Logging in users");
    await test_userLogin(ws1,test_users[0])
    await test_userLogin(ws2,test_users[1])
    
    console.log(`Creating channel: ${ch1}`)
    await test_sendPacketAsync(ws1,"channel_create",{
        name: ch1
    })
    
    console.log(`Adding ${test_users[1].username} to channel: ${ch1}`)
    await test_sendPacketAsync(ws1,"channel_user_add",{
        username: test_users[1].username,
        channel: ch1
    })
    console.log(`Joining ${test_users[1].username} into ${ch1}`);
    await test_sendPacketAsync(ws2,"channel", {
        name: ch1,
        count: 10,
        offset: -1
    })

    console.log("Sending messages");
    var msgp1 = test_sendPacketAsync(ws1,"message",{
        text: "Hello. I am user #1"
    })
    var msgp2 = test_sendPacketAsync(ws2,"message",{
        text: "Hello. I am user #2"
    })

    await Promise.all([msgp1,msgp2])

    console.log("Removing channel");
    await test_sendPacketAsync(ws1,"channel_remove",{
        name: ch1,
    })

    ws1.ws.close()
    ws2.ws.close()
    
}


blub()



// import { test_connect, test_userLogin, test_users, test_waitForInput, test_sendPacket, test_sendPacketAsync } from './utils';
// import { r_id } from '../server-node/source/utils';

// async function blub(){
//     console.log("Connecting in Users");
//     const ws1 = await test_connect("User 1")
//     const ws2 = await test_connect("User 2")
//     const ch1 = `test-${r_id()}`

//     console.log("Logging in users");
//     await test_userLogin(ws1,test_users[0])
//     await test_userLogin(ws2,test_users[1])
    
//     console.log(`Creating channel: ${ch1}`)
//     await test_sendPacketAsync(ws1,"channel_create",{
//         name: ch1
//     })
    
//     console.log(`Adding ${test_users[1].username} to channel: ${ch1}`)
//     await test_sendPacketAsync(ws1,"channel_user_add",{
//         username: test_users[1].username,
//         channel: ch1
//     })
//     console.log(`Joining ${test_users[1].username} into ${ch1}`);
//     await test_sendPacketAsync(ws2,"channel", {
//         name: ch1,
//         count: 10,
//         offset: -1
//     })

//     console.log("Sending messages");
//     await test_sendPacketAsync(ws1,"message",{
//         text: "Hello. I am user #1"
//     })
//     await test_sendPacketAsync(ws2,"message",{
//         text: "Hello. I am user #2"
//     })

//     console.log("Removing channel");
//     await test_sendPacketAsync(ws1,"channel_remove",{
//         name: ch1,
//     })

//     ws1.ws.close()
//     ws2.ws.close()
    
// }


// blub()



