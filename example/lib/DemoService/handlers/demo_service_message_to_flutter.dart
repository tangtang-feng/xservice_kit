import 'dart:async';

import 'package:flutter/services.dart';
import 'package:xservice_kit_androidx/ServiceCallHandler.dart';
import 'package:xservice_kit_androidx/ServiceGateway.dart';

class DemoServiceMessageToFlutter extends ServiceCallHandler {

  static void regsiter() {
    ServiceGateway.sharedInstance().registerHandler(new DemoServiceMessageToFlutter());
  }

  @override
  String name() {
    return "MessageToFlutter";
  }

  @override
  String service() {
    return "DemoService";
  }

  @override
  Future<Map> onMethodCall(MethodCall call) {
    return onCall(call.arguments["message"]);
  }

//==============================================Do not edit code above!
  Future<Map> onCall(String message) async{
    //TODO:Add you handler code here.
    return null;
  }
}
