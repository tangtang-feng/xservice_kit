//Generated by AIOCodeGen.
#import "ServiceGateway.h"
#import "DemoService1MessageToNative.h"
 
 @implementation DemoService1MessageToNative 
 - (void)onCall:(void (^)(BOOL))result message:(NSString *)message {
    //Add your handler code here!
 	//TODO:
 }
 #pragma mark - Do not edit these method.
 - (void)__flutter_p_handler_MessageToNative:(NSDictionary *)args result:(void (^)(BOOL))result {
     [self onCall:result message:args[@"message"] ];
 }
 + (void)load{
     [[ServiceGateway sharedInstance] registerHandler:[DemoService1MessageToNative new]];
 }
 - (NSString *)returnType{
   return @"NSString *";
 }
 - (NSString *)service{
   return @"DemoService1";
 }
 @end
