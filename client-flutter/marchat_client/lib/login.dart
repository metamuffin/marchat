import 'dart:async';

import 'package:flutter/material.dart';
import 'package:marchat_client/main.dart';
import 'package:marchat_client/websocket.dart';

class LoginScreen extends StatefulWidget {
  LoginScreen({Key key}) : super(key: key);

  static String username;

  @override
  _LoginScreenState createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final usernameController = TextEditingController();
  final passwordController = TextEditingController();
  //String targetHost = "marchat.zapto.org:5555";
  //List<String> hostList = ["marchat.zapto.org:5555","127.0.0.1:5555"];

  _LoginScreenState() {
    startWS();

    wse.on("channel-list", (data) {
      wse.off("channel-list");
      LoginScreen.username = usernameController.text;
      mainPageState.setState(() {
        mainPageState.state = PageState.Normal;
      });
      new Timer(Duration(milliseconds: 500), () {
        wse.emit("channel-list", data);
      });
    });
    oke.on("login", (data) {
      oke.off("login");
      LoginScreen.username = usernameController.text;
      mainPageState.setState(() {
        mainPageState.state = PageState.Normal;
      });
    });
  }

  void runLogin() {
    sendPacket("login", {
      "username": usernameController.text,
      "password": sha256String(passwordController.text),
      "anti_replay": sha256String(sha256String(passwordController.text) +
          " " +
          unixTimestamp().toString()),
      "timestamp": unixTimestamp(),
    });
  }

  @override
  Widget build(BuildContext context) {
    globContext = context;
    return Scaffold(
      appBar: AppBar(
        title: Text("Marchat Login"),
      ),
      body: Builder(builder: (context) {
        globContext = context;
        return Center(
          child: Column(
            children: <Widget>[
              SizedBox(
                height: 20,
              ),
              TextField(
                decoration: InputDecoration(
                    contentPadding: EdgeInsets.fromLTRB(20, 15, 20, 15),
                    labelText: "Username"),
                controller: usernameController,
              ),
              SizedBox(
                height: 20,
              ),
              TextField(
                obscureText: true,
                decoration: InputDecoration(
                    contentPadding: EdgeInsets.fromLTRB(20, 15, 20, 15),
                    labelText: "Password"),
                controller: passwordController,
              ),
              SizedBox(
                height: 30,
              ),
              FlatButton(
                onPressed: runLogin,
                child: Text("Login"),
              )
            ],
          ),
        );
      }),
    );
  }
}
