

import 'dart:math';

import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:marchat_client/channel-add.dart';
import 'package:marchat_client/login.dart';
import 'package:marchat_client/message.dart';
import 'package:marchat_client/websocket.dart';

class ChatView extends StatefulWidget {
  ChatView({Key key}) : super(key: key);

  @override
  _ChatViewState createState() => _ChatViewState();
}

class _ChatViewState extends State<ChatView> {

  final int messageBulkLoadCount = 10;
  final int messageInitialBulkLoadCount = 30;

  List<String> channels = [];
  Map<int, MessageView> messages = {};
  int maxMsgNumber = 0;
  int earliestMessageLoadedNumber = 0;
  String currentChannel = "t2";

  TextEditingController messageSendFieldController = TextEditingController();

  _ChatViewState(){
    wse.on("channel-list", (data) {
      setState(() {
        channels = List<String>.from(data["channels"]);
        //debugPrint("Blub1 ${channels.join(", ")}");
      });
    });
    wse.on("channel", (data) {
      if (data.containsKey("history")) {
        print(data.toString());
        for (var msg in data["history"]) {
          addMessage(msg);
        }
      }
    });
    wse.on("message", (data) {
      addMessage(data);
    });
  }

  void addMessage(Map<String, dynamic> data){
    setState((){
      if (data.containsKey("text") && data.containsKey("number") && data.containsKey("username")){
        messages[data["number"]] = MessageView(number: data["number"], text: data["text"], username: data["username"]);
        maxMsgNumber = max(maxMsgNumber, data["number"]);
      }
    });
  }

  void fetchMessages(int start, int length){
    print("Fetching messages in $currentChannel from $start with length ${start+length}");
    if (currentChannel == null) return
    sendPacket("channel", {
      "name": currentChannel,
      "offset": start,
      "count": length
    });
  }

  Widget chatBuilder(BuildContext context){
    globContext = context;

    return SizedBox(
      height: 500,
      child: ListView.builder(
        reverse: true,
        itemCount: maxMsgNumber + 1,
        itemBuilder: (BuildContext context, int index) {
          int msgIndex = (maxMsgNumber ?? 0) - index;
          if (index < maxMsgNumber && index != -1) {
            if (messages.containsKey(msgIndex)){
              return messages[msgIndex];
            } else {
              return MessageView(number: 0,text: "Oh no",username: "I am an error!",);
            }
          } else {
            fetchMessages(msgIndex - messageBulkLoadCount, messageBulkLoadCount);
            return LinearProgressIndicator(
              valueColor: AlwaysStoppedAnimation<Color>(Theme.of(context).accentColor),
            );
          }
        }
      )
    );
  }  

  void sendMessage(){
    sendPacket("message", {
      "text": messageSendFieldController.text
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(currentChannel ?? "No Channel"),
        actions: <Widget>[
          ChannelAddUIButton()
        ],
      ),
      drawer: Drawer(
        child: ListView.builder(
          itemCount: channels.length,
          itemBuilder: (context, int index) =>
            ListTile(
              enabled: true,
              onTap: (){
                debugPrint("Selected a chat.");
                setState(() {
                  currentChannel = channels[index];
                });
                fetchMessages(-1, messageInitialBulkLoadCount);
                Navigator.pop(context);
              },
              onLongPress: () {
                print("Channel context menu!");
                showDialog(context: context, builder: (context) {
                  return AlertDialog(
                    title: Text("Delete"),
                    content: Text("Do you want to delete or leave \"${channels[index]}\"?"),
                    actions: [
                      FlatButton(
                        child: Text("Nothing actually"),
                        onPressed: (){ Navigator.pop(context); },
                      ),
                      FlatButton(
                        child: Text("Just leave"),
                        onPressed: (){
                          sendPacket("channel_user_remove", {
                            "username": LoginScreen.username,
                            "name": currentChannel
                          });
                          Navigator.pop(context);
                        },
                      ),
                      FlatButton(
                        child: Text("DELETE!!!"),
                        onPressed: (){
                          sendPacket("channel_remove", {
                            "name": channels[index]
                          });
                          Navigator.pop(context);
                        },
                      ),
                    ],
                  );
                });
              },
              title: Text(channels[index], style: Theme.of(context).textTheme.bodyText1,),
              leading: Icon(Icons.message),
            )
        ),
      ),
      body: Column(
        textDirection: TextDirection.ltr,
        children: [
          Expanded(
            child: Builder(builder: chatBuilder,),
          ),
          Row(
            textDirection: TextDirection.ltr,
            children: [
              Expanded(
                child: TextField(
                  autocorrect: true,
                  autofocus: true,
                  controller: messageSendFieldController,
                ),
              ),
              IconButton(icon: Icon(Icons.send), onPressed: sendMessage)
            ],
          )
        ]
      )
    );
  }
}
