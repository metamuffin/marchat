


import 'package:flutter/material.dart';

class EventEmitter {
  Map<String,Function(dynamic data)> events = {};

  EventEmitter();
  void on(String name,Function(dynamic data) cb) {
    events[name] = cb;
  }
  void off(String name) {
    events.remove(name);
  }
  void emit(String name, dynamic data){
    if (!events.containsKey(name)) debugPrint("Ohno! EventEmitter goes brrrrr: $name");
    events[name](data);
  }
}

