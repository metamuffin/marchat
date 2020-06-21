import { test_connect, test_userLogin, test_users, test_waitForInput, test_sendPacket } from './utils';
import { r_id } from '../server-node/source/utils';

async function blub(){
    const ws1 = await test_connect("User 1")
    const ws2 = await test_connect("User 2")
    var ch1 = `test-${r_id()}`

    await test_userLogin(ws1,test_users[0])
    await test_userLogin(ws2,test_users[1])
    
    await test_sendPacket(ws1,"channel_create",{
        name: ch1
    })
    await test_sendPacket(ws1,"channel_user_add",{
        username: test_users[1].username,
        name: ch1
    })


    
}


blub()



