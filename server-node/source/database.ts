import { MongoClient, Db } from "mongodb";
export var dbclient:MongoClient;
export var dbcon:Db

export async function connectDB(){
    dbclient = await MongoClient.connect("mongodb://marchat.zapto.org:27017")
    dbcon = dbclient.db("marchat")
    return
}

