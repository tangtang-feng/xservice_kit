import 'dart:async';

import 'package:flutter/services.dart';
import 'package:xservice_kit_androidx/service_call_handler.dart';
import 'package:xservice_kit_androidx/service_gateway.dart';

class DemoService1MessageToFlutter extends ServiceCallHandler {

  static void regsiter() {
    ServiceGateway.sharedInstance().registerHandler(new DemoService1MessageToFlutter());
  }

  @override
  String name() {
    return "MessageToFlutter";
  }

  @override
  String service() {
    return "DemoService1";
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
