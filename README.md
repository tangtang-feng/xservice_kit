# xservice_kit —— 基于Channel通信的统一协议代码生成方案

## QuikStart

### Xservice的优势
- 统一标准化配置
- 一份配置三端代码生成，告别重复手写代码工作。
- 消息类型显示指定，方便消息定义和调用，增加类型安全性。
- 方便支持双向一对一，一对多消息代码生成。
- 自定义消息过滤支持，防止自定义类型序列化过程中的异常。

### 接入简介

1. 安装命令行工具:npm install xservice -g
2. 编写配置文件放置到 ServicesYaml（可自定义）文件夹
3. 用命令行生成代码
例如:```xservice -o out -p fleamarket.taobao.com.xservicekitexample -t yaml ServicesYaml```
4. 拷贝代码到项目中。
5. java和Dart需要手动调用ServiceLoader启动服务。

具体可以参考项目example CodeGen文件夹下面的配置和脚本

详细步骤

- 在Flutter工程pubspec.yaml加入xservice_kit依赖: ```xservice_kit:^0.0.27```
- 安装node（代码生成工具使用node开发），然后npm install xservice -g
- 编写配置文件
- 运行xservice命令行生成代码
- 将生成代码移动到项目，然后在程序的开始调用Serviceloader

在工程中可以开发简单的脚本进行最后两步的动作。具体可以参考flutter_boost里面对xservice_kit的使用。

我们看一下生成代码的基本结构：

![](https://gw.alicdn.com/tfs/TB1DUM4IOrpK1RjSZFhXXXSdXXa-1230-1050.png)


## 详细使用文档

<a name="3gz5bh"></a>
### [](#3gz5bh)Native向Flutter发送消息
<a name="hkgizq"></a>
#### [](#hkgizq)一对一消息 , 一对一消息在配置文件里面定义好，会自动生成调用方法。
iOS

```objectivec
[ServiceName.service method];
```
Android

```java
ServiceName.getService().method();
```
Dart:<br />在Service对应的Handler里面实现method对应逻辑即可。
<a name="wxp8cz"></a>
##### [](#wxp8cz)一对多广播消息：广播是通用的接口。
iOS

```objectivec
[ServiceName.service emitEvent:@"event_name" params:@{@"key":@"value"}];
```
Android

```java
ServiceName.getService().emitEvent("event_name",params);
```
Dart:

```java
ServiceName.service().addEventListner("event_name",listner);
```

<a name="mll1nq"></a>
### [](#mll1nq)Flutter向Native发送消息
基本步骤跟前面一样，只不过由Flutter来调用方法，在Native实现Handler。


<a name="xotbnz"></a>
## [](#xotbnz)基本概念

<a name="z4c3uc"></a>
### [](#z4c3uc)Service

Service可以理解为是一组Handler的组织者，里面主要包含消息派发逻辑和注册到特定channel的逻辑。

<a name="i013vp"></a>
### [](#i013vp)Message
目前消息管道支持双向通信<br />消息目前分为两种:<br />flutter 特指 flutter 发送给 native的消息 flutter->native。<br />native 特指 native 发送给 flutter的小时 native->flutter。

<a name="kycoek"></a>
### [](#kycoek)Type

类型是消息传递的时候非常容易出错的类型，在这里我们约定以下类型。
<a name="n1u8rk"></a>
#### [](#n1u8rk)Dart

- basic types

  - int

  - double

  - String

  - bool

- collections

  - List

  - Map


<a name="8r47kp"></a>
#### [](#8r47kp)Dart -> OC类型影射表

- int -> int64_t

- double -> double

- bool -> BOOL

- String -> NSString

- List -> NSArray

- Map -> NSDictionary


<a name="ub0zlb"></a>
#### [](#ub0zlb)Dart -> Java类型影射表

- int -> int

- double -> double

- bool -> boolean

- String -> String

- List -> List

- Map -> Map


<a name="uurrao"></a>
## [](#uurrao)使用手册
接入使用工具只需要两步

1. 书写配置文档。

2. 执行代码生成脚本。


<a name="4hvllw"></a>
### [](#4hvllw)配置文件
<a name="q2k8gc"></a>
#### [](#q2k8gc)配置支持yaml和json，建议使用yaml比较简洁。
配置文件放在同一个文件夹下面
然后执行：

```
xservice -o out -p fleamarket.taobao.com.xservicekitexample -t yaml ServicesYaml
```


<a name="4aiimk"></a>
#### [](#4aiimk)Yaml 配置实例

```yaml
name : GeneralService #服务名称

messages: #消息

 -
  name : popNative
  returnType : String
  messageType : flutter
  channelType : method
  args : 
   - 
    name : arg1
    type : int
   - 
    name : arg2
    type : String
    
    
 -
  name : popFlutter
  returnType : String
  messageType : native
  channelType : method
  args : 
   - 
    name : arg1
    type : int
   - 
    name : arg2
    type : String
```


需要增加message 只需要复制即可


Json配置请参考项目example/CodeGen/Services
Yaml配置请参考项目example/CodeGen/ServicesYaml




## 设计介绍

### 简介

Flutter与Native的通信是通过Channel实现的。实际上这个Channel本质是在C++层维护的一个map数据结构，由key-value的形式对channel name和handler进行映射。Dart VM通过C接口与C++对象进行通信，iOS原生对C++混编有良好的支持，而Android则是通过JNI的形式与C++进行通信。通过这种形式，Flutter能够与Native基于Channel的这种抽象进行无缝的通信。

官方实现的Channel会在通信过程当中对Dart的类型与Objective-C，Java类型进行转换。所以使用起来还是挺方便，但是对于一些自定义的类型，在转换过程当中有可能会出现一些不可预期的问题。

### 官方插件系统
官方支持的插件，实际上是基于Channel通信的简单封装。Android Studio在生成一个插件项目的时候，会分别生成Dart，OC，Java的插件类。我们大致看一下：

Java

```Java
public class FlutterPlugin implements MethodCallHandler {
  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_plugin");
    channel.setMethodCallHandler(new FlutterPlugin());
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } else {
      result.notImplemented();
    }
  }
}
```

OC

```
@implementation FlutterPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"flutter_plugin"
            binaryMessenger:[registrar messenger]];
  FlutterPlugin* instance = [[FlutterPlugin alloc] init];
  [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
  if ([@"getPlatformVersion" isEqualToString:call.method]) {
    result([@"iOS " stringByAppendingString:[[UIDevice currentDevice] systemVersion]]);
  } else {
    result(FlutterMethodNotImplemented);
  }
}

@end
```

Dart

```
class FlutterPlugin {
  static const MethodChannel _channel =
      const MethodChannel('flutter_plugin');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
```

插件的基本结构其实包含以下主要几个方法:

- invoke接口，也就是发送消息的接口
- onMethodCall，消息处理方法
- 注册接口

#### 官方插件的好处
- 简单，使用方便
- 拥有良好的项目结构跟原生flutter项目兼容性很好
- 轻量级

#### 官方插件的一些问题
- 需要手写handler，if else噩梦。
- 插件包含三份代码：dart，java，oc。同一个消息需要手写三份重复的代码。
- 没有统一定义和维护消息的格式和标准。
- Channel是支持双向通信的，但是官方插件生成出来的代码没有这个支持。
- 需要为不同的插件生成不同的包，粒度不好把握。
- 发送消息的时候无法去做消息内容过滤和检查，可能会有类型导致的异常。

## Xservice

### 简介
为了解决官方插件存在的一些问题，我们同样是基于Flutter本身提供的Channel实现了一个更加强大的通信标准模块。最初的想法来源于protobuf此类的RPC通信标准，从广义上来说Flutter与Native的通信的抽象，也可以大致理解为RPC，只是一种特殊类型。

类似于protobuf的结构，我们开发了一个基础库xservice_kit以及xservice代码生成工具。我们定义了消息的基本结构，只需要配置一个统一配置，即可生成三端代码。

### 主要概念
![](https://gw.alicdn.com/tfs/TB1uQZ0IHPpK1RjSZFFXXa5PpXa-1095-408.png)

这里主要有Service和Handler，Message等概念。
我们在配置文件里面去定义Message以及Message所在的Service，代码生成工具会去解析生成对应的Service以及消息处理者Handler。

Service
Service可以理解为是一组Handler的组织者，里面主要包含消息派发逻辑和注册到特定channel的逻辑。

Handler
消息的最终处理者，由消息对应的Service集中管理。

Message
目前消息管道支持双向通信
消息目前分为两种，消息的类型是以消息的发送者来定义的，比如：
flutter 特指 flutter 发送给 native的消息 flutter->native。
native 特指 native 发送给 flutter的消息 native->flutter。

消息也分为一对一这里有invoke的概念，一对多就是监听与广播的支持。这里一对一，一对多都是支持双向的。


我们支持Json和Yaml格式的配置文件，推荐Yaml比较直观：

一个典型的消息配置文件：

```
name : GeneralService 


messages: 


 -
  name : Message1
  returnType : String
  messageType : flutter
  channelType : method
  args : 
   - 
    name : arg1
    type : int
   - 
    name : arg2
    type : String
    
    
 -
  name :Message2
  returnType : String
  messageType : native
  channelType : method
  args : 
   - 
    name : arg1
    type : int
   - 
    name : arg2
    type : String


```

注意我们的消息里面所带的参数是统一由Dart的类型来进行书写，参数的显示类型配置也是相对于官方插件的一个优势。

### 接入简介
接入只需要几步:

- 在Flutter工程pubspec.yaml加入xservice_kit依赖: ```xservice_kit:^0.0.27```
- 安装node（代码生成工具使用node开发），然后npm install xservice -g
- 编写配置文件
- 运行xservice命令行生成代码
- 将生成代码移动到项目，然后在程序的开始调用Serviceloader

在工程中可以开发简单的脚本进行最后两步的动作。具体可以参考flutter_boost里面对xservice_kit的使用。

我们看一下生成代码的基本结构：

![](https://gw.alicdn.com/tfs/TB1DUM4IOrpK1RjSZFhXXXSdXXa-1230-1050.png)



### Xservice的优势
- 统一标准化配置
- 一份配置三端代码生成，告别重复手写代码工作。
- 消息类型显示指定，方便消息定义和调用，增加类型安全性。
- 方便支持双向一对一，一对多消息代码生成。
- 自定义消息过滤支持，防止自定义类型序列化过程中的异常。

