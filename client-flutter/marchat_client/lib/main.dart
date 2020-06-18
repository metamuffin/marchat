import 'package:flutter/material.dart';
import 'package:marchat_client/chat.dart';
import 'package:marchat_client/login.dart';
import 'package:marchat_client/theme.dart';

void main() {
  runApp(MarchatApp());
}

class MarchatApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: marchatTheme,
      home: MainPage(),
    );
  }
}

class MainPage extends StatefulWidget {
  MainPage({Key key}) : super(key: key);

  @override
  MainPageState createState() => MainPageState();
}

enum PageState {
  Login,
  Normal,
  Error
}

MainPageState mainPageState;

class MainPageState extends State<MainPage> {

  PageState state = PageState.Login;
  
  MainPageState(){
    mainPageState = this;
  }

  @override
  Widget build(BuildContext context) {
    switch (state) {
      case PageState.Login:
        return LoginScreen();
      case PageState.Normal:
        return ChatView();
      default:
    }
    return Text("Oh no.");
  }
}


