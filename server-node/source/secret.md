
Running this server requires a mongodb server.
Create `secret.ts` and export a constant object with the following keys:

- `mongo_user`: Username for mongodb
- `mongo_password`: Password for mongodb


```typescript
export const secrets = {
    mongo_user: "mongo-goes",
    mongo_password: "brrr"
}
```