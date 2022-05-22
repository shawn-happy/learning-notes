## Spring Bean作用域

| 来源        | 说明                                                     |
| ----------- | -------------------------------------------------------- |
| singleton   | spring bean默认的作用域，一个BeanFactory有且仅有一个实例 |
| prototype   | 原型作用域，每次依赖查找和依赖注入都会生成新的bean对象   |
| request     | 将spring bean存储在ServletRequest上下文中                |
| session     | 将spring bean存储在HttpSession上下文中                   |
| application | 将spring bean存储在ServletContext上下文中                |

## singleton bean scope

`singleton`是`spring`容器中bean的默认作用域。它告诉容器仅创建和管理一个`bean`类实例。该单个实例存储在此类单例`bean`的缓存中，并且对该命名`bean`的所有后续请求和引用都返回该缓存的实例。

## prototype bean scope

每次应用程序对`Bean`进行请求时，原型作用域都会创建一个新的`Bean`实例。

您应该知道，销毁`bean`生命周期方法不调用原型作用域`bean`，只调用初始化回调方法。因此，作为开发人员，您要负责清理原型作用域的`bean`实例以及其中包含的所有资源。

> 通常，您应该为所有有状态`bean`使用原型范围，为无状态`bean`使用单例范围。
>
> 要在请求、会话、应用程序和`websocket`范围内使用`bean`，您需要注册`RequestContextListener`或`RequestContextFilter`.
>
> Spring容器没有办法管理prototype bean的完整的生命周期，也没有办法记录实例的存在。销毁回调方法将不会执行，可以利用BeanPostProcessor进行清扫工作

## request scope

在请求范围中，容器为每个`HTTP`请求创建一个新实例。因此，如果服务器当前处理50个请求，那么容器最多可以有50个`bean`类的单独实例。对一个实例的任何状态更改对其他实例都是不可见的。一旦请求完成，这些实例就会被销毁。

## session scope

在会话范围中，容器为每个`HTTP`会话创建一个新实例。因此，如果服务器有20个活动会话，那么容器最多可以有20个`bean`类的单独实例。在单个会话生命周期内的所有`HTTP`请求都可以访问该会话范围内相同的单个`bean`实例。

在会话范围内，对一个实例的任何状态更改对其他实例都是不可见的。一旦会话在服务器上被销毁/结束，这些实例就会被销毁。

## application scope

在应用程序范围内，容器为每个`web`应用程序运行时创建一个实例。它几乎类似于单例范围，只有两个不同之处。即：

1. 应用程序作用域`bean`是每个`ServletContext`的单例对象，而单例作用域`bean`是每个`ApplicationContext`的单例对象。请注意，单个应用程序可能有多个应用程序上下文。
2. 应用程序作用域`bean`作为`ServletContext`属性可见。

## websocket scope

`WebSocket`协议支持客户端和远程主机之间的双向通信，远程主机选择与客户端通信。`WebSocket`协议为两个方向的通信提供了一个单独的`TCP`连接。这对于具有同步编辑和多用户游戏的多用户应用程序特别有用。

在这种类型的`Web`应用程序中，`HTTP`仅用于初始握手。如果服务器同意，服务器可以以`HTTP`状态101（交换协议）进行响应。如果握手成功，则`TCP`套接字保持打开状态，客户端和服务器都可以使用该套接字向彼此发送消息。

`websocket` `bean`范围的`Java`配置示例:

```java
@Component
@Scope("websocket")
public class BeanClass {
}
```

`websocket` `bean`范围的`XML`配置示例:

```java
<bean id="beanId" class="com.howtodoinjava.BeanClass" scope="websocket" />
```

请注意，`websocket`范围内的`bean`通常是单例的，并且比任何单独的`WebSocket`会话寿命更长。

## 自定义threadlocal scope

* 实现`Scope`接口

* 注册Scope。`ConfigurableBeanFactory#registerScope`

* 配置

  ```xml
  <bean class="org.springframework.beans.factory.config.CustomScopeConfigurer">
  	<property name="scopes">
      	<map>
          	<entry key=""></entry>
          </map>
      </property>
  </bean>
  ```

## Spring Cloud RefreshScope

