## Spring Bean的生命周期

### Spring Bean元信息配置阶段

* XML配置
* Properties资源配置
* Annotation
* API

### Spring Bean元信息解析阶段

* BeanDefinitionReader
* XML解析器-BeanDefinitionParser
* AnnotateBeanDefinitionReader

```java
/**
 * Spring Bean元信息配置阶段/解析阶段示例
 */
public class BeanMetaConfigurationDemo {

  public static void main(String[] args) {
    metaConfigurationWithProperties();
    metaConfigurationWithXML();
    metaConfigurationWithAnnotation();
    metaConfigurationWithApi();
  }

  /**
   * 面向资源配置-properties配置
   */
  private static void metaConfigurationWithProperties(){
    DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
    PropertiesBeanDefinitionReader reader = new PropertiesBeanDefinitionReader(beanFactory);
    Resource resource = new ClassPathResource("person.properties");
    EncodedResource encodedResource = new EncodedResource(resource, "UTF-8");
    int count = reader.loadBeanDefinitions(encodedResource);
    System.out.println("已加载 BeanDefinition 数量：" + count);

    Person person = beanFactory.getBean(Person.class);
    System.out.println(person);
  }

  /**
   * 面向资源配置-xml配置
   */
  private static void metaConfigurationWithXML(){
    DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
    XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
    int count = reader.loadBeanDefinitions("classpath:/person.xml");
    System.out.println("已加载 BeanDefinition 数量：" + count);

    Person person = beanFactory.getBean(Person.class);
    System.out.println(person);
  }

  /**
   * 面向注解配置
   */
  private static void metaConfigurationWithAnnotation(){
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
    applicationContext.register(BeanMetaConfigurationDemo.class);
    applicationContext.refresh();
    String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
    Arrays.stream(beanDefinitionNames).forEach(System.out::println);
    applicationContext.close();
  }

  /**
   * 面向API配置
   */
  private static void metaConfigurationWithApi(){
    DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
    AnnotatedBeanDefinitionReader annotatedBeanDefinitionReader = new AnnotatedBeanDefinitionReader(beanFactory);
    int beanDefinitionCountBefore = beanFactory.getBeanDefinitionCount();
    annotatedBeanDefinitionReader.register(BeanMetaConfigurationDemo.class);
    int beanDefinitionCountAfter = beanFactory.getBeanDefinitionCount();
    int beanDefinitionCount = beanDefinitionCountAfter - beanDefinitionCountBefore;
    System.out.println("已加载 BeanDefinition 数量：" + beanDefinitionCount);
    BeanMetaConfigurationDemo demo = beanFactory.getBean("beanMetaConfigurationDemo",
        BeanMetaConfigurationDemo.class);
    System.out.println(demo);
  }

  @Bean
  public Person person(){
    Person p = new Person();
    p.setId(3);
    p.setName("john");
    return p;
  }
}
```

Bean 信息被读取之后，为后续 Bean 注册提供元信息。

需要注意的是：

> 1. `PropertiesBeanDefinitionReader`和`XmlBeanDefinitionReader`都是继承`AbstractBeanDefinitionReader`，但是`AnnotatedBeanDefinitionReader`并不是继承`AbstractBeanDefinitionReader`，就是一个独立的类。
> 2. `PropertiesBeanDefinitionReader`和`XmlBeanDefinitionReader`属于资源文件配置类，`AnnotatedBeanDefinitionReader`属于注解配置，主要调用的类及方法是`AnnotationConfigApplicationContext#register`。

### Spring Bean注册阶段

`BeanDefinitionRegistry`

`org.springframework.beans.factory.support.DefaultListableBeanFactory#registerBeanDefinition`

> Q: 为什么有了Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256)；还需要List<String> beanDefinitionNames = new ArrayList<>(256)；
>
> A：ConcurrentHashMap中的key是无序的，为了保证顺序性，所以需要ArrayList来存储key。
>
> Q: 为什么需要使用synchronized对beanDefinitionMap加锁？
>
> A：注册的过程中需要线程同步，以保证数据的一致性，Cannot modify startup-time collection elements anymore (for stable iteration)。实际上就是不能并发地修改beanDefinitionNames集合元素，在并发情况下，遍历集合元素，对其修改是不允许的，需要保住数据的一致性，便于稳定遍历。需要通过copy on write的方式实现。
>
> Q: 具体的register的逻辑？
>
> A：
>
> 1. 首先完成校验。`((AbstractBeanDefinition) beanDefinition).validate();`
>
> 	2. 接下来从beanDefinitionMap中获取bean，首次获取肯定是空。`BeanDefinition existingDefinition = this.beanDefinitionMap.get(beanName);`
>  	3. 如果获取到的beanDefinition不为空，表示bean已经注册，则会判断是否可以覆盖oldBean，也需要判断bean对应的Role级别。
>  	4. 如果为空，则将新的bean注册到beanDefinitionMap中去。

### Spring BeanDefinition合并阶段

父子BeanDefinition合并

* 当前BeanFactory查找
* 层次性BeanFactory查找

入口：`AbstractBeanFactory#getMergedLocalBeanDefinition`

**show me the code:**

```java
public class MergedBeanDefinitionDemo {

  public static void main(String[] args) throws Exception{
    DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
    // 基于 XML 资源 BeanDefinition 实现
    XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
    String location = "merged-bean-definition.xml";
    // 基于 ClassPath 加载 XML 资源
    Resource resource = new ClassPathResource(location);
    // 指定字符集编码 UTF-8
    EncodedResource encodedResource = new EncodedResource(resource, "UTF-8");
    int beanNumbers = beanDefinitionReader.loadBeanDefinitions(encodedResource);
    System.out.println("已加载 BeanDefinition 数量：" + beanNumbers);
    // 通过 Bean Id 和类型进行依赖查找, 
    Person user = beanFactory.getBean("person", Person.class);
    System.out.println(user);

    SuperPerson superUser = beanFactory.getBean("superPerson", SuperPerson.class);
    System.out.println(superUser);

    InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(location);
    int i;
    while ((i = resourceAsStream.read()) != -1){
      System.out.print(i);
    }
  }

}
```

```java
protected RootBeanDefinition getMergedLocalBeanDefinition(String beanName) throws BeansException {
 // Quick check on the concurrent map first, with minimal locking.
 RootBeanDefinition mbd = this.mergedBeanDefinitions.get(beanName);
 if (mbd != null && !mbd.stale) {
  return mbd;
 }
 return getMergedBeanDefinition(beanName, getBeanDefinition(beanName));
}
```

一直深入代码发现：

> 1. 当demo code执行到Person user = beanFactory.getBean("person", Person.class)的时候，merge bean走的是
>
>    ```java
>    if (bd.getParentName() == null) {
>     // Use copy of given root bean definition.
>     if (bd instanceof RootBeanDefinition) {
>      mbd = ((RootBeanDefinition) bd).cloneBeanDefinition();
>     }
>     else {
>      mbd = new RootBeanDefinition(bd);
>     }
>    }
>    ```
>
>    这个分支，因为我们的xml里的配置是没有指定parent。所以在执行bd.getParentName()的时候，获取到的就是null。
>
>    另外beanDefinition的实现类是GenericBeanDefinition。所以执行的分支是mbd = new RootBeanDefinition(bd)。
>
>    因为本身这个beanDefinition就是RootBeanDefinition，最上层的BeanDefition。
>
> 2. 当demo code执行到SuperPerson superUser = beanFactory.getBean("superPerson", SuperPerson.class)的时候，走的就是另外的分支。
>
> 3. 父子 BeanDefinition 合并 - 当前 BeanFactory 查找 - 层次性 BeanFactory 查找。经过合并后 无parent 的 GenericBeanDefinition 变成 RootBeanDefinition。SuperPerson 经过合并后 GenericBeanDefinition 变成 RootBeanDefinition ，并且覆盖 parent 相关配置。合并取决于首先是不是 root 的，再有是不是存在 parent，合并最终是通过复制将 GenericBeanDefinition 变成 RootBeanDefinition，同时子覆盖父的相关配置。

### Spring Bean Class加载阶段

* ClassLoader类加载
* java Security安全控制
* ConfigurableBeanFactory临时ClassLoader

入口`org.springframework.beans.factory.support.AbstractBeanFactory#resolveBeanClass`

>Java 通过 Class 表达，Class 通过 ClassLoader 加载。
>
>Spring 通过 BeanDefinition，实际是通过文本的方式来呈现，并没有显式的告知类是什么。
>
>实际上在 Spring IoC 容器的实中 ClassLoader 加载是个边缘的操作，通常不太重视。
>
>Spring Bean Class 加载 还是运用了 传统的 Java ClassLoader, 只是在 Class Load 过程中涉及到一些 Java 安全的细节操作，事实上这个操作 Java 本身已经具备，而是我们通常没把它激活的情况下面选择性忽略。
>
>Thread.currentThread().getContextClassLoader() 和 MergedBeanDefinitionDemo.class.getClassLoader() 得到classLoader有什么区别？‘
>
>通常是一样的，不过 ClassLoader 在 Thread.currentThread() 能被重置，而后者则比较固定。
>
>beanClass的值首次情况什么时候是字符串，什么时候是Class对象呢？
>
>如果是配置文件的话，通常是字符串形式。如果是编程组装的话，两者兼有，不过用 Class 对象具备类型安全，字符串容易拼写出错。

### Spring Bean实例化前阶段

非主流生命周期-Bean实例化前阶段

* org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor#postProcessBeforeInstantiation

实际工作中很少去涉猎。

这个阶段会打破既有对Spring 的Bean 实例化的一个认知。

可以通过创建一个代理类的方式来创建一个实例，替换掉传统的实例化方法。

在bean实例化前回调,返回实例则不对bean实例化,返回null则进行spring bean实例化(doCreateBean);

### Spring Bean实例化阶段

org.springframework.beans.factory.support.InstantiationStrategy

构造器依赖注入

### Spring Bean实例化后阶段

org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor#postProcessAfterInstantiation

可以将该阶段理解为它是一个Bean的赋值的判断 或者说是赋值的前置操作。需要进行后续赋值返回 true，不需要则返回false。

```java

```



### Spring Bean属性赋值前阶段

* Bean属性值元信息
  * PropertyValues
* Bean属性赋值前回调
  * Spring1.2-5.0: org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor#postProcessPropertyValues
  * Spring5.1: org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor#postProcessProperties

在属性赋值前的回调在 applyPropertyValues 之前操作可以对属性添加或修改等操作最后在通过applyPropertyValues应用bean对应的wapper对象（BeanWapper）

### Spring Bean Aware接口回调阶段

Spring Aware接口

- BeanNameAware                   **属于BeanFactory Aware 接口回调**
- BeanClassLoaderAware              **属于BeanFactory Aware 接口回调**
- BeanFactoryAware                  **属于BeanFactory Aware 接口回调**
- EnvironmentAware                 **属于ApplicationContext 接口回调**
- EmbeddedValueResolverAware        **属于ApplicationContext 接口回调**
- ResourceLoaderAware               **属于ApplicationContext 接口回调**
- ApplicationEventPublisherAware       **属于ApplicationContext 接口回调**
- MessageSourceAware              **属于ApplicationContext 接口回调**
- ApplicationContextAware             **属于ApplicationContext 接口回调**

>Aware 是一个回调，用于进行接口注入。
>
>invokeAwareInterface
>
>普通的 BeanFactory ,Aware 回调只有3个，BeanNameAware, BeanClassLoaderAware, BeanFactoryAware。
>
>若是 ApplicationContext ，Aware 会更多一些，由于是内置类的关系，在 ApplicationContext 初始化时，会动态的往 BeanFactory 里面添加一个 PostProcess。
>
>可以通过ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory()后给beanFactory.addBeanPostProcessor()添加自定义的InstantiationAwareBeanPostProcessor()处理器;因为在创建ClassPathXmlApplicationContext()对象时是默认调用了ApplicationContext.refresh()操作此时已经将beanFactory初始化;不过我们后面还要进行refresh()一次让beanPostProcessor加载到beanFactory中生效

### Spring Bean初始化前阶段

- 此时已完成
  - Bean实例化
  - Bean属性赋值
  - Bean Aware 回调
- 方法回调
  - BeanPostProcessor#postProcessBeforeInitialization

>方法允许返回空，也允许返回其他的代理对象，用于返回之前做一些相应的定制。
>
>postConstructor 回调 事实上是在注解驱动里面的，当时这个版本 没有这方面的支持。因此api中只写了Spring 内部自己支持的。

### Spring Bean初始化阶段

Bean 初始化 (Initialization)

- @PostConstruct 标注方法
- 实现 InitializingBean 接口的 afterPropertiesSet() 方法
- 自定义初始化方法

> 在bean初始化前阶段initializeBean()对应的applyBeanPostProcessorsBeforeInitialization()方法中执行完自定义的MyInstantiationAwareBeanPostProcessor的postProcessBeforeInitialization()方法同时会执行CommonAnnotationBeanPostProcessor的postProcessBeforeInitialization方法最后执行@PostConstruct修饰的initPostConstructor方法 2.其他两种bean初始化在invokeInitMethods()中被调用

### Spring Bean初始化后阶段

方法回调

- BeanPostProcessor#postProcessAfterInitialization

### Spring Bean初始化完成阶段

Spring 4.1+： SmartInitializingSingleton#afterSingletonsInstantiated

### Spring Bean销毁前阶段

方法回调

- DestructionAwareBeanPostProcessor#postProcessBeforeDestruction

### Spring Bean销毁阶段

 Bean 销毁 （Destory）

- @PreDestory 标注方法
- 实现DisposableBean 接口的 destroy() 方法
- 自定义销毁方法

> 注意：
>
> 只是说在 Bean 容器里面销毁，不是在整个Java程序中销毁

### Spring Bean垃圾回收阶段

Bean 垃圾回收 (GC)

- 关闭 Spring 容器 (应用上下文)
- 执行GC
- Spring Bean 覆盖的 finalize() 方法被回调

BeanPostProcessor 的使用场景有哪些？

`BeanPostProcessor` 提供 Spring Bean 初始化前和初始化后生命周期回调，分别对应 `postProcessBeforeInitialization` 以及 `postProcessAfterInitialization` 方法， 允许对关心的 Bean 进行扩展，甚至是替换。

其中， `ApplicationContext` 相关的 Aware 回调也是基于 `BeanPostProcessor` 实现，即 `ApplicationContextAwareProcesor` 。

BeanFactoryPostProcessor 与 BeanPostProcessor 的区别？

BeanFactoryPostProcessor 是 Spring BeanFactory（实际为 ConfigurableListableBeanFactory ） 的后置处理器，用于扩展 BeanFactory，或通过 BeanFactory 进行依赖查找和依赖注入。

BeanFactoryPostProcess 必须有 Spring ApplicationContext 执行，BeanFactory 无法与其直接交互。

而 BeanPostProcessor 则直接与 BeanFactory 关联，属于 N 对一的关系。

`addBeanPostProcessor` 仅有这一种方式添加。

springboot的自动装配是通过 spring.factories 实现的org.springframework.boot.autoconfigure.EnableAutoConfiguration，实现自动装配的，BeanFactoryPostProcessor，在低版本的spring实现自动装配，可以实现BeanFactoryPostProcessor，获取beanFactory, 也就是 BeanDefinitionRegistry 去注册自己的BeanDefination,来实现自动装配。

BeanFactory 是怎样处理 Bean 生命周期？

BeanFactory 的默认实现为 DefaultListableBeanFactory，其中 Bean 生命周期与方法映射如下：

- BeanDefinition 注册阶段 —— `registerBeanDefinition`
- BeanDefintion 合并阶段 —— `getMergedBeanDefinition`，最后都变为 root BeanDefnition，简而言之就是把一个普通的 BeanDefinition 变为无parent的根部的BeanDefinition 。
- Bean 实例化前阶段 —— `resolveBeforInstantiation`
- Bean 实例化阶段 —— `createBeanInstance`
- Bean 初始化后阶段 —— `populateBean`
- Bean 属性赋值前阶段 —— `populateBean`
- Bean 属性赋值阶段 —— `populateBean`
- Bean Aware 接口回调阶段 —— `initializeBean`
- Bean 初始化阶前段 —— `initializeBean`
- Bean 初始化阶段 —— `initializeBean`
- Bean 初始化后阶段 —— `initializeBean`
- Bean 初始化完成阶段 —— `preInitializeBean`
- Bean 销毁前阶段 —— `destroyBean`
- Bean 销毁阶段 —— `destroyBean`

配置和解析阶段在和 BeanFactory 交互之前就已经解决了。