


import 'package:flutter/material.dart';

class EventEmitter<T> {
  Map<String,List<Function(T data)>> events = {};

  EventEmitter();
  void on(String name,Function(T data) cb) {
    if (!events.containsKey(name)) events[name] = [];
    events[name].add(cb);
  }
  void off(String name) {
    events.remove(name);
  }
  void emit(String name, T data){
    if (!events.containsKey(name)) return debugPrint("Ohno! EventEmitter goes brrrrr: $name");
    events[name].last(data);
  }
  void pop(String name) {
    events[name].removeLast();
  }
}

