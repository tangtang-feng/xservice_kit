
import 'dart:async';
import 'package:flutter/services.dart';

class ServiceCallHandler{


  String name() {
    return "root";
  }

  String service() {
    return "root";
  }

  Future<dynamic> onMethodCall(MethodCall call) async{
    //TODO:Add you handler code here.
    return null;
  }

}
