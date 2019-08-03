//Generated code. Do not edit!
#import "Service_DemoService1.h"
#import "ServiceGateway.h"
#import "FlutterServiceTemplate.h"
 
@implementation Service_DemoService1

 + (FlutterServiceTemplate *)service {
     static id _instance = nil;
     static dispatch_once_t onceToken;
     dispatch_once(&onceToken, ^{
         _instance = [[FlutterServiceTemplate alloc] initWithName:@"DemoService1"];
     });
     return _instance;
 }
 
 + (void)load{
      [[ServiceGateway sharedInstance] addService:[self service]];
 }

+ (void)MessageToFlutter:(void (^)(NSDictionary *))resultCallback message:(NSString *)message  {
     NSMutableDictionary *tmp = [NSMutableDictionary dictionary];
     if(message) tmp[@"message"] = message;
     [self.service invoke:@"MessageToFlutter" args:tmp result:^(id tTesult) {
         if (resultCallback) {
             resultCallback(tTesult);
         }
     }];
 }

 @end
