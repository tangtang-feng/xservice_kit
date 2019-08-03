
 import 'demo_service.dart';
 import '../handlers/demo_service_message_to_flutter.dart'; 
 
 class DemoServiceRegister{
 
  static void register(){
      DemoService.regsiter();
      DemoServiceMessageToFlutter.regsiter();
   }
 
 }