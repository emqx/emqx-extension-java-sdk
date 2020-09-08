# Java SDK 快速入门

我们提供了 Java 语言适用的 SDK，以帮助快速、方便地开发 Java 语言的 EMQ X 扩展。

## 准备

1. 建议 JDK 1.8 及以上。

2. 在 IDE 中创建 Java 项目，将 `io.emqx.extension.jar` 及 `erlport.jar` 作为依赖引入该项目。

## 下载依赖

### SDK  `io.emqx.extension.jar` 

驱动版本选择参考[README.md](https://github.com/emqx/emqx-extension-java-sdk/blob/master/README.md)   ``SDK edition & EMQ X Broker edition``章节。

#### 1 直接下载jar

进入[下载页面](https://search.maven.org/search?q=a:emqx-extension-java-sdk) ，选择与您正在使用的EMQ X Broker版本对应的驱动。

#### 2 使用maven引入`io.emqx.extension.jar`

打开maven项目的pom.xml文件，在`<dependencies></dependencies>`标签内，添加记录：

```xml
<dependency>
  <groupId>io.emqx</groupId>
  <artifactId>emqx-extension-java-sdk</artifactId>
  <!-- 
			版本选择请按照部署的EMQ X Broker版本选择，版本对应请参考
			https://github.com/emqx/emqx-extension-java-sdk/blob/master/README.md
			的 SDK edition & EMQ X Broker edition 章节，
			此处的版本号<version>1.0.0</version>仅作为示例
 -->
  <version>1.0.0</version>
</dependency>
```

### SDK依赖下载 `erlport.jar`

使用[下载链接](https://github.com/emqx/emqx-extension-java-sdk/raw/master/deps/erlport-v1.2.2.jar)下载

## 示例

我们提供了 [SampleHandler.java](https://github.com/emqx/emqx-extension-java-sdk/blob/v4.2.0/examples/SampleHandler.java) 示例程序，
该程序继承自 SDK 中的 `DefaultCommunicationHandler` 类。该示例代码演示了如何挂载 EMQ X 系统中所有的钩子：

``` java
import emqx.extension.java.handler.*;
import emqx.extension.java.handler.codec.*;
import emqx.extension.java.handler.ActionOptionConfig.Keys;

public class SampleHandler extends DefaultCommunicationHandler {
    
    @Override
    public ActionOptionConfig getActionOption() {
        ActionOptionConfig option = new ActionOptionConfig();
        option.set(Keys.MESSAGE_PUBLISH_TOPICS, "#");
        option.set(Keys.MESSAGE_DELIVERED_TOPICS, "#");
        option.set(Keys.MESSAGE_ACKED_TOPICS, "#");
        option.set(Keys.MESSAGE_DROPPED_TOPICS, "#");
        
        return option;
    }
    
    // Clients
    @Override
    public void onClientConnect(ConnInfo connInfo, Property[] props) {
        System.err.printf("[Java] onClientConnect: connInfo: %s, props: %s\n", connInfo, props);
    }

    @Override
    public void onClientConnack(ConnInfo connInfo, ReturnCode rc, Property[] props) {
        System.err.printf("[Java] onClientConnack: connInfo: %s, rc: %s, props: %s\n", connInfo, rc, props);
    }

    @Override
    public void onClientConnected(ClientInfo clientInfo) {
        System.err.printf("[Java] onClientConnected: clientinfo: %s\n", clientInfo);
    }

    @Override
    public void onClientDisconnected(ClientInfo clientInfo, Reason reason) {
        System.err.printf("[Java] onClientDisconnected: clientinfo: %s, reason: %s\n", clientInfo, reason);
    }

    @Override
    public boolean onClientAuthenticate(ClientInfo clientInfo, boolean authresult) {
        System.err.printf("[Java] onClientAuthenticate: clientinfo: %s, authresult: %s\n", clientInfo, authresult);

        return true;
    }

    @Override
    public boolean onClientCheckAcl(ClientInfo clientInfo, PubSub pubsub, Topic topic, boolean result) {
        System.err.printf("[Java] onClientCheckAcl: clientinfo: %s, pubsub: %s, topic: %s, result: %s\n", clientInfo, pubsub, topic, result);

        return true;
    }

    @Override
    public void onClientSubscribe(ClientInfo clientInfo, Property[] props, TopicFilter[] topic) {
        System.err.printf("[Java] onClientSubscribe: clientinfo: %s, topic: %s, props: %s\n", clientInfo, topic, props);
    }

    @Override
    public void onClientUnsubscribe(ClientInfo clientInfo, Property[] props, TopicFilter[] topic) {
        System.err.printf("[Java] onClientUnsubscribe: clientinfo: %s, topic: %s, props: %s\n", clientInfo, topic, props);
    }

    // Sessions
    @Override
    public void onSessionCreated(ClientInfo clientInfo) {
        System.err.printf("[Java] onSessionCreated: clientinfo: %s\n", clientInfo);
    }

    @Override
    public void onSessionSubscribed(ClientInfo clientInfo, Topic topic, SubscribeOption opts) {
        System.err.printf("[Java] onSessionSubscribed: clientinfo: %s, topic: %s\n", clientInfo, topic);
    }

    @Override
    public void onSessionUnsubscribed(ClientInfo clientInfo, Topic topic) {
        System.err.printf("[Java] onSessionUnsubscribed: clientinfo: %s, topic: %s\n", clientInfo, topic);
    }

    @Override
    public void onSessionResumed(ClientInfo clientInfo) {
        System.err.printf("[Java] onSessionResumed: clientinfo: %s\n", clientInfo);
    }

    @Override
    public void onSessionDiscarded(ClientInfo clientInfo) {
        System.err.printf("[Java] onSessionDiscarded: clientinfo: %s\n", clientInfo);
    }
    
    @Override
    public void onSessionTakeovered(ClientInfo clientInfo) {
        System.err.printf("[Java] onSessionTakeovered: clientinfo: %s\n", clientInfo);
    }

    @Override
    public void onSessionTerminated(ClientInfo clientInfo, Reason reason) {
        System.err.printf("[Java] onSessionTerminated: clientinfo: %s, reason: %s\n", clientInfo, reason);
    }

    // Messages
    @Override
    public Message onMessagePublish(Message message) {
        System.err.printf("[Java] onMessagePublish: message: %s\n", message);
        
        return message;
    }

    @Override
    public void onMessageDropped(Message message, Reason reason) {
        System.err.printf("[Java] onMessageDropped: message: %s, reason: %s\n", message, reason);
    }

    @Override
    public void onMessageDelivered(ClientInfo clientInfo, Message message) {
        System.err.printf("[Java] onMessageDelivered: clientinfo: %s, message: %s\n", clientInfo, message);
    }

    @Override
    public void onMessageAcked(ClientInfo clientInfo, Message message) {
        System.err.printf("[Java] onMessageAcked: clientinfo: %s, message: %s\n", clientInfo, message);
    }
}
```

`SampleHandler` 主要包含两部分：

1. 重载了 `getActionOption` 方法。该方法对消息（Message）相关的钩子进行配置，指定了需要生效的主题列表。

   | 配置项                   | 对应钩子          |
   | :----------------------- | ----------------- |
   | MESSAGE_PUBLISH_TOPICS   | message_publish   |
   | MESSAGE_DELIVERED_TOPICS | message_delivered |
   | MESSAGE_ACKED_TOPICS     | message_acked     |
   | MESSAGE_DROPPED_TOPICS   | message_dropped   |

2. 重载了 `on<hookName>` 方法，这些方法是实际处理钩子事件的回调函数，函数命名方式为各个钩子名称变体后前面加 `on` 前缀，变体方式为钩子名称去掉下划线后使用骆驼拼写法（CamelCase），例如，钩子client_connect对应的函数名为onClientConnect。
   EMQ X 客户端产生的事件，例如：连接、发布、订阅等，都会最终分发到这些钩子事件回调函数上，然后回调函数可对各属性及状态进行相关操作。
   示例程序中仅对各参数进行了打印输出。如果只关心部分钩子事件，只需对这部分钩子事件的回调函数进行重载即可，不需要重载所有回调函数。


注：各回调函数的执行时机，与钩子一致。参见：[Hooks - EMQ X](https://docs.emqx.io/broker/latest/en/advanced/hooks.html#hookpoint)


在实现自己的扩展程序时，最简单的方式也是继承 `DefaultCommunicationHandler` 父类，该类对各钩子与回调函数的绑定进行了封装，并进一步封装了回调函数涉及到的参数数据结构，以方便快速上手使用。

## 部署

在完成开发后，需要将 Java 的代码内容部署到 EMQ X 中：

1. 编译扩展程序，将生成的 `.class`  文件（含包结构） 和 SDK 本身的 Jar 包
   拷贝至 EMQ X 安装目录下的 `data/extension` 目录下。
2. 修改 EMQ X 的 `emqx_extension_hook.conf` 配置文件：

``` properties
exhook.drivers = java
exhook.drivers.java.path = data/extension
exhook.drivers.java.init_module = SampleHandler     # 如果有包名，需包含包名
```

3. 使用 `bin/emqx consnole` 启动 EMQ X 并开启 `emqx_extension_hook` 插件。
4. 接入一个 MQTT 客户端，观察 console 中的输出。

## 进阶

如果对 Java 扩展程序的可控性要求更高，`DefaultCommunicationHandler` 类已无法满足需求时，可以通过实现 `CommunicationHandler` 接口，从更底层控制代码逻辑，编写更灵活的扩展程序。

```
package emqx.extension.java.handler;

public interface CommunicationHandler {
    
    public Object init();
    
    public void deinit();
}
```

- `init()` 方法：用于初始化，声明扩展需要挂载哪些钩子，以及挂载的配置
- `deinit()` 方法：用于注销。

详细数据格式说明，参见 [设计文档](https://github.com/emqx/emqx-extension-hook/blob/master/docs/design)

## 特别说明

标准输入输出流 System.in 和 System.out 用于 EMQ X 系统内部的交互，请不要在扩展程序中使用

目前可以使用 System.err 将 Java 侧的日志打印到 EMQ X 的控制台。
