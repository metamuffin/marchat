






import 'dart:convert';

import 'package:eventify/eventify.dart';
import 'package:flutter/material.dart';
import 'package:web_socket_channel/io.dart';

IOWebSocketChannel wsc;
EventEmitter wse = EventEmitter();

void startWS(){
  wsc = IOWebSocketChannel.connect("ws://localhost:5555/");
  wsc.stream.listen((msg) {
    debugPrint(msg);
    Codec<String, String> codec = utf8.fuse(base64);
    debugPrint(codec.decode(msg));
  });
}



void sendPacket(String packetName,Map<String,dynamic> packetData){
  Codec<String, String> codec = utf8.fuse(base64);
  String packetDataEncoded = codec.encode(jsonEncode(packetData));
  String packet = packetName + ":" + packetDataEncoded;
  wsc.sink.add(packet);
}