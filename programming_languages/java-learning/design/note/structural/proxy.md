# 代理模式定义

In computer programming, the proxy pattern is a software design pattern. A proxy, in its most general form, is a class functioning as an interface to something else. The proxy could interface to anything: a network connection, a large object in memory, a file, or some other resource that is expensive or impossible to duplicate. In short, a proxy is a wrapper or agent object that is being called by the client to access the real serving object behind the scenes. Use of the proxy can simply be forwarding to the real object, or can provide additional logic. In the proxy, extra functionality can be provided, for example caching when operations on the real object are resource intensive, or checking preconditions before operations on the real object are invoked. For the client, usage of a proxy object is similar to using the real object, because both implement the same interface.

# 动态代理

静态代理需要针对每个类都创建一个代理类，并且每个代理类中的代码都有点像模板式的重复代码，增加了维护成本和开发成本。对于静态代理存在的问题，我们可以通过动态代理来解决，我们不事先为每个原始类编写代理类，而是在运行的时候动态地创建原始类对应的代理类。然后在系统中用代理类替换掉原始类。

# 使用场景

1. 业务系统中的非功能性的需求开发

   * 事务
   * 监控
   * 统计
   * 鉴权
   * 限流
   * 日志
   * 幂等

2. PRC，cache

   RPC框架可以看做是一个代理模式

   远程代理

   通过远程代理，将网络通信，数据编码等细节隐藏起来。客户端在使用RPC服务的时候，就像使用本地函数一样，无需了解跟服务器交互的细节。

   缓存：

   如果我们要开发一个接口请求的缓存功能，对于某些接口请求，如果入参相同，在设定的过期时间内，直接返回缓存结果，而不用重新进行逻辑处理。

# 代码示例

需求：统计接口执行的效率

