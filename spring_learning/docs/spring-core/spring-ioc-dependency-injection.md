## 依赖注入

### 依赖注入的模式

* 非自动模式：通过XML配置或者Annotation，或者API的方式，实现依赖注入。
* 自动模式：可以通过XML配置里的auto-wiring这个TAG实现，或者@Autowired

### 依赖注入的类型

* `Setter Method:`

  手动模式：

  * xml配置：

    1. `dependency-injection.xml`

       ```xml
       <bean id="personService"
         class="com.shawn.study.spring.ioc.dependency_injection.service.PersonService">
         <property name="repository" ref="personRepository"/>
       </bean>
       
       <bean id="personRepository"
         class="com.shawn.study.spring.ioc.dependency_injection.repository.PersonRepository"/>
       ```

    2. `Test.java`

       ```java
       DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
       XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
       String xmlResourcePath = "classpath:/dependency-injection.xml";
       beanDefinitionReader.loadBeanDefinitions(xmlResourcePath);
       System.out.println("演示基于xml的方式进行Setter方法注入");
       PersonService personService = beanFactory.getBean(PersonService.class);
       personService.getAllPersons().forEach(System.out::println);
       System.out.printf("person Id = 2: %s\n", personService.getPersonById(2));
       ```

  * annotation: 

    ```java
      private static void annotationDependencySetterInjection(){
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(DependencySetterInjectionDemo.class);
        applicationContext.refresh();
        PersonService personService = applicationContext.getBean(PersonService.class);
        System.out.println("演示基于注解的方式进行Setter方法注入");
        personService.getAllPersons().forEach(System.out::println);
        System.out.printf("person Id = 2: %s\n", personService.getPersonById(2));
        applicationContext.close();
      }
      @Bean
      public PersonRepository personRepository(){
        return new PersonRepository();
      }
    
      @Bean
      public PersonService personService(PersonRepository repository){
        PersonService personService = new PersonService();
        personService.setRepository(repository);
        return personService;
      }
    ```

  * API:

    ```java
    private static void apiDependencySetterInjection(){
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(PersonService.class);
        builder.addPropertyValue("repository", new PersonRepository());
        applicationContext.registerBeanDefinition("personService2", builder.getBeanDefinition());
        applicationContext.refresh();
        PersonService personService = applicationContext.getBean("personService2", PersonService.class);
        System.out.println("演示基于api的方式进行Setter方法注入");
        personService.getAllPersons().forEach(System.out::println);
        System.out.printf("person Id = 2: %s\n", personService.getPersonById(2));
        applicationContext.close();
    }
    ```

  自动模式：

  * `byType`

    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <beans
      xmlns="http://www.springframework.org/schema/beans"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://www.springframework.org/schema/beans
            https://www.springframework.org/schema/beans/spring-beans.xsd">
    
      <!--
        byType模式需要注意的是：
        如果是找到多个相同类型的Bean，则会报错
        No qualifying bean of type 'com.shawn.study.spring.ioc.dependency_injection.repository.PersonRepository' available:
        expected single matching bean but found 2: personRepository,personRepository1
       -->
      <bean id="personService-setter-byType"
        class="com.shawn.study.spring.ioc.dependency_injection.service.PersonService" autowire="byType">
      </bean>
    
      <bean id="personRepository"
        class="com.shawn.study.spring.ioc.dependency_injection.repository.PersonRepository"/>
    
      <!--  <bean id="personRepository1"-->
      <!--    class="com.shawn.study.spring.ioc.dependency_injection.repository.PersonRepository"/>-->
    
    </beans>
    ```

    ```java
    private static void autowiredDependencySetterInjectionByType() {
      DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
      String location = "classpath:/autowired-dependency-setter-injection-byType.xml";
      XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
      reader.loadBeanDefinitions(location);
      PersonService personService = beanFactory
          .getBean("personService-setter-byType", PersonService.class);
      print("自动模式-演示byType-依赖注入", personService, 2);
    }
    ```

  * `byName`

    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <beans
      xmlns="http://www.springframework.org/schema/beans"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://www.springframework.org/schema/beans
            https://www.springframework.org/schema/beans/spring-beans.xsd">
    
      <!--
          byName模式，如果beanName与setter方法的名称不一致则会报错，
          例如此例中，setRepository(PersonRepository repository)，
          <bean id="repository"
          class="com.shawn.study.spring.ioc.dependency_injection.repository.PersonRepository"/>
          中的id值一定是repository，才会生效。
    
          那到底是看setXxx还是看方法参数名呢？
          答案是看setXxx，id的名字就是xxx。
          可以把方法改成这样setRepository(PersonRepository repo)测试一下。答案就显而易见了
      -->
      <bean id="personService-setter-byName"
        class="com.shawn.study.spring.ioc.dependency_injection.service.PersonService" autowire="byName">
      </bean>
    
      <bean id="repository"
        class="com.shawn.study.spring.ioc.dependency_injection.repository.PersonRepository"/>
    
    </beans>
    ```

    ```java
    private static void autowiredDependencySetterInjectionByName() {
      DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
      String location = "classpath:/autowired-dependency-setter-injection-byName.xml";
      XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
      reader.loadBeanDefinitions(location);
      PersonService personService = beanFactory
          .getBean("personService-setter-byName", PersonService.class);
      print("自动模式-演示byName-依赖注入", personService, 3);
    }
    ```

* `constructor-arg`

  手动模式：

  * xml:

    ```xml
    <bean id="personRepository"
      class="com.shawn.study.spring.ioc.dependency_injection.repository.PersonRepository"/>
    
    <bean id="personService-constructor"
      class="com.shawn.study.spring.ioc.dependency_injection.service.PersonService">
      <constructor-arg name="repository" ref="personRepository"/>
    </bean>
    ```

    ```java
    private static void xmlDependencyConstructorInjection(){
      DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
      String xmlResourcePath = "classpath:/dependency-injection.xml";
      XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
      reader.loadBeanDefinitions(xmlResourcePath);
      PersonService personService = beanFactory.getBean("personService-constructor", PersonService.class);
      System.out.println("演示基于xml配置元信息实现的构造器依赖注入：");
      personService.getAllPersons().forEach(System.out::println);
      System.out.printf("person Id = 2: %s\n", personService.getPersonById(2));
    }
    ```

  * annotation:

    ```java
      private static void annotationDependencyConstructorInjection(){
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(DependencyConstructorInjectionDemo.class);
        applicationContext.refresh();
        PersonService personService = applicationContext.getBean(PersonService.class);
        System.out.println("演示基于annotation配置元信息实现的构造器依赖注入：");
        personService.getAllPersons().forEach(System.out::println);
        System.out.printf("person Id = 2: %s\n", personService.getPersonById(2));
        applicationContext.close();
      }
    
      @Bean
      public PersonRepository personRepository(){
        return new PersonRepository();
      }
    
      @Bean
      public PersonService personService(PersonRepository repository){
        return new PersonService(repository);
      }
    ```

  * api:

    ```java
      private static void apiDependencyConstructorInjection(){
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(PersonService.class);
        builder.addConstructorArgValue(new PersonRepository());
        applicationContext.registerBeanDefinition("personService-constructor2", builder.getBeanDefinition());
        applicationContext.refresh();
        PersonService personService = applicationContext.getBean("personService-constructor2", PersonService.class);
        System.out.println("演示基于api配置元信息实现的构造器依赖注入：");
        personService.getAllPersons().forEach(System.out::println);
        System.out.printf("person Id = 2: %s\n", personService.getPersonById(2));
        applicationContext.close();
      }
    ```

  自动模式：

  * `constructor`

    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <beans
      xmlns="http://www.springframework.org/schema/beans"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://www.springframework.org/schema/beans
            https://www.springframework.org/schema/beans/spring-beans.xsd">
    
      <bean id="personService-constructor-autowired"
        class="com.shawn.study.spring.ioc.dependency_injection.service.PersonService" autowire="constructor">
      </bean>
    
      <bean id="repository"
        class="com.shawn.study.spring.ioc.dependency_injection.repository.PersonRepository"/>
    
    </beans>
    ```

    ```java
    private static void autowiredDependencyConstructorInjection() {
      DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
      XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
      String location = "classpath:/autowired-dependency-constructor-injection.xml";
      reader.loadBeanDefinitions(location);
      PersonService personService = beanFactory
          .getBean("personService-constructor-autowired", PersonService.class);
      print("自动模式-演示constructor-构造器依赖注入", personService, 1);
    }
    ```

* `field`

  `@Autowired`

  `@Resource`

  `@Inject`

  ```java
   @Autowired
    private PersonService personService;
  
    @Resource
    private PersonService personService2;
  
    @Inject
    private PersonService personService3;
  
    public static void main(String[] args) {
      AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
      applicationContext.register(DependencyFieldInjectionDemo.class);
      applicationContext.refresh();
      DependencyFieldInjectionDemo bean = applicationContext
          .getBean(DependencyFieldInjectionDemo.class);
      System.out.println(bean.personService2 == bean.personService);
      System.out.println(bean.personService3 == bean.personService);
      applicationContext.close();
    }
  
    @Bean
    public PersonService personService() {
      return new PersonService(new PersonRepository());
    }
  ```

* `方法参数注入`

  手动模式-java注解配置元信息

  1. `@Autowired`
  2. `@Resource`
  3. `@Inject`
  4. `@Bean`

  ```java
  private PersonService autowiredPersonService;
  
  private PersonService resourcePersonService;
  
  private PersonService injectPersonService;
  
  @Bean
  public PersonRepository personRepository() {
    return new PersonRepository();
  }
  
  // 方法参数注入-@Bean的方式
  @Bean
  public PersonService personService(PersonRepository personRepository) {
    return new PersonService(personRepository);
  }
  
  @Autowired
  public void autowiredPersonService(PersonService autowiredPersonService) {
    this.autowiredPersonService = autowiredPersonService;
  }
  
  @Resource
  public void resourcePersonService(PersonService resourcePersonService) {
    this.resourcePersonService = resourcePersonService;
  }
  
  @Inject
  public void injectPersonService(PersonService injectPersonService) {
    this.injectPersonService = injectPersonService;
  }
  
  public static void main(String[] args) {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
    applicationContext.register(DependencyMethodArgsInjectionDemo.class);
    applicationContext.refresh();
  
    DependencyMethodArgsInjectionDemo dependencyMethodArgsInjectionDemo = applicationContext
        .getBean(DependencyMethodArgsInjectionDemo.class);
  
    print("方法参数注入-@Autowired方式：", dependencyMethodArgsInjectionDemo.autowiredPersonService, 2);
  
    print("方法参数注入-@Resource方式：", dependencyMethodArgsInjectionDemo.resourcePersonService, 1);
  
    print("方法参数注入-@Inject方式：", dependencyMethodArgsInjectionDemo.injectPersonService, 3);
  
    applicationContext.close();
  }
  
  private static void print(String source, PersonService personService, int id) {
    System.out.println(source);
    personService.getAllPersons().forEach(System.out::println);
    System.out.printf("person Id = %d: %s\n", id, personService.getPersonById(id));
  }
  ```

  

* `接口回调注入`

  接口回调注入只有自动模式

  | 内建接口                         | 说明                                                  |
  | -------------------------------- | ----------------------------------------------------- |
  | `BeanFactoryAware`               | 获取`ioc`容器-`BeanFactory`                           |
  | `ApplicationContextAware`        | 获取`spring`应用上下文-`ApplicationContext`           |
  | `EnvironmentAware`               | 获取`Environment`对象                                 |
  | `ResourceLoaderAware`            | 获取资源加载器对象-`ResourceLoader`                   |
  | `BeanClassLoaderAware`           | 获取加载当前`Bean Class`的`ClassLoader`               |
  | `BeanNameAware`                  | 获取当前`bean`的名称                                  |
  | `MessageSourceAware`             | 获取`MessageSource`对象，用于`Spring`国际化           |
  | `ApplicationEventPublisherAware` | 获取`ApplicationEventPublisher`对象，用于`spring`事件 |
  | `EmbeddedValueResolverAware`     | 获取`StringValueResolver`对象，用于占位符处理         |


```java
public class DependencyInterfaceCallbackInjectionDemo implements BeanFactoryAware,
    ApplicationContextAware, BeanNameAware {

  private static BeanFactory beanFactory;

  private static ApplicationContext applicationContext;

  private static String name;

  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    DependencyInterfaceCallbackInjectionDemo.beanFactory = beanFactory;
  }

  @Override
  public void setBeanName(String name) {
    DependencyInterfaceCallbackInjectionDemo.name = name;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    DependencyInterfaceCallbackInjectionDemo.applicationContext = applicationContext;
  }

  public static void main(String[] args) {

    // 创建 BeanFactory 容器
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    // 注册 Configuration Class（配置类） -> Spring Bean
    context.register(DependencyInterfaceCallbackInjectionDemo.class);

    // 启动 Spring 应用上下文
    context.refresh();

    System.out.println(beanFactory == context.getBeanFactory());
    System.out.println(applicationContext == context);
    System.out.println(
        applicationContext.getBean(DependencyInterfaceCallbackInjectionDemo.class) == context
            .getBean(DependencyInterfaceCallbackInjectionDemo.class));

    // 显示地关闭 Spring 应用上下文
    context.close();
  }
}
```

## 自动绑定

理论上只要自动绑定类型和名称能够与 Bean 对应上， 需要自动装配的对象增加字段时，有可以自动装配的bean时，也会随之自动装配

- 可以有效减少一些属性或构造器参数的一个设定
- 自动绑定可以更新配置，当对象正在被逐渐升级哦逐渐形成时

### 类型

| 模式        | 说明                                                         |
| ----------- | ------------------------------------------------------------ |
| no          | 默认值，未激活Autowiring，需要手动指定依赖注入对象           |
| byName      | 根据被注入属性的名称作为Bean 名称进行依赖查找，并将对象设置到该属性 |
| byType      | 根据被注入属性的类型作为依赖类型进行查找，并将对象设置到该属性 |
| constructor | 特殊byType 类型，用于构造器参数                              |

参考枚举：org.springframework.beans.factory.annotation.Autowire

`Autodetect` 自动探测，这种模式spring 3以前官方文档里面留下来的历史的一个配置，在新版本中基本上已经不存在了。默认值变成了`no`这种模式。

### 自动绑定（Autowiring）限制和不足

自动绑定方式使用方便，但实际使用很少，因为存在着一些不足和限制。

- 关于精确依赖：在 property 和 constructor-arg (即构造器参数和 property )上面对设置，通常会覆盖掉我们的 Autowiring 。 Autowiring 方式只是在我们不填写的时候，可以指定它是 byType还是byName或者byConstructor 这种方式来进行自动绑定。
- 不能绑定一些简单的 property ,也就是说不能绑定 简单的一个类型，包括原生类型以及 String 类型、class 类型，这也是一个设计上的限制。自动绑定通常去绑定一些 Bean 或者一些相关的引用对象。这时候我们的原生类型是没办法声明成一个 Bean 的，所以这种情况下没办法执行自动的Autowiring。但可以进行注入，可通过@Value的方式，也可以通过其他的方式转换，或者等于说把文本类型转化成长整型的Long类型，
- 缺乏精确性。Autowiring实际是一种猜测型或者半猜测性，比如byName，此时会搜索名称、字段或方法或参数相同的方式来进行一个自动的关联。但这会显示出一个问题Spring 会非常在乎、关注于精确性。如果里面出现一些模糊的情况，如名称取的不对，或者恰好取了一个非关联性的名称，此时 Autowiring 就搞错了。因此这种精确性无法保证。所以 Spring 非常在意避免去猜测这种模糊情况。
- 工具方面，wiring信息很或者说几乎不可能在一些工具上来来呈现。比如产生一些文档或者产生一些这样帮助事情。 有时候比如说Autowire 此时还不是自动绑定，它都很难承认Bean是不是在上下文里存在，因为没办法确保整个classpath上面所有的包都是不是少建了。它只能通过静态的方式来分析的上下文里面是不是有这些信息。
- 如果应用上下文存在多个 Bean 的定义，绑定时会产生歧义性。

解决方案：

- 关闭并放弃这种方式，采用直接或者精确的一个方位的方式避免这种歧义性。
- 在 Autowire-candidate 属性 从true 变成 false ,这种方式来显式地告知。
- 把 Bean 设置成 primary ，这是一种矫枉过正的做法，有时候若再上下文定义了相同类型多个 Bean 时，可以通过 primary = true 的方式定义获取首要的。

若想更细粒度的控制，建议使用注解的方式来进行配置各种各样的信息。

Autowiring 基本上是跟着 Xml 文件来走的。所以注解方面通常来说会好一点。

注解方式是通过一个精确的方式，如用 @Autowing 或者 @Qualifier 这种注解 来帮助我们精确定位到底用哪个相关的这些 bean的依赖。

## 依赖注入类型选择

- 注入造型
  - 低依赖： 构造器注入 byType* byName 官方推荐
  - 多依赖： Setter方法注入 有个不足， 就是注入时先后顺序依赖于用户操作先后顺序，如果有注入的字段或属性有顺序要求， 这个方法注入可能会有些问题。
  - 便利性： 字段注入
  - 声明类： 方法注入

## java基础类型和集合类型注入

### 基础类型注入

- 基础类型
  - 原生类型（Primitive）： boolean、byte 、 char 、 short、int、float、long、double
  - 标量类型（Scalar)： Number、Character、Boolean、 Enum、Locale、Charset、Currency、Properties、UUID
  - 常规类型(General): Object、String、TimeZone、Calendar、Optional
  - Spring类型： Resource、InputSource、Formatter

### 集合类型注入

- 集合类型
  - 数组类型（Array）： 原生类型、标量类型、 常规类型、Spring类型
  - 集合类型（Collection）
    - Collection： List、Set、(Sortedset、NavigableSet、EnumSet)
    - Map: Properties

```java
public class OptionalFactoryBean implements FactoryBean<Optional<String>> {

  @Override
  public Optional<String> getObject() throws Exception {
    return Optional.of("shawn");
  }

  @Override
  public Class<?> getObjectType() {
    return Optional.class;
  }
}
```

```java
@Bean
  public Resource resource(){
    return new ClassPathResource("application.properties");
  }

  @Bean
  public Optional<String> optional(){
    return Optional.of("Shawn");
  }

  @Bean
  public JavaBaseTypeInject annotationJavaBaseTypeInject(Resource resource){
    JavaBaseTypeInject inject = new JavaBaseTypeInject();
    inject.setB(true);
    inject.setC('a');
    inject.setCalendar(Calendar.getInstance());
    inject.setCharacter('f');
    inject.setD(111.0);
    inject.setF(11.2f);
    inject.setS((short) 12);
    inject.setI(22);
    inject.setL(555l);
    inject.setDate(new Date());
    inject.setFlag(Boolean.FALSE);
    inject.setInteger(23);
    inject.setName("shawn");
    inject.setNumber(2.0f);
    inject.setNumDouble(3.0d);
    inject.setOptional(Optional.of("jack"));
    inject.setTimeZone(TimeZone.getDefault());
    inject.setType(TYPE.MYSQL);
    inject.setList(Collections.singletonList(123));
    inject.setSet(Collections.singleton("bob"));
    Map<String, Object> map = new HashMap<>();
    map.put("id",1);
    map.put("name","john");
    inject.setMap(map);
    inject.setResource(resource);
    return inject;
  }

  public static void main(String[] args) {
//    annotationJavaBaseTypeInjection();
    xmlJavaBaseTypeInjection();
  }

  private static void annotationJavaBaseTypeInjection(){
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    // 注册 Configuration Class（配置类） -> Spring Bean
    context.register(DependencyJavaBaseClassInjectionDemo.class);
    // 启动 Spring 应用上下文
    context.refresh();
    JavaBaseTypeInject javaBaseTypeInject = context
        .getBean("annotationJavaBaseTypeInject", JavaBaseTypeInject.class);
    System.out.println(javaBaseTypeInject);
    // 显示地关闭 Spring 应用上下文
    context.close();
  }

  private static void xmlJavaBaseTypeInjection(){
    ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:/dependency-injection-java-base-type.xml");
    JavaBaseTypeInject bean = applicationContext.getBean(JavaBaseTypeInject.class);
    System.out.println(bean);
    applicationContext.close();
  }
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans
  xmlns="http://www.springframework.org/schema/beans"
  xmlns:context="http://www.springframework.org/schema/context"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd">

  <context:annotation-config />

  <context:component-scan base-package="com.shawn.study.spring.ioc.dependency_injection"/>

  <bean id="javaBaseType"
    class="com.shawn.study.spring.ioc.dependency_injection.entity.JavaBaseTypeInject">
    <property name="i" value="1"/>
    <property name="s" value="2"/>
    <property name="l" value="3"/>
    <property name="c" value="a"/>
    <property name="d" value="2.3"/>
    <property name="f" value="4.5"/>
    <property name="b" value="true"/>
    <!--
      private Number number;
      private Character character;
      private Integer integer;
      private Boolean flag;
      private Double numDouble;
      private TYPE type;
      -->
    <property name="number" ref="number"/>
    <property name="character" value="b"/>
    <property name="integer" value="14"/>
    <property name="flag" value="false"/>
    <property name="numDouble" value="16.5"/>
    <property name="type" value="ORACLE"/>
    <!--
      private Date date;
      private String name;
      private TimeZone timeZone;
      private Calendar calendar;
      private Optional<String> optional;
      -->

    <property name="date" ref="date"/>
    <property name="name" value="shawn"/>
    <property name="timeZone" ref="timeZone"/>
    <property name="calendar" ref="calendar"/>
    <property name="optional" ref="optional"/>

    <property name="resource" value="classpath:/application.properties"/>
    <property name="list">
      <list>
        <value>1</value>
        <value>2</value>
        <value>3</value>
      </list>
    </property>

    <property name="set">
      <set>
        <value>4</value>
        <value>5</value>
        <value>6</value>
      </set>
    </property>

    <property name="map">
      <map>
        <entry>
          <key>
            <value>id</value>
          </key>
          <value>1</value>
        </entry>
        <entry>
          <key>
            <value>name</value>
          </key>
          <value>shawn</value>
        </entry>
      </map>
    </property>
  </bean>

  <bean id="number" class="java.lang.Integer">
    <constructor-arg name="value" value="33"/>
  </bean>
  <bean id="date" class="java.util.Date"/>
  <bean id="timeZone" class="java.util.SimpleTimeZone" factory-method="getDefault"/>
  <bean id="calendar" class="java.util.GregorianCalendar" factory-method="getInstance"/>
  <bean id="optional" class="com.shawn.study.spring.ioc.dependency_injection.factory.OptionalFactoryBean"/>

</beans>
```

## 限定注入

- 使用注解 @Qualifier 限定
  - 通过Bean 名称限定
  - 通过分组限定
- 基于注解 @Qualifier 扩展限定
  - 自定义注解 - 如 Spring Cloud @LoadBalanced

```java
@Autowired
@Qualifier("person")
private Person person;

@Autowired
@Qualifier("person1")
private Person person1;

@Autowired
private List<Person> allPersons;

@Autowired
@Qualifier
private List<Person> qualifierPersons;

@Autowired
@PersonGroup
private List<Person> groupPersons;

@Bean
@Qualifier
private Person person() {
  return createPerson("person");
}

@Bean
@Qualifier
private Person person1() {
  return createPerson("person1");
}

@Bean
@PersonGroup
private Person person2() {
  return createPerson("person2");
}

@Bean
@PersonGroup
private Person person3() {
  return createPerson("person3");
}

private static Person createPerson(String name) {
  Person person = new Person();
  person.setId(new Random().nextInt(9000));
  person.setName(name);
  return person;
}

public static void main(String[] args) {

  // 创建 BeanFactory 容器
  AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
  // 注册 Configuration Class（配置类） -> Spring Bean
  applicationContext.register(QualifierDependencyInjectionDemo.class);

  // 启动 Spring 应用上下文
  applicationContext.refresh();

  // 依赖查找 QualifierAnnotationDependencyInjectionDemo Bean
  QualifierDependencyInjectionDemo demo = applicationContext
      .getBean(QualifierDependencyInjectionDemo.class);

  // 期待输出 person Bean
  System.out.println("demo.person = " + demo.person);
  // 期待输出 person1 Bean
  System.out.println("demo.person1 = " + demo.person1);
  // 期待输出 person + person1 + person2 + person3 Bean
  System.out.println("demo.allPersons = " + demo.allPersons);

  // 如果没有@PersonGroup注解，预期输出 person + person1 Bean
  // 如果加上@PersonGroup注解，预期输出 person + person1 + person2 + person3 Bean
  System.out.println("demo.qualifierPersons = " + demo.qualifierPersons);
  // 期待输出 person2 + person3 Bean
  System.out.println("demo.groupPersons = " + demo.groupPersons);

  // 显示地关闭 Spring 应用上下文
  applicationContext.close();

}
```

## 延迟依赖注入

- 使用 API ObjectFactory 延迟注入

  - 单一类型
  - 集合类型

- 使用API Objectprovider 延迟注入 （推荐）， 其实就是扩展了ObjectFactory的类

  - 单一类型
  - 集合类型

- 从安全的角度讲，ObjectProvider 可以减少或避免相关的一些异常。

  SpringBoot/Cloud等的构造技术中会大量用到ObjectProvider进行注入一些非必要性的依赖，从而避免异常。

## 依赖处理过程

- 基础知识
  - 入口 - DefaultListableBeanFactory # resolveDependency
  - 依赖描述符： DependencyDescriptor
  - 自定绑定候选处理器 - AutowireCandidateResolver

## @Autowired 注入

- @Autowired 注入过程
  - 元信息解析
  - 依赖查找
  - 依赖注入（ 字段、 方法）
- 核心类 AutowiredAnnotationBeanPostProcessor
  - postProcessMergedBeanDefinition
  - postProcessProperties

1. 在doCreateBean中会先调用applyMergedBeanDefinitionPostProcessors，后执行populateBean所以会先调用postProcessMergedBeanDefinition后执行InstantiationAwareBeanPostProcessor的postProcessProperties。
2. postProcessProperties中有两个步骤：
   * findAutowiringMetadata查找注入元数据，没有缓存就创建，具体是上一节内容。最终会返回InjectionMetadata，里面包括待注入的InjectedElement信息（field、method）等等
   * 执行InjectionMetadata的inject方法，具体为AutowiredFieldElement和AutowiredMethodElement的Inject方法
     AutowiredFieldElement inject具体流程：
     - DependencyDescriptor的创建
     - 调用beanFactory的resolveDependency获取带注入的bean。resolveDependency根据具体类型返回候选bean的集合或primary 的bean
     - 利用反射设置field

## @Inject 注入

- @Inject 注入过程
  - 如果JSR-330 存在于ClassPath 中， 复用AutowiredAnnotationBeanPostProcessor 实现。
  - 可以处理 @Autowired @Value @Inject
  - AutowiredAnnotationBeanPostProcessor 进行了 @Autowired 和 @Inject 的注解处理。 处理逻辑基本一致。
- CommonAnnotationBeanPostProcessor
  - 注入注解
    - javax.xml.ws.WebServiceRef
    - javax.ejb.EJB
    - javax.annotation.Resource
  - 生命周期注解
    - javax.annotation.PostConstruct
    - javax.annotation.PreDestroy

## 如何自定义依赖注入

- 基于AutowiredAnnotationBeanPostProcessor 实现
- 自定义实现
  - 生命周期处理
    - InstanticationAwareBeanPostProcessor
    - MergedBeanDefinitionPostProcessor
  - 元数据
    - InjectedElement
    - InjectionMetadata

