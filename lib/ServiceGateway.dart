
import 'ServiceTemplate.dart';
import 'ServiceCallHandler.dart';


class ServiceGateway{

  Map<String,ServiceTemplate> _services = new Map();

  static ServiceGateway _instance = new ServiceGateway();

  static ServiceGateway sharedInstance(){
    return _instance;
  }

  void registerService(ServiceTemplate service){
    _services[service.serviceName()] = service;
  }
  void registerHandler(ServiceCallHandler handler){
    ServiceTemplate service = _services[handler.service()];
    if(service != null){
      service.regiserHandler(handler);
    }
  }
}