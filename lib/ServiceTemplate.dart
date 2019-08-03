import 'dart:async';
import 'package:flutter/services.dart';
import 'ServiceCallHandler.dart';

typedef void ServiceEventListner(String event , Map<dynamic,dynamic> params);
typedef void ServiceEventListnerRemoveCallback();

typedef dynamic onInvocationException(Exception e);

class ServiceTemplate implements ServiceCallHandler{
  MethodChannel _channel;
  EventChannel _eventChannel;
  String _serviceName = "root";

  Map<String, ServiceCallHandler> _callHandlers = new Map();
  Map<String, List<ServiceEventListner>> _eventListners = new Map();

  Map<int, StreamSubscription<dynamic>> _subscriptions =
      new Map<int, StreamSubscription<dynamic>>();
  int _subscriptionCounter = 0;
  Stream<dynamic> _stream = null;

  ServiceTemplate(String serviceName) {
    _serviceName = serviceName;
    _channel = new MethodChannel(serviceName + "_method_channel");
    _eventChannel = new EventChannel(serviceName + "_event_channel");

    regiserHandler(this);

    _channel.setMethodCallHandler((MethodCall call) {
      final ServiceCallHandler handler = _callHandlers[call.method];
      if (handler != null) {
        return handler.onMethodCall(call);
      } else {
        return null;
      }
    });
  }

  String serviceName() {
    return _serviceName;
  }

  Future<dynamic> invokeMethod(String method, [dynamic arguments,onInvocationException onException]) async{
    try{
      dynamic ret = await _channel.invokeMethod(method,arguments);
      return ret;
    }catch(e){
      if(onException != null){
        return onException(e);
      }else{
        return null;
      }
    }
  }


  void regiserHandler(ServiceCallHandler handler) {
    _callHandlers[handler.name()] = handler;
  }

  //anonymous interfaces
  //Return a unique id for subscription.
  @Deprecated('should avoid anonymous event try to use addEventListner instead')
  int listenEvent(void onData(dynamic event)) {
    if (_stream == null) {
      _stream = _eventChannel.receiveBroadcastStream();
    }
    final dynamic subscription = _stream.listen(onData);
    _subscriptions[_subscriptionCounter] = subscription;
    return _subscriptionCounter++;
  }

  //Cancel event for subscription with ID.
  @Deprecated('should avoid anonymous event try to use addEventListner instead')
  void cancelEventForSubscription(int subID) {
    try{
      StreamSubscription<dynamic> sub = _subscriptions[subID];
      if (sub != null) {
        sub.cancel();
        _subscriptions.remove(subID);
      }
    }catch(e){
      //Try to remove non existing ID.
    }
  }

  void emitEvent(String event , Map<dynamic,dynamic> params){
    if(event == null){
      return;
    }

    Map<dynamic,dynamic> msg = Map();
    msg["event"] = event;
    msg["params"] = params;
    _channel.invokeMethod("__event__",msg);
  }

  //Add listner for broadcast event.
  ServiceEventListnerRemoveCallback addEventListner(String event , ServiceEventListner listner){

    if(event == null || listner == null){
      return (){};
    }

    List<ServiceEventListner> list = _eventListners[event];
    if(list == null){
      list = List();
      _eventListners[event] = list;
    }

    list.add(listner);

    return (){
      list.remove(listner);
    };
  }

  bool _onEvent(String event , Map<dynamic,dynamic> params){
    if(event == null) return false;
    List<ServiceEventListner> list = _eventListners[event];
    if(list != null){
      for(ServiceEventListner l in list){
        l(event,params);
      }
    }

    return true;
  }

  MethodChannel methodChannel() {
    return _channel;
  }

  EventChannel eventChannel() {
    return _eventChannel;
  }

  @override
  String name() {
    return "__event__";
  }

  @override
  String service() {
    return _serviceName;
  }

  @override
  Future<dynamic> onMethodCall(MethodCall call) async{
    return _onEvent(call.arguments["event"], call.arguments["params"]);
  }
}
