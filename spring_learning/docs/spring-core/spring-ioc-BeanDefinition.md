## 什么是BeanDefinition

`BeanDefinition`是`Spring framework`定义`bean`的配置元信息的接口，包含：

* `Bean`的类名
* `Bean`的行为配置，如作用域，自动装配的配置，生命周期回调等。
* 其他`Bean`引用，又可称做合作者(`Collaborators`)或者依赖(`Dependencies`)
* 配置设置，比如`bean properties`

## BeanDefinition的元信息

| 属性                     | 说明                                                         |
| ------------------------ | ------------------------------------------------------------ |
| class                    | `Bean`的全限定类名，必须是一个具体的实现类，不能是抽象类或者是接口 |
| name                     | `Bean`的名称或者id                                           |
| scope                    | `bean`的作用域                                               |
| constructor arguments    | `bean`的构造器参数                                           |
| properties               | `bean`的属性参数                                             |
| autowiring mode          | `bean`的自动绑定模式                                         |
| lazy initialization mode | `bean`的延迟初始化模式                                       |
| initialization method    | `bean`的初始化方法                                           |
| destruction method       | `bean`的销毁方法                                             |

`BeanDefinition`配置元信息的简单demo示例：

1. `Beandefinition-demo.xml`

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans
     xmlns="http://www.springframework.org/schema/beans"
     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     xsi:schemaLocation="http://www.springframework.org/schema/beans
           https://www.springframework.org/schema/beans/spring-beans.xsd">
   
     <bean id="person" class="com.shawn.study.spring.ioc.bean.basic.domain.Person" scope="singleton">
       <constructor-arg name="name" type="java.lang.String">
         <value>shawn</value>
       </constructor-arg>
       <constructor-arg index="1" type="java.lang.String">
         <value>1234567890123</value>
       </constructor-arg>
       <constructor-arg index="2">
         <value>25</value>
       </constructor-arg>
     </bean>
   
   </beans>
   ```

2. `Person.java`

   ```java
   public class Person {
   
     private String name;
     private String idCard;
     private int age;
   
     public Person() {
   
     }
   
     public Person(String name, String idCard, int age) {
       this.name = name;
       this.idCard = idCard;
       this.age = age;
     }
   
     public String getName() {
       return name;
     }
   
     public void setName(String name) {
       this.name = name;
     }
   
     public String getIdCard() {
       return idCard;
     }
   
     public void setIdCard(String idCard) {
       this.idCard = idCard;
     }
   
     public int getAge() {
       return age;
     }
   
     public void setAge(int age) {
       this.age = age;
     }
   
     @Override
     public String toString() {
       return "Person{" +
           "name='" + name + '\'' +
           ", idCard='" + idCard + '\'' +
           ", age=" + age +
           '}';
     }
   }
   ```

3. `BeanDefinitionDemo.java`

   ```java
   public class BeanDefinitionDemo {
   
     public static void main(String[] args) {
       ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
           "classpath:/beandefinition-demo.xml");
       Person person = applicationContext.getBean("person", Person.class);
       System.out.println(person);
     }
   
   }
   ```

## BeanDefinition的类图关系

![spring-bean-definition-class-relations](.\images\spring-bean-definition-class-relations.png)

一般情况下，构建`BeanDefinition`可以通过`BeanDefinitionBuilder`构建，也可以通过`AbstractBeanDefinition`的派生类构建。

**代码实现：**

```java
public class BeanDefinitionCreationDemo {

  public static void main(String[] args) {
    createBeanDefinitionByBuilder();
    createBeanDefinitionByAbstract();
  }

  /**
   * 使用{@link BeanDefinitionBuilder}创建
   */
  private static void createBeanDefinitionByBuilder() {
    BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(Person.class);
    builder.addPropertyValue("name", "Shawn")
        .addPropertyValue("idCard", "1234566778").addPropertyValue("age", 25);
    BeanDefinition beanDefinition = builder.getBeanDefinition();
    MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
    propertyValues.getPropertyValueList().forEach(propertyValue -> System.out
        .printf("%s=%s\n", propertyValue.getName(), propertyValue.getValue()));
  }

  /**
   * 使用{@link AbstractBeanDefinition}的派生类创建
   *
   * @see RootBeanDefinition
   * @see ChildBeanDefinition
   * @see GenericBeanDefinition
   */
  private static void createBeanDefinitionByAbstract() {
    AbstractBeanDefinition beanDefinition = new GenericBeanDefinition();
    beanDefinition.setBeanClass(Person.class);
    ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
    constructorArgumentValues.addIndexedArgumentValue(0, "Shawn", "java.lang.String");
    ValueHolder valueHolder = new ValueHolder("123423312145", "java.lang.String");
    constructorArgumentValues.addIndexedArgumentValue(1, valueHolder);
    constructorArgumentValues.addIndexedArgumentValue(2, 26);
    beanDefinition.setConstructorArgumentValues(constructorArgumentValues);
    Map<Integer, ValueHolder> indexedArgumentValues = beanDefinition.getConstructorArgumentValues()
        .getIndexedArgumentValues();
    indexedArgumentValues.forEach((index, holder) ->
        System.out
            .printf("index = %d,  %s=%s, type=%s\n", index, holder.getName(), holder.getValue(),
                holder.getType())
    );
  }

}
```

**`RootBeanDefinition,ChildBeanDefinition,GenericBeanDefinition`三种的区别：**

* 从上图可以看出，`RootBeanDefinition`、`ChildBeanDefinition`、`GenericBeanDefinition`。他们都是`AbstractBeanDefinition`的直接实现类

* `GenericBeanDefinition`：标准bean definition，通用的除了具有指定类、可选的构造参数值和属性参数这些其它`bean definition`一样的特性外，它还具有通过`parenetName`属性来灵活（动态）设置`parent bean definition`，而非硬编码作为`root bean definition`。

* `ChildBeanDefinition`：子Bean定义信息，依赖于父类`RootBeanDefinition`，`ChildBeanDefinition`是一种bean definition，它可以继承它父类的设置，即`ChildBeanDefinition`对`RootBeanDefinition`有一定的依赖关系。

* `RootBeanDefinition`:定义表明它是一个可合并的bean definition：即在spring beanFactory运行期间，可以返回一个特定的bean。但在Spring2.5以后，我们绝大多数情况还是可以使用`GenericBeanDefinition`来做。我们非常熟悉的`final RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);`这句代码，就是去合并parent的属性进来，这样体现了继承的强大。属性也才完整。

  在配置文件中可以定义父和子，`父用RootBeanDefinition`表示， 而`子用ChildBeanDefiniton`表示，而没有父的就使用 `RootBeanDefinition`表示。

值得一提的是：Spring常用的注解对应的BeanDefinition分别是

1. @Configuration：AnnotatedGenericBeanDefinition
2. @Bean：ConfigurationClassBeanDefinition
3. @Component、@Service、@Controller、@Repository：ScannedGenericBeanDefinition

## Bean如何命名，取别名

### Bean的名称

* 每个Bean拥有一个或多个标识符(identifiers)，这些标识符在bean所在的容器必须是唯一的。通常，一个bean仅有一个标识符，如果需要额外的，可考虑使用别名(Alias)来扩充。
* 在基于xml的配置元信息中，开发人员可用id或者name属性来规定bean的标识符。通常bean的标识符由字母组成，允许出现特殊字符。如果想要引入bean的别名的话，可在name属性使用`,`或者`;`来间隔。
* bean的id或name属性并非必须制定，如果留空的话，容器会为bean自动生成一个唯一的bean名称。
* bean的命名尽管没有限制，但官方建议采用驼峰式的方式，更符合java的命名规约。

### Bean名称生成器

* `DefaultBeanNameGenerator`
* `AnnotationBeanNameGenerator`

### Bean的别名

* 复用现有的`BeanDefinition`
* 更具有场景化的命名方法。

show me the code:

`org.springframework.beans.factory.support.DefaultBeanNameGenerator#generateBeanName`

`org.springframework.beans.factory.support.BeanDefinitionReaderUtils#generateBeanName(org.springframework.beans.factory.config.BeanDefinition, org.springframework.beans.factory.support.BeanDefinitionRegistry, boolean)`

`org.springframework.beans.factory.support.BeanDefinitionReaderUtils#uniqueBeanName`

```java
public static String generateBeanName(
  BeanDefinition definition, BeanDefinitionRegistry registry, boolean isInnerBean)
  throws BeanDefinitionStoreException {

 // 类的全限定名
 String generatedBeanName = definition.getBeanClassName();
 if (generatedBeanName == null) {
  if (definition.getParentName() != null) {
   generatedBeanName = definition.getParentName() + "$child";
  }
  else if (definition.getFactoryBeanName() != null) {
   generatedBeanName = definition.getFactoryBeanName() + "$created";
  }
 }
 if (!StringUtils.hasText(generatedBeanName)) {
  throw new BeanDefinitionStoreException("Unnamed bean definition specifies neither " +
    "'class' nor 'parent' nor 'factory-bean' - can't generate bean name");
 }

 String id = generatedBeanName;
 // 内部类处理
 if (isInnerBean) {
  // Inner bean: generate identity hashcode suffix.
  id = generatedBeanName + GENERATED_BEAN_NAME_SEPARATOR + ObjectUtils.getIdentityHexString(definition);
 }
 else {
  // 生成唯一的beanName
  return uniqueBeanName(generatedBeanName, registry);
 }
 return id;
}

public static String uniqueBeanName(String beanName, BeanDefinitionRegistry registry) {
    String id = beanName;
    int counter = -1;

    // Increase counter until the id is unique.
    while (counter == -1 || registry.containsBeanDefinition(id)) {
        counter++;
        id = beanName + GENERATED_BEAN_NAME_SEPARATOR + counter;
    }
    return id;
}

```

```java
private static void testBeanName() {
  ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
      "classpath:/beandefinition-demo.xml");
  String[] beanNamesForType = applicationContext.getBeanNamesForType(Person.class);
  // person
  // com.shawn.study.spring.ioc.bean.basic.domain.Person#0
  // com.shawn.study.spring.ioc.bean.basic.domain.Person#1
  Arrays.stream(beanNamesForType).forEach(System.out::println);
}
```

```java
/**
* {@link BeanDefinitionReaderUtils#uniqueBeanName}示例
*/
private static void testUniqueBeanName() {
  AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
  applicationContext.register(BeanNameDemo.class);
  applicationContext.refresh();
  String person = BeanDefinitionReaderUtils.uniqueBeanName("person", applicationContext);
  applicationContext.registerBean(person, Person.class);
  String[] beanNames = applicationContext.getBeanNamesForType(Person.class);
  Arrays.stream(beanNames).forEach(System.out::println);
  applicationContext.close();
}
/**
* {@link BeanDefinitionReaderUtils#generateBeanName}示例
*/
private static void testGenerateBeanName() {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
    applicationContext.register(BeanNameAndAliasDemo.class);
    applicationContext.refresh();
    BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(Person.class);
    BeanDefinition beanDefinition = builder.addPropertyValue("name", "shawn")
        .addPropertyValue("idCard", "123412312412").addPropertyValue("age", 26).getBeanDefinition();
    String beanName = BeanDefinitionReaderUtils
        .generateBeanName(beanDefinition, applicationContext, false);
    applicationContext.registerBean(beanName, Person.class);
    String[] beanNames = applicationContext.getBeanNamesForType(Person.class);
    Arrays.stream(beanNames).forEach(System.out::println);
    applicationContext.close();
}

/**
 * {@link org.springframework.beans.factory.support.BeanNameGenerator} 示例
 */
private static void testBeanNameGenerator() {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
    applicationContext.setBeanNameGenerator(new AnnotationBeanNameGenerator(){
        @Override
        public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
            return "custom-" + super.generateBeanName(definition, registry);

        }
    });
    applicationContext.register(BeanNameAndAliasDemo.class);
    applicationContext.refresh();
    applicationContext.registerBean(Person.class);
    String[] beanNames = applicationContext.getBeanNamesForType(Person.class);
    Arrays.stream(beanNames).forEach(System.out::println);
    applicationContext.close();
}
```

```java
/**
* bean别名示例
*/
private static void testAlias(){
  ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
      "classpath:/beandefinition-demo.xml");
  Person person = applicationContext.getBean("person", Person.class);
  Person aliasPerson = applicationContext.getBean("alias-person", Person.class);
  System.out.printf("person是否与alias-person相同？ 答案：%s\n", (person == aliasPerson)); //(true)
}
```

## 注册spring bean

1. xml配置元信息 `<bean></bean>`
2. annotation  
   * `@Bean`
   * `@Component,@Service,@Repository @Controller @Configuration ...`
   * `@Import`
3. `Spring api`
   * 命名方式：`org.springframework.beans.factory.support.BeanDefinitionRegistry#registerBeanDefinition`
   * 非命名方式：`org.springframework.beans.factory.support.BeanDefinitionReaderUtils#registerWithGeneratedName`
   * 配置类方式：`org.springframework.context.annotation.AnnotatedBeanDefinitionReader#register`
4. 外部单例对象注册：`org.springframework.beans.factory.config.SingletonBeanRegistry#registerSingleton`

**show me the code:**

1. `BeanConfig.java`

   ```java
   public class BeanConfig {
   
     @Bean
     public Person person() {
       Person p = new Person();
       p.setIdCard("123213254sas");
       p.setName("shawn_import");
       p.setAge(26);
       return p;
     }
   
   }
   ```

2. `ComponentConfig.java`

   ```java
   @Component
   public class ComponentConfig {
   
     @Bean
     public Person person() {
       Person p = new Person();
       p.setIdCard("123213254sas");
       p.setName("shawn_component");
       p.setAge(26);
       return p;
     }
   
   }
   ```

3. `UserFactory.java&& DefaultUserFactory.java`

   ```java
   public interface UserFactory {
   
     default User createUser(){
       return User.getInstance();
     }
   
   }
   
   public class DefaultUserFactory implements UserFactory{
   
   }
   ```

4. `BeanDefinitionRegisterDemo.java`

```java
@Import(BeanConfig.class)
public class BeanDefinitionRegisterDemo {

  public static void main(String[] args) {
    registerBeanByAnnotationWithImport();
    registerBeanByAnnotationWithComponent();

    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
    applicationContext.register(BeanDefinitionRegisterDemo.class);
    registerBeanDefinition(applicationContext, "person");
    registerBeanDefinitionWithoutBeanName(applicationContext);
    applicationContext.refresh();

    System.out.println(applicationContext.getBeansOfType(Person.class));
    applicationContext.close();

    registerSingletonBean();
  }

  /**
   * {@link Import}示例 结果：有三个bean被注册： 1. BeanDefinitionRegisterDemo 2. BeanConfig 3. Person
   */
  private static void registerBeanByAnnotationWithImport() {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
    // 实际上 AnnotationConfigApplicationContext.register 内部调用的 就是 AnnotatedBeanDefinitionReader#register方法
    applicationContext.register(BeanDefinitionRegisterDemo.class);
    applicationContext.refresh();
    String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
    Arrays.stream(beanDefinitionNames).forEach(System.out::println);
    Person person = applicationContext.getBean("person", Person.class);
    System.out.println(person);
    applicationContext.close();
  }

  /**
   * {@link org.springframework.stereotype.Component} 有两个bean被注册： 1. ComponentConfig 2. Person
   */
  private static void registerBeanByAnnotationWithComponent() {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
    applicationContext.scan("com.shawn.study.spring.ioc.bean.basic.configuration");
    applicationContext.refresh();
    String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
    Arrays.stream(beanDefinitionNames).forEach(System.out::println);
    Person person = applicationContext.getBean("person", Person.class);
    System.out.println(person);
    applicationContext.close();
  }

  /**
   * 命名方式 注册spring bean {@link BeanDefinitionRegistry#registerBeanDefinition(String,
   * BeanDefinition)}
   *
   * @param registry
   * @param beanName
   */
  private static void registerBeanDefinition(BeanDefinitionRegistry registry,
      String beanName) {
    BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(Person.class);
    AbstractBeanDefinition beanDefinition = builder.addPropertyValue("name", "shawn")
        .addPropertyValue("idCard", "123412312412").addPropertyValue("age", 26).getBeanDefinition();
    if (StringUtils.hasText(beanName)) {
      // 注册 BeanDefinition
      registry.registerBeanDefinition(beanName, beanDefinition);
    } else {
      // 非命名 Bean 注册方法
      BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinition, registry);
    }
  }

  /**
   * 非命名方式 注册spring bean {@link BeanDefinitionReaderUtils#registerWithGeneratedName(AbstractBeanDefinition,
   * BeanDefinitionRegistry)}
   *
   * @param registry
   */
  private static void registerBeanDefinitionWithoutBeanName(BeanDefinitionRegistry registry) {
    registerBeanDefinition(registry, null);
  }

  /**
   * {@link SingletonBeanRegistry#registerSingleton(String, Object)}示例
   */
  private static void registerSingletonBean() {
    // 创建 BeanFactory 容器
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
    // 创建一个外部 UserFactory 对象
    UserFactory userFactory = new DefaultUserFactory();
    SingletonBeanRegistry singletonBeanRegistry = applicationContext.getBeanFactory();
    // 注册外部单例对象
    singletonBeanRegistry.registerSingleton("userFactory", userFactory);
    // 启动 Spring 应用上下文
    applicationContext.refresh();

    // 通过依赖查找的方式来获取 UserFactory
    UserFactory userFactoryByLookup = applicationContext.getBean("userFactory", UserFactory.class);
    System.out
        .println("userFactory  == userFactoryByLookup : " + (userFactory == userFactoryByLookup));

    // 关闭 Spring 应用上下文
    applicationContext.close();
  }
}
```

该接口约定了实现者(一般指容器)具有如下能力 :

1. 注册登记将创建完成的单例`bean`实例;
2. 获取某个指定名称的单例`bean`实例;
3. 检测是否已经注册登记了某个指定名称的单例`bean`实例;
4. 获取所有单例`bean`实例的名称;
5. 获取注册登记的所有单例`bean`实例的数量;
6. 获取当前单例`bean`实例注册表的互斥量(`mutex`),供外部使用者协作(`collaborator`)使用；

## 实例化spring bean

* 常规方式
  1. 通过构造器(配置元信息：xml, annotation和java api)
  2. 通过静态工厂方法(配置元信息: xml和java api)
  3. 通过bean工厂方法（配置元信息：xml和java api）
  4. 通过FactoryBean（配置元信息：xml, annotation和java api）
* 特殊方式
  1. `ServiceLoaderFactoryBean`（配置元信息：xml, annotation和java api）
  2. `org.springframework.beans.factory.config.AutowireCapableBeanFactory#createBean(java.lang.Class<?>, int, boolean)`
  3. `org.springframework.beans.factory.support.BeanDefinitionRegistry#registerBeanDefinition`

**show me the code:**

```java
public class BeanInstantiationDemo {

  private final static String LOCATION = "classpath:bean-instantiation-context.xml";

  public static void main(String[] args) {
    ApplicationContext applicationContext = new ClassPathXmlApplicationContext(LOCATION);
    beanInstantiationByStaticMethod(applicationContext);
    beanInstantiationByInstanceMethod(applicationContext);
    beanInstantiationByFactoryBean(applicationContext);
    System.out.println("演示serviceLoader: ");
    beanInstantiationByServiceLoader(applicationContext);
    System.out.println("演示AutowireCapable: ");
    beanInstantiationByAutowireCapable(applicationContext);
  }

  /**
   * 基于静态工厂方法的bean的实例化
   * @param applicationContext
   */
  private static void beanInstantiationByStaticMethod(ApplicationContext applicationContext){
    Person person = applicationContext
        .getBean("createPersonByStaticMethod", Person.class);
    System.out.println(person);
  }

  /**
   * 基于实例工厂方法的bean的实例化
   * @param applicationContext
   */
  private static void beanInstantiationByInstanceMethod(ApplicationContext applicationContext){
    Person person = applicationContext
        .getBean("createPersonByInstanceMethod", Person.class);
    System.out.println(person);
  }

  /**
   * 基于{@link org.springframework.beans.factory.FactoryBean}的实例化
   * @param applicationContext
   */
  private static void beanInstantiationByFactoryBean(ApplicationContext applicationContext){
    Person person = applicationContext
        .getBean("createPersonByFactoryBean", Person.class);
    System.out.println(person);
  }

  /**
   * 基于{@link ServiceLoader}的实例化
   * @param applicationContext
   */
  private static void beanInstantiationByServiceLoader(ApplicationContext applicationContext){
    ServiceLoader<PersonFactory> serviceLoader = applicationContext.getBean("personFactoryServiceLoader", ServiceLoader.class);
    Iterator<PersonFactory> iterator = serviceLoader.iterator();
    while (iterator.hasNext()) {
      PersonFactory personFactory = iterator.next();
      System.out.println(personFactory.createPerson());
    }
  }

  /**
   * 基于{@link AutowireCapableBeanFactory}的实例化   {@link org.springframework.beans.factory.support.BeanDefinitionRegistry}
   * @param applicationContext
   */
  private static void beanInstantiationByAutowireCapable(ApplicationContext applicationContext){
    AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext
        .getAutowireCapableBeanFactory();
    DefaultPersonFactory factory = autowireCapableBeanFactory.createBean(DefaultPersonFactory.class);
    System.out.println(factory.createPerson());
  }

  /**
   *   基于{@link org.springframework.beans.factory.support.BeanDefinitionRegistry#registerBeanDefinition(String, BeanDefinition)}
   *   的实例化可参考：com.shawn.study.spring.ioc.bean.basic.application.BeanDefinitionRegisterDemo#registerBeanDefinition
   */
}
```

## 初始化spring bean

### 实时初始化

1. `@PostConstruct`
2. 实现`org.springframework.beans.factory.InitializingBean#afterPropertiesSet`
3. 自定义初始化方法
   * xml配置：`<bean init-method="">`
   * annotation：`@Bean(initMethod = "")`
   * java api: `org.springframework.beans.factory.support.AbstractBeanDefinition#setInitMethodName`

**三种方式均在同一个Bean里实现，那么他的方法执行的顺序是怎样的？**

### 延迟初始化

1. xml配置：`<bean init-method="" lazy-init="true">`
2. annotation：`@Lazy(true)`

**当某个bean定义为延迟初始化，那么spring容器返回的对象与非延迟的对象存在怎样的差异？**

## 销毁spring bean

1. `@PreDestroy`
2. 实现`org.springframework.beans.factory.DisposableBean#destroy`
3. 自定义初始化方法
   * xml配置：`<bean destroy="">`
   * annotation：`@Bean(destroy= "")`
   * java api: `org.springframework.beans.factory.support.AbstractBeanDefinition#setDestroyMethodName`

**三种方式均在同一个Bean里实现，那么他的方法执行的顺序是怎样的？**

## 垃圾回收spring bean

1. 关系spring ioc容器
2. 执行gc
3. spring bean覆盖的`finalize()`方法被回调

**show me the code:**

`BeanInitializationAndDestroyAndGcDemo.java`

```java
public class BeanInitializationAndDestroyAndGcDemo {

  public static void main(String[] args) throws Exception{
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
    applicationContext.register(BeanInitializationAndDestroyAndGcDemo.class);
    System.out.println("spring application context is ready to refresh!");
    applicationContext.refresh();
    System.out.println("spring application context finished to refresh!");
    PersonFactory personFactory = applicationContext.getBean("personFactory", PersonFactory.class);
    System.out.println("personFactoryLazy: ");
    PersonFactory personFactoryLazy = applicationContext.getBean("personFactoryLazy", PersonFactory.class);
    personFactory.createPerson();
    System.out.println("personFactoryLazy.createPerson: ");
    personFactoryLazy.createPerson();

    System.out.println("spring application context is ready to destroy!");
    applicationContext.close();
    System.out.println("spring application context finished to destroy!");
    TimeUnit.SECONDS.sleep(10);
    System.gc();
    TimeUnit.SECONDS.sleep(10);
  }

  @Bean(initMethod = "initByMethod", destroyMethod = "destroyByMethod")
  public PersonFactory personFactory(){
    return new DefaultPersonFactory();
  }

  @Bean(initMethod = "initByMethod", destroyMethod = "destroyByMethod")
  @Lazy
  public PersonFactory personFactoryLazy(){
    return new DefaultPersonFactory();
  }

}
```

`DefaultPersonFactory.java`

```java
public class DefaultPersonFactory implements PersonFactory, InitializingBean, DisposableBean {

  @PostConstruct
  public void initByPostConstruct(){
    System.out.println("DefaultPersonFactory.initByPostConstruct");
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    System.out.println("DefaultPersonFactory.afterPropertiesSet");
  }

  public void initByMethod(){
    System.out.println("DefaultPersonFactory.initByMethod");
  }

  @PreDestroy
  public void destroyByPreDestroy(){
    System.out.println("DefaultPersonFactory.destroyByPreDestroy");
  }

  @Override
  public void destroy() throws Exception {
    System.out.println("DefaultPersonFactory.destroy");
  }

  public void destroyByMethod(){
    System.out.println("DefaultPersonFactory.destroyByMethod");
  }

  @Override
  protected void finalize() throws Throwable {
    System.out.println("DefaultPersonFactory.finalize");
    super.finalize();
  }
}
```

上述代码执行结果：

```
spring application context is ready to refresh!
DefaultPersonFactory.initByPostConstruct
DefaultPersonFactory.afterPropertiesSet
DefaultPersonFactory.initByMethod
spring application context finished to refresh!
personFactoryLazy: 
DefaultPersonFactory.initByPostConstruct
DefaultPersonFactory.afterPropertiesSet
DefaultPersonFactory.initByMethod
personFactoryLazy.createPerson: 
spring application context is ready to destroy!
DefaultPersonFactory.destroyByPreDestroy
DefaultPersonFactory.destroy
DefaultPersonFactory.destroyByMethod
DefaultPersonFactory.destroyByPreDestroy
DefaultPersonFactory.destroy
DefaultPersonFactory.destroyByMethod
spring application context finished to destroy!
DefaultPersonFactory.finalize  (不一定会执行到)
```

**三种初始化、销毁方式均在同一个Bean里实现，那么他的方法执行的顺序是怎样的？**

根据结果显示：

`@PostConstruct > org.springframework.beans.factory.InitializingBean#afterPropertiesSet > 自定义初始化方法`

销毁方法是在执行close方法的时候调用的。

`@PreDestroy >  org.springframework.beans.factory.DisposableBean#destroy > 自定义销毁方法 `

**当某个bean定义为延迟初始化，那么spring容器返回的对象与非延迟的对象存在怎样的差异？**

答：如果是实时初始化，是在调用refresh方法时候进行初始化。如果是延迟初始化，是在依赖查找的时候进行初始化。