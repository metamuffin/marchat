


import 'package:flutter/material.dart';
import 'package:marchat_client/websocket.dart';

class ChannelAddUIButton extends StatefulWidget {
  ChannelAddUIButton({Key key}) : super(key: key);

  @override
  _ChannelAddUIButtonState createState() => _ChannelAddUIButtonState();
}

class _ChannelAddUIButtonState extends State<ChannelAddUIButton> {

  TextEditingController newTextChannelNameFieldController = TextEditingController();
  bool workingState = false;


  void showOverlay(){
    showModalBottomSheet(context: context, builder: (BuildContext bc){
      return Container(
        height: 400,
        color: Colors.transparent,
        child: Container(
          decoration: new BoxDecoration(
            borderRadius: new BorderRadius.only(
              topLeft: const Radius.circular(10.0),
              topRight: const Radius.circular(10.0)
            )
          ),
          child: Center( 
            child: Column(
              children: (!workingState)
              ? <Widget>[
                  TextField(
                    controller: newTextChannelNameFieldController,
                    decoration: InputDecoration(
                      contentPadding: EdgeInsets.fromLTRB(20, 15, 20, 15),
                      labelText: "Channel name"
                    ),
                  ),
                  FlatButton(
                    child: Text("Create Channel"),
                    onPressed: (){
                      setState((){
                        workingState = true;
                      });
                      oke.on("channel_create",(_) {
                        setState((){
                          workingState = true;
                        });
                        Navigator.pop(context);
                      });
                    },
                  )
                ]
              : <Widget>[
                Center(
                  child: CircularProgressIndicator(
                    valueColor: AlwaysStoppedAnimation<Color>(Theme.of(context).accentColor),
                  )
                )
              ]
            )
          ),
        )
      );
    });
  }
  
  @override
  Widget build(BuildContext context) {
    return IconButton(
      icon: Icon(Icons.add),
      onPressed: showOverlay,
    );
  }
}