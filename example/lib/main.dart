import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:xservice_kit_androidx/xservice_kit.dart';
import 'package:xservice_kit_example/loader/service_loader.dart';
import 'package:xservice_kit_example/DemoService0/service/demo_service0.dart';

void main(){
  ServiceLoader.load();

  runApp(new MyApp());

  print("Sending flutter message");
  DemoService0.MessageToNative("This is message from flutter",(Exception e){
    return false;
  }).then((bool value){
    if(value){
      print("Sending flutter message to native. succuess");
    }else{
      print("Sending flutter message to native. failed");
    }
  });

  DemoService0.service().addEventListner("test", (String event,Map<dynamic,dynamic> params){
    print("Recieved broadcast event from native $event $params");
  });

  print("Sending broadcast event to native");
  DemoService0.service().emitEvent("test", {});

}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => new _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await XserviceKit.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      home: new Scaffold(
        appBar: new AppBar(
          title: const Text('Plugin example app',textScaleFactor: 1.0),
        ),
        body: new Center(
          child: new Text('Running on: $_platformVersion\n',textScaleFactor: 1.0),
        ),
      ),
    );
  }
}
