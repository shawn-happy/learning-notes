## Ioc

### 什么是ioc

控制反转(**inversion of control,简称ioc**)是一种设计原则，顾名思义，它就是把原来代码里需要实现的对象创建，依赖，反转给容器来帮忙实现。

哪些方面的控制被反转了？。是依赖对象的获得被反转了，因为大多数应用程序都是由两个或是更多的类通过彼此的合作来实现业务逻辑，这使得每个对象都需要获取与其合作的对象（也就是它所依赖的对象）的引用。如果这个获取过程要靠自身实现，那么这将导致代码高度耦合并且难以维护和调试。

Class A中用到了Class B的对象b，一般情况下，需要在A的代码中显式的new一个B的对象。

采用依赖注入技术之后，A的代码只需要定义一个私有的B对象，不需要直接new来获得这个对象，而是通过相关的容器控制程序来将B对象在外部new出来并注入到A类里的引用中。而具体获取的方法、对象被获取时的状态由配置文件（如XML）来指定。

### Ioc实现策略

根据维基百科里的说法，主要技术有：

* 使用`service locator pattern`
* 依赖注入(`dependency injection`， **简称DI**):
  * 基于接口。实现特定接口以供外部容器注入所依赖类型的对象。
  * 基于 setter方法。实现特定属性的public set方法，来让外部容器调用传入所依赖类型的对象。
  * 基于构造函数。实现特定参数的构造函数，在新建对象时传入所依赖类型的对象。
  * 基于注解。基于Java的注解功能，在私有变量前加“@Autowired”等注解，不需要显式的定义以上三种代码，便可以让外部容器传入对应的对象。该方案相当于定义了public的set方法，但是因为没有真正的set方法，从而不会为了实现依赖注入导致暴露了不该暴露的接口（因为set方法只想让容器访问来注入而并不希望其他依赖此类的对象访问）。
* 依赖查找(`dependency lookup`)
  * 依赖查找更加主动，在需要的时候通过调用框架提供的方法来获取对象，获取时需要提供相关的配置文件路径、key等信息来确定获取对象的状态。
* 使用模板方法模式
* 使用策略模式

### Ioc的设计目的

控制反转可用于以下设计目的：

* 使任务的执行与实现脱钩。(松耦合)
* 使模块专注于其设计的任务。
* 使模块摆脱关于其他系统如何做而依赖合同的假设。
* 为防止在更换模块时产生副作用。

控制反转有时被戏称为好莱坞原则：不要打电话给我们，我们会打电话给您。

### ioc容器的职责

* 依赖处理
  * 依赖注入
  * 依赖查找
* 生命周期管理
  * 容器
  * 托管的资源（`java beans`或其他资源）
* 配置
  * 容器
  * 外部化配置
  * 托管的资源（`java beans`或其他资源）

### ioc容器的实现

* java se
  * java beans
  * java serviceloader spi
  * jndi(java naming and directory interface)
* java ee
  * ejb(enterprise java bease)
  * servlet
* 开源框架
  * [google guice](https://github.com/google/guice)
  * [spring framework](https://spring.io/projects/spring-framework)

### Ioc的特征

1. 可以管理应用代码
2. 可以快速启动
3. 不需要特殊的部署步骤把对象部署到容器里。
4. 一个具有如此小的占用空间和最小的api依赖性的容器，可以在各种环境中运行。
5. 一个容器，它设置了添加托管对象的门槛，使其部署工作和性能开销非常低，以至于可以部署和管理细粒度的对象以及粗粒度的组件

### ioc的优点

1. 避免了单体容器
2. 最大化代码复用率
3. 更好的面向对象
4. 更好的生产力
5. 更好的可测试性

### 依赖查找和依赖注入

实现控制反转主要有两种方式：依赖注入和依赖查找。两者的区别在于，前者是被动的接收对象，在类A的实例创建过程中即创建了依赖的B对象，通过类型或名称来判断将不同的对象注入到不同的属性中，而后者是主动索取相应类型的对象，获得依赖对象的时间也可以在代码中自由控制。

## Spring IoC 

spring-framework实现了控制反转原则，主要方式是依赖查找和依赖注入。

### 依赖查找

* 可以根据bean-name查找：实时和延迟
* 根据bean-type查找：单个bean对象，集合bean对象
* 根据bean-name,bean-type混合查找
* 根据Java annotation查找：单个bean，集合bean

**代码实现：**

`User.java`

```java
public class User {
  private String id;
  private String name;
  private int age;
  private String address;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return "User{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", age=" + age +
        ", address='" + address + '\'' +
        '}';
  }
}
```

`@Super`

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Super {}
```

`SuperUser.java`

```java
@Super
public class SuperUser extends User {

  private String idCard;

  public String getIdCard() {
    return idCard;
  }

  public void setIdCard(String idCard) {
    this.idCard = idCard;
  }

  @Override
  public String toString() {
    return "SuperUser{"
        + "id='"
        + getId()
        + '\''
        + ", name='"
        + getName()
        + '\''
        + ", age="
        + getAge()
        + ", address='"
        + getAddress()
        + '\''
        + ", idCard='"
        + getIdCard()
        + '}';
  }
}
```

`dependency-lookup.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans
  xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

  <bean id="user" class="com.shawn.study.spring.ioc.domain.User">
    <property name="id" value="1"/>
    <property name="name" value="shawn"/>
    <property name="age" value="25"/>
    <property name="address" value="shanghai"/>
  </bean>

  <!--  primary="true"  用于解决org.springframework.beans.factory.NoUniqueBeanDefinitionException expected single matching bean but found 2-->
  <bean id="superUser" class="com.shawn.study.spring.ioc.domain.SuperUser" parent="user"
    primary="true">
    <property name="idCard" value="1234567890"/>
  </bean>

  <bean id="objectFactory"
    class="org.springframework.beans.factory.config.ObjectFactoryCreatingFactoryBean">
    <property name="targetBeanName" value="user"/>
  </bean>

</beans>
```

`DependencyLookupDemo.java`

```java
public class DependencyLookupDemo {
  public static void main(String[] args) {
    BeanFactory beanFactory = new ClassPathXmlApplicationContext("classpath:/dependency-lookup.xml");
    lookupInRealTime(beanFactory);
    lookupInLazy(beanFactory);
    lookupByType(beanFactory);
    lookupByCollectionType(beanFactory);
    lookupByAnnotationType(beanFactory);
  }
  // 根据java注解查找
  private static void lookupByAnnotationType(BeanFactory beanFactory) {
    if (beanFactory instanceof ListableBeanFactory) {
      ListableBeanFactory listableBeanFactory = (ListableBeanFactory) beanFactory;
      Map<String, Object> map = listableBeanFactory.getBeansWithAnnotation(Super.class);
      map.forEach(
          (k, v) ->
              System.out.println(
                  String.format("查找标注@Super注解的所有user对象集合： %s", ((User) v).toString())));
    }
  }

  // 按照复合类型查找
  private static void lookupByCollectionType(BeanFactory beanFactory) {
    if (beanFactory instanceof ListableBeanFactory) {
      ListableBeanFactory listableBeanFactory = (ListableBeanFactory) beanFactory;
      Map<String, User> userMap = listableBeanFactory.getBeansOfType(User.class);
      userMap.forEach((k, v) -> System.out.println(String.format("集合类型查找： %s", v.toString())));
    }
  }

  // 按照单一类型查找
  private static void lookupByType(BeanFactory beanFactory) {
    /*
     如果按照单一类型查找，发现有多个同一类型的bean，会报如下的错误，此时解决方案可以给这个bean标记为Primary
     org.springframework.beans.factory.NoUniqueBeanDefinitionException: No qualifying bean of type 'com.shawn.study.spring.ioc.domain.User' available: expected single matching bean but found 2: user,superUser
    */
    User user = beanFactory.getBean(User.class);
    System.out.println(String.format("单一类型查找： %s", user.toString()));
  }

  // 实时依赖查找
  private static void lookupInRealTime(BeanFactory beanFactory) {
    User user = (User) beanFactory.getBean("user");
    System.out.println(String.format("实时查找： %s", user.toString()));
  }

  // 延迟依赖查找
  private static void lookupInLazy(BeanFactory beanFactory) {
    ObjectFactory<User> objectFactory = (ObjectFactory<User>) beanFactory.getBean("objectFactory");
    User user = objectFactory.getObject();
    System.out.println(String.format("延迟查找： %s", user.toString()));
  }
}
```

### 依赖注入

* 根据Bean名称注入
* 根据Bean类型注入
  * 单个Bean对象
  * 集合Bean对象
* 注入容器内建Bean对象
* 注入非Bean对象
* 注入类型
  * 实时注入
  * 延迟注入

**代码实现：**

`UserDao.java`

```java
public class UserDao {

  private List<User> users;

  private BeanFactory beanFactory;

  private ObjectFactory<User> userObjectFactory;

  private ObjectFactory<ApplicationContext> objectFactory;

  private Environment environment;

  public List<User> getUsers() {
    return users;
  }

  public void setUsers(List<User> users) {
    this.users = users;
  }

  public BeanFactory getBeanFactory() {
    return beanFactory;
  }

  public void setBeanFactory(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }

  public ObjectFactory<User> getUserObjectFactory() {
    return userObjectFactory;
  }

  public void setUserObjectFactory(
      ObjectFactory<User> userObjectFactory) {
    this.userObjectFactory = userObjectFactory;
  }

  public ObjectFactory<ApplicationContext> getObjectFactory() {
    return objectFactory;
  }

  public void setObjectFactory(
      ObjectFactory<ApplicationContext> objectFactory) {
    this.objectFactory = objectFactory;
  }

  public Environment getEnvironment() {
    return environment;
  }

  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }
}
```

`dependency-injection.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans
  xmlns="http://www.springframework.org/schema/beans"
  xmlns:util="http://www.springframework.org/schema/util"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/util
        https://www.springframework.org/schema/util/spring-util.xsd">

  <import resource="dependency-lookup.xml"/>

  <bean id="userDao1" class="com.shawn.study.spring.ioc.dao.UserDao">
    <!-- 硬编码，手动配置-->
    <property name="users">
      <util:list>
        <ref bean="user"></ref>
        <ref bean="superUser"></ref>
      </util:list>
    </property>
  </bean>

  <bean id="userDao2" class="com.shawn.study.spring.ioc.dao.UserDao" autowire="byType"/>

</beans>
```

`DependencyInjectionDemo.java`

```java
public class DependencyInjectionDemo {
  public static void main(String[] args) {
    BeanFactory beanFactory =
        new ClassPathXmlApplicationContext("classpath:/dependency-injection.xml");
    injectionWithCode(beanFactory);
    injectionWithAutoWiring(beanFactory);
  }

  // 硬编码方式，手动配置的方式注入
  private static void injectionWithCode(BeanFactory beanFactory) {
    UserDao userDao = (UserDao) beanFactory.getBean("userDao1");
    System.out.println(String.format("手动配置方式注入：%s", userDao.getUsers()));
  }

  // 自动装配注入
  private static void injectionWithAutoWiring(BeanFactory beanFactory) {
    UserDao userDao = (UserDao) beanFactory.getBean("userDao2");
    System.out.println(String.format("自动装配方式注入：%s", userDao.getUsers()));
  }
}
```

### 依赖来源

* 自定义bean
* 容器内置bean对象
* 容器内置依赖

**代码示例：**

`DependencySourceDemo.java`

```java
public class DependencySourceDemo {

  public static void main(String[] args) {
    BeanFactory beanFactory =
        new ClassPathXmlApplicationContext("classpath:/dependency-injection.xml");
    injectionWithBeanInSpring(beanFactory);
  }

  // 注入spring内置的bean对象
  private static void injectionWithBeanInSpring(BeanFactory beanFactory) {
    // 手动注入的bean 依赖来源一：自定义bean
    UserDao userDao1 = (UserDao) beanFactory.getBean("userDao1");
    // 自动注入的bean 依赖来源一：自定义bean
    UserDao userDao2 = (UserDao) beanFactory.getBean("userDao2");

    System.out.println(userDao1.getBeanFactory()); // null
    // 依赖来源二： 依赖注入（內建依赖）
    System.out.println(
        userDao2
            .getBeanFactory()); // org.springframework.beans.factory.support.DefaultListableBeanFactory@51e5fc98:
    // defining beans [user,superUser,objectFactory,userDao1,userDao2]; root of factory hierarchy
    System.out.println(
        beanFactory); // org.springframework.context.support.ClassPathXmlApplicationContext@1e643faf,
    // started on Sun Oct 18 01:30:43 CST 2020
    System.out.println(userDao1.getBeanFactory() == beanFactory); // false
    System.out.println(userDao2.getBeanFactory() == beanFactory); // false
    // org.springframework.beans.factory.NoSuchBeanDefinitionException: No
    // qualifying bean of type 'org.springframework.beans.factory.BeanFactory'
    // available
    //    System.out.println(
    //        beanFactory.getBean(
    //            BeanFactory
    //                .class));

    ObjectFactory<User> userObjectFactory = userDao1.getUserObjectFactory();
    System.out.println(userObjectFactory); // null

    // 依赖来源二： 依赖注入（內建依赖）
    ObjectFactory<User> userObjectFactory2 = userDao2.getUserObjectFactory();
    User user = userObjectFactory2.getObject();
    System.out.println(
        user); // SuperUser{id='1', name='shawn', age=25, address='shanghai', idCard='1234567890}

    ObjectFactory<ApplicationContext> objectFactory = userDao1.getObjectFactory();
    System.out.println(objectFactory); // null

    ObjectFactory<ApplicationContext> objectFactory2 = userDao2.getObjectFactory();
    ApplicationContext context2 = objectFactory2.getObject();
    System.out.println(context2 == beanFactory); // true

    // 依赖来源三：内建bean
    Environment environment = beanFactory.getBean(Environment.class);
    System.out.println("beanFactory 获取 Environment 类型的 Bean：" + environment);

    Environment userDao1Environment = userDao1.getEnvironment();
    System.out.println("userDao1    获取 Environment 类型的 Bean：" + userDao1Environment); // null

    Environment userDao2Environment = userDao2.getEnvironment();
    System.out.println("userDao2    获取 Environment 类型的 Bean：" + userDao2Environment);
  }
}
```

注意：

1. 在`AutoWiring`模式下，内建的依赖才会被依赖进来。
2. 不能进行对内建依赖的依赖查找，尤其可以得出依赖注入和依赖查找的依赖来源是不一样的（在依赖来源的章节会深入的讨论）。

### 构造器注入和setter方法注入

在spring3.x以及之前，推荐采用setter方法注入。

> The Spring team generally advocates setter injection, because large numbers of constructor arguments can get unwieldy, especially when properties are optional. Setter methods also make objects of that class amenable to reconfiguration or re-injection later. Management through [JMX MBeans](http://docs.spring.io/spring/docs/3.1.x/spring-framework-reference/html/jmx.html) is a compelling use case.
>
> Some purists favor constructor-based injection. Supplying all object dependencies means that the object is always returned to client (calling) code in a totally initialized state. The disadvantage is that the object becomes less amenable to reconfiguration and re-injection.

spring3.x以后，推荐采用构造器方式注入

>The Spring team generally advocates constructor injection, as it lets you implement application components as immutable objects and ensures that required dependencies are not `null`. Furthermore, constructor-injected components are always returned to the client (calling) code in a fully initialized state. As a side note, a large number of constructor arguments is a bad code smell, implying that the class likely has too many responsibilities and should be refactored to better address proper separation of concerns.

那综合比较一下

1. setter方式注入灵活，构造器注入则显得比较笨拙，在有些属性是可选的情况下，如果通过构造器的方式注入，也需要提供一个null值。
2. 构造器的参数如果很多，也会导致可读性变差。
3. 那如果是必选的属性，通过构造器注入是个比较好的方式，因为保证了依赖项不会为null，可以变为final对象。那如果是使用setter方法注入，则需要添加@Required注解，表明该依赖项是必须的，也必须在使用该依赖的时候，判断一下是否为空。
4. 构造器注入可以更好的封装类变量,不需要未每个属性指定setter方法,避免外部错误的调用。
5. 如果是第三方的类库，未公开任何setter方法，那构造器注入就是一个很好的选择。

### BeanFactory vs ApplicationContext

*  BeanFactory接口提供了一种高级配置机制，能够管理任何类型的对象。 ApplicationContext是BeanFactory的子接口。 
* ApplicationContext新增一些企业级的特性：
  * Easier integration with Spring’s AOP features
  * Message resource handling (for use in internationalization)
  * Event publication
  * Application-layer specific contexts such as the `WebApplicationContext` for use in web applications.
  * Configuration MetaData
  * Resources
  * Annotations
  * Environment Abstration

简而言之，BeanFactory提供了配置框架和基本功能，而ApplicationContext添加了更多企业特定的功能。 ApplicationContext是BeanFactory的完整超集。

### Spring Ioc容器的生命周期

* 启动
  * 启动的入口是由容器实现中的`refresh()`方法调用完成的。
  * 对`bean`定义载入ioc容器使用的方法是`loadBeanDefinition`。
* 运行
  * 依赖注入
  * 依赖查找
* 停止
  * 

启动对应的源码：

`org.springframework.context.support.AbstractApplicationContext#refresh`

```java
public void refresh() throws BeansException, IllegalStateException {
 synchronized (this.startupShutdownMonitor) {
  // Prepare this context for refreshing.
  prepareRefresh();

  // Tell the subclass to refresh the internal bean factory.
  ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

  // Prepare the bean factory for use in this context.
  prepareBeanFactory(beanFactory);

  try {
   // Allows post-processing of the bean factory in context subclasses.
   postProcessBeanFactory(beanFactory);

   // Invoke factory processors registered as beans in the context.
   invokeBeanFactoryPostProcessors(beanFactory);

   // Register bean processors that intercept bean creation.
   registerBeanPostProcessors(beanFactory);

   // Initialize message source for this context.
   initMessageSource();

   // Initialize event multicaster for this context.
   initApplicationEventMulticaster();

   // Initialize other special beans in specific context subclasses.
   onRefresh();

   // Check for listener beans and register them.
   registerListeners();

   // Instantiate all remaining (non-lazy-init) singletons.
   finishBeanFactoryInitialization(beanFactory);

   // Last step: publish corresponding event.
   finishRefresh();
  }

  catch (BeansException ex) {
   if (logger.isWarnEnabled()) {
    logger.warn("Exception encountered during context initialization - " +
      "cancelling refresh attempt: " + ex);
   }

   // Destroy already created singletons to avoid dangling resources.
   destroyBeans();

   // Reset 'active' flag.
   cancelRefresh(ex);

   // Propagate exception to caller.
   throw ex;
  }

  finally {
   // Reset common introspection caches in Spring's core, since we
   // might not ever need metadata for singleton beans anymore...
   resetCommonCaches();
  }
 }
}
```

补充一些说明：

`ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();`，执行此方法，里面调用了`refreshBeanFactory()`，首先创建了`DefaultListableBeanFactory beanFactory = createBeanFactory();`其次执行了`loadBeanDefinitions(beanFactory);`加载了bean定义。

在执行`prepareBeanFactory(beanFactory);`的时候，事实上就是在添加一些`Spring`内建的依赖。

`finishRefresh();`执行这个时候，已经进入bean的生命周期的管理

`onRefresh();`为初始化容器提供了扩展方法。

其余比如加载`MessageSourece国际化(i18n),event,listener`等，在上述代码里都有体现，具体内容可以以后再分享。

停止的相关源代码：

`org.springframework.context.support.AbstractApplicationContext#registerShutdownHook`

`org.springframework.context.support.AbstractApplicationContext#doClose`

```java
@Override
public void registerShutdownHook() {
 if (this.shutdownHook == null) {
  // No shutdown hook registered yet.
  this.shutdownHook = new Thread(SHUTDOWN_HOOK_THREAD_NAME) {
   @Override
   public void run() {
    synchronized (startupShutdownMonitor) {
     doClose();
    }
   }
  };
  Runtime.getRuntime().addShutdownHook(this.shutdownHook);
 }
}
```

```java
protected void doClose() {
 // Check whether an actual close attempt is necessary...
 if (this.active.get() && this.closed.compareAndSet(false, true)) {
  if (logger.isDebugEnabled()) {
   logger.debug("Closing " + this);
  }

  LiveBeansView.unregisterApplicationContext(this);

  try {
   // Publish shutdown event.
   publishEvent(new ContextClosedEvent(this));
  }
  catch (Throwable ex) {
   logger.warn("Exception thrown from ApplicationListener handling ContextClosedEvent", ex);
  }

  // Stop all Lifecycle beans, to avoid delays during individual destruction.
  if (this.lifecycleProcessor != null) {
   try {
    this.lifecycleProcessor.onClose();
   }
   catch (Throwable ex) {
    logger.warn("Exception thrown from LifecycleProcessor on context close", ex);
   }
  }

  // Destroy all cached singletons in the context's BeanFactory.
  destroyBeans();

  // Close the state of this context itself.
  closeBeanFactory();

  // Let subclasses do some final clean-up if they wish...
  onClose();

  // Reset local application listeners to pre-refresh state.
  if (this.earlyApplicationListeners != null) {
   this.applicationListeners.clear();
   this.applicationListeners.addAll(this.earlyApplicationListeners);
  }

  // Switch to inactive.
  this.active.set(false);
 }
}
```

补充一些说明：

1. `this.lifecycleProcessor.onClose();`首先结束对bean的生命周期的管理
2. `destroyBeans();`销毁所有的bean
3. `closeBeanFactory();`关闭`ioc`容器
4. `onClose();`提供停止`ioc`容器的扩展点

### Spring ioc的类图关系

`BeanFactory `作为最顶层的一个接口类，它定义了` IOC `容器的基本功能规范，`BeanFactory` 有三个重要的子类：`ListableBeanFactory、HierarchicalBeanFactory 和 AutowireCapableBeanFactory`。但是从类图中我们可以发现最终的默认实现类是 `DefaultListableBeanFactory`，它实现了所有的接口。那为何要定义这么多层次的接口呢？查阅这些接口的源码和说明发现，每个接口都有它使用的场合，它主要是为了区分在 `Spring` 内部在操作过程中对象的传递和转化过程时，对对象的数据访问所做的限制。例如 `ListableBeanFactory `接口表示这些 `Bean` 是可列表化的，而 `HierarchicalBeanFactory `表示的是这些 `Bean` 是有继承关系的，也就是每个 `Bean` 有可能有父 `Bean`。`AutowireCapableBeanFactory `接口定义 `Bean` 的自动装配规则。这三个接口共同定义了` Bean` 的集合、`Bean` 之间的关系、以及 `Bean` 行为。最基本的` IOC` 容器接口` BeanFactory`

![Spring-ioc-BeanFactory类图关系](.\images\Spring-ioc-BeanFactory类图关系.png)

IOC 容器的初始化包括 `BeanDefinition` 的` Resource` 定位、加载和注册这三个基本的过程。我们以`ApplicationContext` 为例讲解，`ApplicationContext` 系列容器也许是我们最熟悉的，因为 Web 项目中使用的`XmlWebApplicationContext`就属于这个继承体系，还有`ClasspathXmlApplicationContext，AnnotationConfigApplicationContext`等

![Spring-ioc-BeanFactory类图关系](.\images\Spring-ioc-ApplicationContext类图关系.png)

参考此文： [Spring-2019-08-渐入Spring-IOC]([http://www.mzc.wiki/2019/08/21/Spring-2019-08-%E6%B8%90%E5%85%A5Spring-IOC/](http://www.mzc.wiki/2019/08/21/Spring-2019-08-渐入Spring-IOC/))

