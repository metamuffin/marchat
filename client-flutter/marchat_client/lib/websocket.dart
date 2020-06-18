
import 'dart:convert';
import 'package:crypto/crypto.dart';

import 'package:flutter/material.dart';
import 'package:marchat_client/event-emitter.dart';
import 'package:web_socket_channel/io.dart';

IOWebSocketChannel wsc;
EventEmitter wse = EventEmitter();
EventEmitter oke = EventEmitter();

BuildContext globContext;

void loginDummy(){
  sendPacket("login", {
    "username": "dummy",
    "password": sha256String("dummy"),
    "anti_replay": sha256String(sha256String("dummy") + " " + unixTimestamp().toString()),
    "timestamp": unixTimestamp(),
  });
}


void startWS(){
  debugPrint("Connecting WS");
  wsc = IOWebSocketChannel.connect("ws://marchat.zapto.org:5555");
  wsc.stream.listen((msg) {
    parsePacket(msg);
  });
  wse.on("error", (data) {
    showError(data["message"]);
  });
  wse.on("ok", (data) {
    oke.emit(data["packet"],null);
  });
}

void showError(String message){
  Scaffold.of(globContext).showSnackBar(SnackBar(
    content: Text(message),
    action: SnackBarAction(label: "Ignore", onPressed: (){}),
  ));
}

void parsePacket(String packet){
  Codec<String, String> codec = utf8.fuse(base64);
  List<String> packetSplit = packet.split(":");
  String packetName, packetData;
  packetName = packetSplit[0];
  try {
    packetData = codec.decode(packetSplit[1]);
  } catch (e) {
    showError("Uff. It seems like the server is too distorded to send us correct data.");
  }
  dynamic packetDataJ = json.decode(packetData);
  debugPrint(packetName);
  debugPrint(packetData);
  wse.emit(packetName,packetDataJ);
}

void sendPacket(String packetName,Map<String,dynamic> packetData){
  Codec<String, String> codec = utf8.fuse(base64);
  String packetDataJson = jsonEncode(packetData);
  String packetDataEncoded = codec.encode(packetDataJson);
  String packet = packetName + ":" + packetDataEncoded;
  debugPrint(packetDataJson);
  wsc.sink.add(packet);
}

String sha256String(String a){
  var bytes = utf8.encode(a);
  var digest = sha256.convert(bytes);
  return digest.toString();
}

int unixTimestamp(){
  return (DateTime.now().toUtc().millisecondsSinceEpoch / 1000).floor();
}
