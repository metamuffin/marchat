

import 'package:flutter/material.dart';
import 'package:marchat_client/channel-add.dart';
import 'package:marchat_client/message.dart';
import 'package:marchat_client/websocket.dart';

class ChatView extends StatefulWidget {
  ChatView({Key key}) : super(key: key);

  @override
  _ChatViewState createState() => _ChatViewState();
}

class _ChatViewState extends State<ChatView> {

  final int messageBulkLoadCount = 10;

  List<String> channels = [];
  Map<int, MessageView> messages = {};
  int maxMsgNumber = 0;
  String currentChannel;

  _ChatViewState(){
    wse.on("channel-list", (data) {
      setState(() {
        channels = List<String>.from(data["channels"]);
        //debugPrint("Blub1 ${channels.join(", ")}");
      });
    });
  }

  void fetchMessages(int start, int length){
    if (currentChannel == null) return
    sendPacket("channel", {
      "name": currentChannel,
      "offset": start,
      "count": length
    });
  }

  

  Widget chatBuilder(BuildContext context){
    globContext = context;

    return ListView.builder(
      itemCount: maxMsgNumber + 1,
      itemBuilder: (BuildContext context, int index) {
        if (index < maxMsgNumber) {
          if (messages.containsKey(index)){
            return messages[index];
          } else {
            return MessageView(number: 0,text: "Oh no",username: "I am an error!",);
          }
        } else {
          fetchMessages(index, messageBulkLoadCount);
          return LinearProgressIndicator(
            valueColor: AlwaysStoppedAnimation<Color>(Theme.of(context).accentColor),
          );
        }
      }
    );
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
              onTap: (){
                setState(() {
                  currentChannel = channels[index];
                });
                Navigator.pop(context);
              },
              title: Text(channels[index], style: Theme.of(context).textTheme.bodyText1,),
              leading: Icon(Icons.message),
            )
        ),
      ),
      body: Builder(builder: chatBuilder,)
    );
  }
}
