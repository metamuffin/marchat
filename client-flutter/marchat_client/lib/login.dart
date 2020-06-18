
import 'package:flutter/material.dart';
import 'package:marchat_client/main.dart';
import 'package:marchat_client/websocket.dart';

class LoginScreen extends StatefulWidget {
  LoginScreen({Key key}) : super(key: key);

  @override
  _LoginScreenState createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {

  final usernameController = TextEditingController();
  final passwordController = TextEditingController();

  _LoginScreenState(){
    startWS();
    wse.on("channel-list",(data) {
      wse.off("channel-list");
      mainPageState.setState(() {
        mainPageState.state = PageState.Normal;
      });
      wse.emit("channel-list", data);
    });
  }

  void runLogin(){
    sendPacket("login", {
      "username": usernameController.text,
      "password": sha256String(passwordController.text),
      "anti_replay": sha256String(sha256String(passwordController.text) + " " + unixTimestamp().toString()),
      "timestamp": unixTimestamp(),
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Marchat Login"),
      ),
      body: Builder(
        builder: (context) {
          globContext = context;
          return Center(
            child: Column(
              children: <Widget>[
                TextField(
                  decoration: InputDecoration(
                    contentPadding: EdgeInsets.fromLTRB(20, 15, 20, 15),
                    labelText: "Username"
                  ),
                  controller: usernameController,
                ),
                SizedBox(height: 20,),
                TextField(
                  obscureText: true,
                  decoration: InputDecoration(
                    contentPadding: EdgeInsets.fromLTRB(20, 15, 20, 15),
                    labelText: "Password"
                  ),
                  controller: passwordController,
                ),
                SizedBox(height: 30,),
                FlatButton(
                  onPressed: runLogin,
                  child: Text("Login"),
                )
              ],
            ),
          );
        }
      ),
    );
  }
}