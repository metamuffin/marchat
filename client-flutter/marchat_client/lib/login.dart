
import 'package:flutter/material.dart';
import 'package:marchat_client/websocket.dart';

class LoginScreen extends StatefulWidget {
  LoginScreen({Key key}) : super(key: key);

  @override
  _LoginScreenState createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {

  final usernameController = TextEditingController();
  final passwordController = TextEditingController();

  void runLogin(){
    sendPacket("login", {
      "username": usernameController.text
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Marchat Login"),
      ),
      body: Center(
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
      ),
    );
  }
}