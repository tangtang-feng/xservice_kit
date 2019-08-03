import 'dart:async';
import 'package:xservice_kit/ServiceTemplate.dart';
import 'package:xservice_kit/ServiceGateway.dart';

class DemoService1 {
	static final ServiceTemplate _service = new ServiceTemplate("DemoService1");

	static ServiceTemplate service(){
		return _service;
	}

	static void regsiter() {
		ServiceGateway.sharedInstance().registerService(_service);
	}

  static Future<String> MessageToNative(String message,[onInvocationException onException]) {
		 Map<String,dynamic> properties = new Map<String,dynamic>();
		 properties["message"]= message;
		 return _service.invokeMethod('MessageToNative',properties,onException).then<String>((value){
        if(value == null){
		     return "";
			 }else{
				 return value;
			 }
		 });
	}
  static Future<int> MessageToNative2(String message,[onInvocationException onException]) {
		 Map<String,dynamic> properties = new Map<String,dynamic>();
		 properties["message"]= message;
		 return _service.invokeMethod('MessageToNative2',properties,onException).then<int>((value){
        if(value == null){
		     return 0;
			 }else{
				 return value;
			 }
		 });
	}
  static Future<double> MessageToNative3(String message,[onInvocationException onException]) {
		 Map<String,dynamic> properties = new Map<String,dynamic>();
		 properties["message"]= message;
		 return _service.invokeMethod('MessageToNative3',properties,onException).then<double>((value){
        if(value == null){
		     return 0.0;
			 }else{
				 return value;
			 }
		 });
	}
  static Future<String> MessageToNative4(String message,[onInvocationException onException]) {
		 Map<String,dynamic> properties = new Map<String,dynamic>();
		 properties["message"]= message;
		 return _service.invokeMethod('MessageToNative4',properties,onException).then<String>((value){
        if(value == null){
		     return "";
			 }else{
				 return value;
			 }
		 });
	}
  static Future<List> MessageToNative5(String message,[onInvocationException onException]) {
		 Map<String,dynamic> properties = new Map<String,dynamic>();
		 properties["message"]= message;
		 return _service.invokeMethod('MessageToNative5',properties,onException).then<List>((value){
        if(value == null){
		     return [];
			 }else{
				 return value;
			 }
		 });
	}
  static Future<Map> MessageToNative6(String message,[onInvocationException onException]) {
		 Map<String,dynamic> properties = new Map<String,dynamic>();
		 properties["message"]= message;
		 return _service.invokeMethod('MessageToNative6',properties,onException).then<Map>((value){
        if(value == null){
		     return {};
			 }else{
				 return value;
			 }
		 });
	}

}