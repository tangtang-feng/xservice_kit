import 'dart:async';
import 'package:xservice_kit_androidx/ServiceTemplate.dart';
import 'package:xservice_kit_androidx/ServiceGateway.dart';

class DemoService0 {
	static final ServiceTemplate _service = new ServiceTemplate("DemoService0");

	static ServiceTemplate service(){
		return _service;
	}

	static void regsiter() {
		ServiceGateway.sharedInstance().registerService(_service);
	}

  static Future<bool> MessageToNative(String message,[onInvocationException onException]) {
		 Map<String,dynamic> properties = new Map<String,dynamic>();
		 properties["message"]= message;
		 return _service.invokeMethod('MessageToNative',properties,onException).then<bool>((value){
        if(value == null){
		     return false;
			 }else{
				 return value;
			 }
		 });
	}

}