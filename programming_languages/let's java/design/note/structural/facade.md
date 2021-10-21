# 门面模式定义

`Provide a unified interface to a set of interfaces in a subsystem. Facade Pattern defines a higher-level interface that makes the subsystem easier to use.`

**门面模式为子系统提供一组统一的接口，定义一组高层接口让子系统更易用**

系统A有a,b,c,d四个接口， 系统B完成某个业务功能，需要调用A系统的a,b,d接口。利用门面模式，我们提供一个包裹a,b,d接口调用的门面接口x。给B直接使用。

# 门面模式应用场景

**1.解决易用性问题**

门面模式可以用来封装系统的底层实现，隐藏系统的复杂性，提供一组更加简单易用、更高层的接口。例如Linux。

**2.解决性能问题**

我们通过将多个接口调用替换为一个门面接口调用，减少网络通信的成本，提高client端的响应速度。

**3.解决分布式事务的问题**

