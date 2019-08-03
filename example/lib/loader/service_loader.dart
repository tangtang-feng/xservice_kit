import '../DemoService/service/demo_service_register.dart';
 
class ServiceLoader{
  static void load(){
    DemoServiceRegister.register();
  }
}