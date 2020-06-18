

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
      debugPrint(data);
      setState(() {
        debugPrint("Blub1 ${data.channels[0]}");
        channels = data["channels"];
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

  void addChannel(){
    
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
            Card(
              child: Text(channels[index]),
            )
        ),
      ),
      body: Builder(builder: chatBuilder,)
    );
  }
}
