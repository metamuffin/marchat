






import 'dart:convert';

import 'package:eventify/eventify.dart';
import 'package:flutter/material.dart';
import 'package:web_socket_channel/io.dart';

IOWebSocketChannel wsc;
EventEmitter wse = EventEmitter();

void startWS(){
  debugPrint("Connecting WS");
  wsc = IOWebSocketChannel.connect("ws://192.168.178.23:5555");
  wsc.stream.listen((msg) {
    debugPrint(msg);
    Codec<String, String> codec = utf8.fuse(base64);
    debugPrint(codec.decode(msg));
  });
}

void sendPacket(String packetName,Map<String,dynamic> packetData){
  Codec<String, String> codec = utf8.fuse(base64);
  String packetDataJson = jsonEncode(packetData);
  String packetDataEncoded = codec.encode(packetDataJson);
  String packet = packetName + ":" + packetDataEncoded;
  debugPrint(packetDataJson);
  wsc.sink.add(packet);
}
