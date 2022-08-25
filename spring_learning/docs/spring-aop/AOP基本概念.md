# AOP基本概念

面向切面编程主要实现的目的是针对业务处理过程中的切面进行提取，它所面对的是处理过程中的某个步骤或阶段，以获得逻辑过程中各部分之间低耦合性的隔离效果。

面向方面编程 (AOP) 通过提供另一种思考程序结构的方式来补充面向对象编程 (OOP)。 OOP 中模块化的关键单元是类，而 AOP 中模块化的单元是方面。 方面支持跨多种类型和对象的关注点（例如事务管理）的模块化。

## 为什么要引入AOP

Java OOP存在局限性：

1. 静态化语言:类结构一旦定义，不容易被修改
2. 侵入性扩展:通过继承和组合组织新的类结构

## AOP使用场景

* 日志场景
  * 诊断上下文，如:log4j 或 logback 中的 _x0008_MDC
  * 辅助信息，如:方法执行时间
* 统计场景
  * 方法调用次数
  * 执行异常次数
  * 数据抽样
  * 数值累加
* 安防场景
  * 熔断，如:Netflix Hystrix
  * 限流和降级:如:Alibaba Sentinel
  * 认证和授权，如:Spring Security
  * 监控，如:JMX
* 性能场景
  * 缓存，如 Spring Cache
  * 超时控制

## AOP的基本组件

### Aspect

Aspect是横切关注点的模块化单元，有点像Java类，但也有可能包括PointCuts，Advice以及Inter-Type声明。

### Join Point

表示在程序中明确定义的点，典型的包括方法调用，对类成员的访问以及异常处理程序块的执行等等，它自身还可以嵌套其它 joint point。

### Point Cut

表示一组 joint point，这些 joint point 或是通过逻辑关系组合起来，或是通过通配、正则表达式等方式集中起来，它定义了相应的 Advice 将要发生的地方。

### Advice

Advice 定义了在 pointcut 里面定义的程序点具体要做的操作，它通过 before、after 和 around 来区别是在每个 joint point 之前、之后还是代替执行的代码。

### Introduction

AspectJ 中的类型间声明是跨越类及其层次结构的声明。 他们可以声明跨越多个类的成员，或者改变类之间的继承关系。

# Java AOP设计模式

## 代理模式

* Java 静态代理
  * 常用 OOP 继承和组合相结合
* Java 动态代理
  * JDK 动态代理
  * 字节码提升，如 CGLIB

## 判断模式

判断来源

* 类型(Class)
* 方法(Method)
* 注解(Annotation)
* 参数(Parameter) 
* 异常(Exception)

## 拦截模式

拦截类型

* 前置拦截(Before)
* 后置拦截(After)
* 异常拦截(Exception)

# Spring AOP

## Spring AOP功能概述

**核心特性**

* 纯 Java 实现、无编译时特殊处理、不修改和控制 ClassLoader
* 仅支持方法级别的 Join Points
* 非完整 AOP 实现框架
* Spring IoC 容器整合
* AspectJ 注解驱动整合(非竞争关系)

## Spring AOP编程模型

### XML编程模型

实现:Spring Extensble XML Authoring

XML元素

* 激活 AspectJ 自动代理:`<aop:aspectj-autoproxy/>`
* 配置:`<aop:config/>`
* Aspect :` <aop:aspect/>`
* Pointcut :`<aop:pointcut/>`
* Advice :`<aop:around/>、<aop:before/>、<aop:after-returning/>、<aop:after-throwing/> 和 <aop:after/>`
* Introduction :`<aop:declare-parents/>`
* 代理 Scope : `<aop:scoped-proxy/>`

### Annotation编程模型

实现:Enable 模块驱动，`@EnableAspectJAutoProxy`

注解:

* 激活 AspectJ 自动代理:`@EnableAspectJAutoProxy`
* Aspect: `@Aspect`
* Pointcut:`@Pointcut`
* Advice:`@Before、@AfterReturning、@AfterThrowing、@After、@Around`
* Introduction:`@DeclareParents`

### API编程模型

实现:JDK 动态代理、CGLIB 以及 AspectJ

API:

* 代理:`AopProxy`
* 配置:`ProxyConfig`
* Join Point:`JoinPoint`
* Pointcut :`Pointcut`
* Advice :`Advice、BeforeAdvice、AfterAdvice、AfterReturningAdvice、 ThrowsAdvice`

## Spring AOP设计目标

Spring AOP 的 AOP 方法不同于大多数其他 AOP 框架。 目的不是提供最完整的 AOP 实现（尽管 Spring AOP 非常有能力）。 相反，其目的是提供 AOP 实现和 Spring IoC 之间的紧密集成，以帮助解决企业应用程序中的常见问题。
Spring AOP 从不努力与 AspectJ 竞争以提供全面的 AOP 解决方案。 我们认为 Spring AOP 等基于代理的框架和 AspectJ 等成熟框架都很有价值，它们是互补的，而不是竞争。Spring 将 Spring AOP 和 IoC 与 AspectJ 无缝集成，以实现 AOP 的所有使用 一致的基于 Spring 的应用程序架构。 此集成不会影响 Spring AOP API 或 AOP Alliance API。 Spring AOP 保持向后兼容。

## Spring AOP Advice类型

* 环绕(Around)
* 前置(Before)
* 后置(After)
  * 方法执行
  * finally 执行
  * 异常(Exception)

## Spring AOP代理实现

### JDK动态代理

基于接口代理

### Cglib动态代理

基于类代理(字节码提升)

### Aspectj代理

#### Aspectj语法

* Aspect
* Join Points
* Pointcuts
* Advice
* Introduction

#### Aspectj注解

* 激活 AspectJ 自动代理:`@EnableAspectJAutoProxy`
* Aspect: `@Aspect`
* Pointcut:`@Pointcut`
* Advice:`@Before、@AfterReturning、@AfterThrowing、@After、@Around`
* Introduction:`@DeclareParents`