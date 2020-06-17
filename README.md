# marchat

## Protocal Specifications

Upgrade to a Websocket connection with `GET ws(s)://server/ws`

## Packets

`<packet-name>:<json encoded data again encoded in base64>`

## Client-bound Packets

### login

All the SHA256 hashes are converted into hex before the are processed any further.
Response: error or ok

- username: string - *Nickname of the user*
- password: string - *SHA256(password)*
- anti_replay: string - *SHA256(SHA256(password) + " " + timestamp) This is useless!!!!!!!!!!!!!!!!!* 
- timestamp: number - The current UNIX-timestamp
    - Servers should accept the login if the timestamp is at most 10 seconds behind the timestamp

### register

Responses: error or ok

- username: string - *Nickname of the user*
- password: string - *SHA256(password)*

### channel_create

- name: string

### channel_user_add

- username: string
- channel: name

### channel_user_remove

- username: string
- channel: string

### channel_update_admin

- channel: string
- username: string
- value: number
    1. Raise priveleges of this user to full access of this channel
    0. Remove All Privileges of this user

### message

- text: string - Text to send in the active channel!

### status

- status: number
    1. Offline
    2. Online
    3. AFK

### channel

Join a channel and request its contents

- name: string - *New active channel of this user*
- count: number - *number of messages to display*
- offset: number - *where to start the history (-1 for latest)*

## Server-bound Packets

### login

- status: number
    1. OK
    2. User not existing
    3. Password incorrect
    4. Timestamp wrong
    5. Connection throttled (reconnected too fast ( < 20s ))
- channel: string - *name of the channel the user starts in*

### ok

The last action send was successful!
This will be sent if the action doesnt respond with a packet by default.

### channel_list

- channels: A
- name: string

### channel_update_admin

- username: string
- value: number
    1. Raise priveleges of this user to full access of this channel
    0. Remove All Privileges of this userrray of String - *All channels the user joined*

### error

- message: string

### message

- username: string
- text: string

### status

- username: string
- status: number - See client-bound packet

### channel

- current_message_number: number - *number of newest message*
- users: Array of String (Usernames)
- admin_users: Array of Strings (Usernames)
- history: Array
    - username: string
    - text: string
    - number: number

## Important Notes

Running the nodejs server requires a mongodb server.
Create `secret.ts` and export a constant object with the following keys:

- `mongo_user`: Username for mongodb
- `mongo_password`: Password for mongodb

A sample `secret.ts` file.

```typescript
export const secrets = {
    mongo_user: "mongo-goes",
    mongo_password: "brrr"
}
```

Running the java client requires additional files and requirements. See /client-java/README.md for details.