import { MongoClient } from "mongodb";
export var dbcon:any;

export async function connectDB(){
    dbcon = await MongoClient.connect("mongodb://marchat.zapto.org:27017")
}

