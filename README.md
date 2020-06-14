# marchat

## Protocal Specifications

Upgrade to a Websocket connection with `GET ws(s)://server/ws`

## Packets

`<packet-name>:<json encoded data again encoded in base64>`

## Client-bound Packets

### login

- username: string - *Nickname of the user*
- password: string - *SHA256 hash of the users password followed by a space and the current UNIX-timestamp*
    - Servers should accept the login if the timestamp is at most 10 seconds behind the timestamp
- timestamp: number - The current UNIX-timestamp

### text

- text: string - Text!

### status

- status: number
    1. Offline
    2. Online
    3. AFK

## Server-bound Packets

### login

- status: number
    1. OK
    2. User not existing
    3. Password incorrect
    4. Timestamp wrong
    5. Connection throttled (reconnected too fast ( < 20s ))

### text

- username: string
- text: string

### status

- username: string
- status: number - See client-bound packet

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