


import 'package:flutter/material.dart';

class EventEmitter {
  Map<String,List<Function(dynamic data)>> events = {};

  EventEmitter();
  void on(String name,Function(dynamic data) cb) {
    if (!events.containsKey(name)) events[name] = [];
    events[name].add(cb);
  }
  void off(String name) {
    events.remove(name);
  }
  void emit(String name, dynamic data){
    if (!events.containsKey(name)) return debugPrint("Ohno! EventEmitter goes brrrrr: $name");
    events[name].last(data);
  }
  void pop(String name) {
    events[name].removeLast();
  }
}

