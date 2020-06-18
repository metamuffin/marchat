

import 'package:flutter/material.dart';

class MessageView extends StatelessWidget {
  final String text;
  final String username;
  final int number;
  const MessageView({Key key, @required this.text, @required this.username, @required this.number,}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Card(
      child: Row(
        children: <Widget>[
          Column(
            children: <Widget>[
              Text(username, style: Theme.of(context).textTheme.caption,),
              Text(text)
            ],
          )
        ],
      ),
    );
  }
}