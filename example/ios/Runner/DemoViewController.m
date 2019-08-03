//
//  DemoViewController.m
//  Runner
//
//  Created by Jidong Chen on 2019/1/18.
//  Copyright © 2019 The Chromium Authors. All rights reserved.
//

#import "DemoViewController.h"
#import "ServiceDemoService0.h"

@interface DemoViewController ()<FlutterServiceEventListner>

@end

@implementation DemoViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    
    //Invoke method from flutter.
    [ServiceDemoService0 MessageToFlutter:^(NSDictionary *r){
        NSLog(@"Message to Flutter success!");
    } message:@"Message from native"];
    
    //Listen for flutter broadcast event.
    [ServiceDemoService0.service addListener:self forEvent:@"test"];
    
    
    //Send BroadCast event to flutter.
    [ServiceDemoService0.service emitEvent:@"test" params:@{}];
}


- (void)onEvent:(NSString *)event params:(NSString *)params
{
    if([event isEqual:@"test"]){
        NSLog(@"Did recive broadcast event from flutter");
    }
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
