## 依赖查找

它是控制反转设计原则的一种实现方式。它的大体思路是：容器中的受控对象通过容器的 API 来查找自己所依赖的资源和协作对象。这种方式虽然降低了对象间的依赖，但是同时也使用到了容器的 API，造成了我们无法在容器外使用和测试对象。依赖查找是一种更加传统的 IOC 实现方式。

## 单一类型依赖查找

核心接口：`BeanFactory`

* 根据bean名称查找:`getBean(String)`
* 根据bean类型查找:`getBean(Class)`
* 根据bean名称+类型查找: `getBean(String, class)`

注意：覆盖参数的调用建议不要运用，该方式非常危险，该接口会覆盖掉一些默认参数。

**show me the code:**

```java
public class SingletonDependencyLookupDemo {

  public static void main(String[] args) {
    AnnotationConfigApplicationContext applicationContext =
        new AnnotationConfigApplicationContext();
    applicationContext.register(SingletonDependencyLookupDemo.class);
    applicationContext.refresh();
    // 根据bean name查找
    Singleton singleton = Singleton.class.cast(applicationContext.getBean("singleton"));
    // 根据bean name+bean type查找
    Singleton singleton1 = applicationContext.getBean("singleton", Singleton.class);
    System.out.println(singleton == singleton1);

    // 根据bean name查找
    Singleton singleton2 = (Singleton) applicationContext.getBean("singleton1");
    /*
    Exception in thread "main" org.springframework.beans.factory.NoUniqueBeanDefinitionException:
    No qualifying bean of type 'com.shawn.study.spring.ioc.dependency_lookup.SingletonDependencyLookupDemo$Singleton' available:
    expected single matching bean but found 2: singleton,singleton1

    使用根据bean class类型进行依赖查找，如果有多个bean的类型相同，则会报错。
    解决方案1：根据bean name依赖查找；
    解决方案2：根据bean name + bean class type依赖查找；
    解决方案3：使用@Primary注解标注，或者xml里配置primary
     */
    // 根据bean type查找
    Singleton bean = applicationContext.getBean(Singleton.class);
    System.out.println(bean == singleton2);
    applicationContext.close();
  }

  @Bean
  public Singleton singleton() {
    Singleton singleton = new Singleton();
    singleton.setId(1);
    return singleton;
  }

  // 方法名作为bean name
  @Bean
  @Primary
  public Singleton singleton1() {
    Singleton singleton = new Singleton();
    singleton.setId(2);
    return singleton;
  }

  private static class Singleton {

    private int id;

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    @Override
    public String toString() {
      return "Singleton{" + "id=" + id + '}';
    }
  }
}
```

## 集合类型依赖查找

核心接口：`ListableBeanFactory`

* 根据Bean类型查找：
  * 获取同类型bean名称列表:`getBeanNamesForType(Class)`
  * 获取同类型Bean实例列表：`getBeansOfType(Class)`
* 根据注解类型查找：
  * 获取标注类型bean名称列表：`getBeanNamesForAnnotation(Class <? extends Annotation>)`
  * 获取标注类型bean实例列表：`getBeansWithAnnotation(Class <? extends Annotation>)`
  * 获取指定名称+标注类型bean实例：`findAnnotationOnBean(String, Class<? extends Annotation>)`

**show me the code:**

```java
public class CollectionDependencyLookupDemo {

  public static void main(String[] args) {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
    applicationContext.register(CollectionDependencyLookupDemo.class);
    applicationContext.refresh();
    // 根据bean类型获取bean名称列表
    String[] names = applicationContext.getBeanNamesForType(Person.class);
    Arrays.stream(names).forEach(System.out::println);

    // 根据bean类型获取bean实例列表
    // 返回的是map对象，key为bean名称，value为bean类型
    Map<String, Person> beans = applicationContext.getBeansOfType(Person.class);
    beans.forEach((key, value) -> {
      System.out.println(String.format("%s=%s", key, value));
    });

    // 根据标注java注解获取bean实例列表
    // 首先得先是个bean，可以利用@Bean的方式标注，然后根据自定义的注解筛选
    Map<String, Object> beansWithAnnotation = applicationContext
        .getBeansWithAnnotation(MyBean.class);
    beansWithAnnotation.forEach((key, value) -> {
      System.out.println(String.format("%s=%s", key, Person.class.cast(value)));
    });

    // 根据标注java注解获取bean名称列表
    String[] beanNamesForAnnotation = applicationContext.getBeanNamesForAnnotation(MyBean.class);
    Arrays.stream(beanNamesForAnnotation).forEach(System.out::println);

    // 获取指定名称+标注类型bean实例
    MyBean student = applicationContext.findAnnotationOnBean("student", MyBean.class);
    if (student != null) {
      System.out.println(applicationContext.getBean("student"));
    }
    applicationContext.close();
  }

  @Bean
  public Person person() {
    Person person = new Person();
    person.setAge(25);
    person.setName("person");
    return person;
  }

  @MyBean
  @Bean
  public Person myBean() {
    Person person = new Person();
    person.setAge(25);
    person.setName("myBean");
    return person;
  }

  @Bean
  @MyBean
  public Person student() {
    Student p = new Student();
    p.setName("student");
    p.setAge(26);
    p.setScore(95);
    return p;
  }

  private static class Person {

    private String name;
    private int age;

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

    @Override
    public String toString() {
      return "Person{" +
          "name='" + name + '\'' +
          ", age=" + age +
          '}';
    }
  }

  private static class Student extends Person {

    private int score;

    public int getScore() {
      return score;
    }

    public void setScore(int score) {
      this.score = score;
    }

    @Override
    public String toString() {
      return "Student{" +
          "name='" + getName() + '\'' +
          ", age=" + getAge() +
          ", score=" + getScore() +
          '}';
    }
  }
}
```

## 层次性依赖查找

将单一类型和集合类型合并查找的方式。

和ClassLoader里面的双亲委派非常相似，都是相同的设计模式：如果有层次性，此时当父亲或者父亲的父亲，一直往上推，推到能够找到其根的第一个对象为止。该对象通过层层递进的方式进行查找。

核心接口：`HierarchicalBeanFactory`

* 双亲委派机制：`BeanFactory:getParentBeanFactory()`
* 层次性查找：
  * 根据bean名称查找：`containsLocalBean`
  * 根据Bean类型查找实例列表：
    * 单一类型：`BeanFactoryUtils#beanOfType`
    * 集合类型：`BeanFactoryUtils#beansOfTypeIncludingAncestors`
  * 根据java注解查找名称列表
    * `BeanFactoryUtils#beanNamesForTypeIncludingAncestors`

`HierarchicalBeanFactory`继承了`BeanFactory`，扩展了父类容器的方法，比如对父容器的获取。

有两个方法：

* `getParentBeanFactory`: 获取父容器，跟BeanFactory一样，只有get没有put的操作，可见在spring中，获取与设置是分开的。
* `containsLocalBean:`判断当前容器是否包含某个bean，忽略了父类容器中的bean。

那么setParentBeanFactory的方法在哪呢？

`org.springframework.beans.factory.config.ConfigurableBeanFactory#setParentBeanFactory`

`ConfigurableBeanFactory`继承了`HierarchicalBeanFactory`。`ConfigurableBeanFactory`又被`ConfigurableListableBeanFactory`继承

同时`ConfigurableListableBeanFactory`又是`ListableBeanFactory, AutowireCapableBeanFactory`，所以`ConfigurableListableBeanFactory`既包含了单一类型查找，集合类型查找，还包含了层次性依赖查找，同时又有`Autowire`的能力。实际上`ConfigurableListableBeanFactory`的默认实现就是`DefaultListableBeanFactory`。

![这里写图片描述](https://img-blog.csdn.net/20180626183130774?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTM0MTI3NzI=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

**show me the code:**

```java
public class HierarchicalDependencyLookupDemo {

  public static void main(String[] args) {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
    applicationContext.register(HierarchicalDependencyLookupDemo.class);
    applicationContext.refresh();
    ConfigurableBeanFactory configurableBeanFactory = applicationContext.getBeanFactory();
    System.out.println("父容器：" + configurableBeanFactory.getParentBeanFactory());

    HierarchicalBeanFactory parentBeanFactory = createBeanFactory();
    configurableBeanFactory.setParentBeanFactory(parentBeanFactory);

    displayContainsLocalBean(configurableBeanFactory, "person");
    displayContainsLocalBean(parentBeanFactory, "student");

    displayContainsBean(configurableBeanFactory, "student");
    displayContainsBean(parentBeanFactory, "person");

    applicationContext.close();
  }

  private static void displayContainsBean(HierarchicalBeanFactory beanFactory, String beanName) {
    System.out.printf("当前 BeanFactory[%s] 是否包含 Bean[name : %s] : %s\n", beanFactory, beanName,
        containsBean(beanFactory, beanName));
  }

  private static boolean containsBean(HierarchicalBeanFactory beanFactory, String beanName) {
    BeanFactory parentBeanFactory = beanFactory.getParentBeanFactory();
    if (parentBeanFactory instanceof HierarchicalBeanFactory) {
      HierarchicalBeanFactory parentHierarchicalBeanFactory = HierarchicalBeanFactory.class.cast(parentBeanFactory);
      if (containsBean(parentHierarchicalBeanFactory, beanName)) {
        return true;
      }
    }
    return beanFactory.containsLocalBean(beanName);
  }

  private static void displayContainsLocalBean(HierarchicalBeanFactory beanFactory, String beanName) {
    System.out.printf("当前 BeanFactory[%s] 是否包含 Local Bean[name : %s] : %s\n", beanFactory, beanName,
        beanFactory.containsLocalBean(beanName));
  }

  private static HierarchicalBeanFactory createBeanFactory(){
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
    applicationContext.register(Config.class);
    applicationContext.refresh();
    return applicationContext.getBeanFactory();
  }

  @Bean
  public Person student() {
    Student p = new Student();
    p.setName("student");
    p.setAge(26);
    p.setScore(95);
    return p;
  }

  public static class Config{
    @Bean
    public Person person(){
      Person person = new Person();
      person.setAge(25);
      person.setName("person");
      return person;
    }
  }

  private static class Person {

    private String name;
    private int age;

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

    @Override
    public String toString() {
      return "Person{" +
          "name='" + name + '\'' +
          ", age=" + age +
          '}';
    }
  }

  private static class Student extends Person {

    private int score;

    public int getScore() {
      return score;
    }

    public void setScore(int score) {
      this.score = score;
    }

    @Override
    public String toString() {
      return "Student{" +
          "name='" + getName() + '\'' +
          ", age=" + getAge() +
          ", score=" + getScore() +
          '}';
    }
  }

}
```

## 延迟依赖查找

`ObjectFactory和ObjectProvider`

ObjectProvider接口是ObjectFactory接口的扩展，专门为注入点设计的，可以让注入变得更加宽松和更具有可选项。

那么什么时候使用ObjectProvider接口？

如果待注入参数的Bean为空或有多个时，便是ObjectProvider发挥作用的时候了。

如果注入实例为空时，使用ObjectProvider则避免了强依赖导致的依赖对象不存在异常；如果有多个实例，ObjectProvider的方法会根据Bean实现的Ordered接口或@Order注解指定的先后顺序获取一个Bean。从而了提供了一个更加宽松的依赖注入方式。

Spring 5.1之后提供了基于Stream的orderedStream方法来获取有序的Stream的方法。

**show me the code**

```java
public static void main(String[] args) {
    // 创建 BeanFactory 容器
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
    // 将当前类 ObjectProviderDemo 作为配置类（Configuration Class）
    applicationContext.register(LazyDependencyLookupDemo.class);
    // 启动应用上下文
    applicationContext.refresh();
    // 依赖查找集合对象
    lookupByObjectProvider(applicationContext);
    lookupIfAvailable(applicationContext);
    lookupByStreamOps(applicationContext);

    // 关闭应用上下文
    applicationContext.close();

  }

  private static void lookupByStreamOps(AnnotationConfigApplicationContext applicationContext) {
    ObjectProvider<String> objectProvider = applicationContext.getBeanProvider(String.class);
//        Iterable<String> stringIterable = objectProvider;
//        for (String string : stringIterable) {
//            System.out.println(string);
//        }
    // Stream -> Method reference
    objectProvider.stream().forEach(System.out::println);
  }

  private static void lookupIfAvailable(AnnotationConfigApplicationContext applicationContext) {
    ObjectProvider<User> userObjectProvider = applicationContext.getBeanProvider(User.class);
    User user = userObjectProvider.getIfAvailable(User::getInstance);
    System.out.println("当前 User 对象：" + user);
  }

  @Bean
  @Primary
  public String helloWorld() { // 方法名就是 Bean 名称 = "helloWorld"
    return "Hello,World";
  }

  @Bean
  public String message() {
    return "Message";
  }

  private static void lookupByObjectProvider(
      AnnotationConfigApplicationContext applicationContext) {
    ObjectProvider<String> objectProvider = applicationContext.getBeanProvider(String.class);
    System.out.println(objectProvider.getObject());
  }
```

## 安全依赖查找和依赖查找中的常见异常

**依赖查找的安全性**：

| 依赖查找类型 | 代表实现                             | 是否安全 |
| ------------ | ------------------------------------ | -------- |
| 单一类型查找 | `BeanFactory#getBean`                | 否       |
|              | `ObjectFactory#getObject`            | 否       |
|              | `ObjectProvider#getIfAvailable`      | 是       |
|              |                                      |          |
| 集合类型查找 | `ListableBeanFactory#getBeansOfType` | 是       |
|              | `ObjectProvider#stream`              | 是       |

**常见的异常**：

| 异常类型                          | 触发条件                                   | 场景举例                     |
| --------------------------------- | ------------------------------------------ | ---------------------------- |
| `NoSuchBeanDefinitionException`   | 当查找 Bean 不存在于 IoC 容器时            | `BeanFactory#getBean`        |
| `NoUniqueBeanDefinitionException` | 类型依赖查找时，IoC 容器存在多个 Bean 实例 | `BeanFactory#getBean(Class)` |
| `BeanInstantiationException`      | 当 Bean 所对应的类型非具体类时             | `BeanFactory#getBean`        |
| `BeanCreationException`           | 当 Bean 初始化过程中                       | Bean 初始化方法执行异常      |
| `BeanDefinitionStoreException`    | 当 BeanDefinition 配置元信息非法           | XML 配置资源无法打开         |

**show me the code**

1. 演示依赖查找的安全性：

   ```java
   public class TypeSafetyDependencyLookupDemo {
   
     public static void main(String[] args) {
       AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
       applicationContext.register(TypeSafetyDependencyLookupDemo.class);
       applicationContext.refresh();
   
       // 演示 BeanFactory#getBean 方法的安全性
       displayBeanFactoryGetBean(applicationContext);
       // 演示 ObjectFactory#getObject 方法的安全性
       displayObjectFactoryGetObject(applicationContext);
       // 演示 ObjectProvider#getIfAvaiable 方法的安全性
       displayObjectProviderIfAvailable(applicationContext);
   
       // 演示 ListableBeanFactory#getBeansOfType 方法的安全性
       displayListableBeanFactoryGetBeansOfType(applicationContext);
       // 演示 ObjectProvider Stream 操作的安全性
       displayObjectProviderStreamOps(applicationContext);
   
       applicationContext.close();
     }
   
     private static void displayObjectProviderStreamOps(AnnotationConfigApplicationContext applicationContext) {
       ObjectProvider<User> userObjectProvider = applicationContext.getBeanProvider(User.class);
       printBeansException("displayObjectProviderStreamOps", () -> userObjectProvider.forEach(System.out::println));
     }
   
     private static void displayListableBeanFactoryGetBeansOfType(ListableBeanFactory beanFactory) {
       printBeansException("displayListableBeanFactoryGetBeansOfType", () -> beanFactory.getBeansOfType(User.class));
     }
   
     private static void displayObjectProviderIfAvailable(AnnotationConfigApplicationContext applicationContext) {
       ObjectProvider<User> userObjectProvider = applicationContext.getBeanProvider(User.class);
       printBeansException("displayObjectProviderIfAvailable", () -> userObjectProvider.getIfAvailable());
     }
   
     private static void displayObjectFactoryGetObject(AnnotationConfigApplicationContext applicationContext) {
       // ObjectProvider is ObjectFactory
       ObjectFactory<User> userObjectFactory = applicationContext.getBeanProvider(User.class);
       printBeansException("displayObjectFactoryGetObject", () -> userObjectFactory.getObject());
     }
   
     public static void displayBeanFactoryGetBean(BeanFactory beanFactory) {
       printBeansException("displayBeanFactoryGetBean", () -> beanFactory.getBean(User.class));
     }
   
     private static void printBeansException(String source, Runnable runnable) {
       System.err.println("==========================================");
       System.err.println("Source from :" + source);
       try {
         runnable.run();
       } catch (BeansException exception) {
         exception.printStackTrace();
       }
     }
   }
   ```

2. 演示依赖查找常见的异常：

   ```java
   public class ExceptionDependencyLookupDemo {
   
     public static void main(String[] args) {
       // 演示BeanInstantiationException
       displayBeanInstantiationException();
       // 演示BeanCreateException
       displayBeanCreationException();
       // 演示NoSuchBeanDefinitionException
       displayNoSuchBeanDefinitionException();
       // 演示NoUniqueBeanDefinitionException
       displayNoUniqueBeanDefinitionException();
       // 演示BeanDefinitionStoreException
       displayBeanDefinitionStoreException();
     }
   
     /**
      * {@link org.springframework.beans.factory.NoSuchBeanDefinitionException} 示例
      */
     private static void displayNoSuchBeanDefinitionException() {
       printBeansException("演示NoSuchBeanDefinitionException", () -> {
         AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
         applicationContext.register(ExceptionDependencyLookupDemo.class);
         applicationContext.getBean("user");
         applicationContext.refresh();
         applicationContext.close();
       });
   
     }
   
     /**
      * {@link org.springframework.beans.factory.NoUniqueBeanDefinitionException}示例
      */
     private static void displayNoUniqueBeanDefinitionException() {
       printBeansException("演示NoUniqueBeanDefinitionException",
           () -> {
             AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
             applicationContext.register(ExceptionDependencyLookupDemo.class);
             applicationContext.getBean(String.class);
             applicationContext.refresh();
             applicationContext.close();
           });
     }
   
     /**
      * {@link org.springframework.beans.factory.BeanInitializationException}示例
      */
     private static void displayBeanInstantiationException() {
       printBeansException("演示BeanInitializationException", () -> {
         AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
         BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
             .genericBeanDefinition(CharSequence.class);
         applicationContext
             .registerBeanDefinition("errorBean", beanDefinitionBuilder.getBeanDefinition());
         applicationContext.refresh();
         applicationContext.close();
       });
   
     }
   
     /**
      * {@link org.springframework.beans.factory.BeanCreationException}示例
      */
     private static void displayBeanCreationException() {
       printBeansException("演示BeanCreationException", () -> {
         AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
         BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
             .genericBeanDefinition(BeanCreateExceptionDemo.class);
         applicationContext
             .registerBeanDefinition("errorBean", beanDefinitionBuilder.getBeanDefinition());
         applicationContext.refresh();
         applicationContext.close();
       });
   
     }
   
     /**
      * {@link org.springframework.beans.factory.BeanDefinitionStoreException}示例
      * 实际上有很多原因导致BeanDefinitionStoreException <br/>
      * 1. FileNotFoundException
      * 2. Could Not Resolve Placeholder
      * 3. java.lang.NoSuchMethodError
      * 4. java.lang.NoClassDefFoundError
      *  具体可以参考<a>https://www.baeldung.com/spring-beandefinitionstoreexception</a>
      */
     private static void displayBeanDefinitionStoreException() {
       printBeansException("演示BeanDefinitionStoreException：", () -> {
         new ClassPathXmlApplicationContext("beans.xml");
       });
     }
   
     private static void printBeansException(String source, Runnable runnable) {
       System.err.println("==========================================");
       System.err.println("Source from :" + source);
       try {
         runnable.run();
       } catch (Exception e) {
         e.printStackTrace();
       }
     }
   
     @Bean
     public String string1() {
       return "string1";
     }
   
     @Bean
     public String string2() {
       return "string2";
     }
   
     @Bean
     public String string3() {
       return "string3";
     }
   
     static class BeanCreateExceptionDemo implements InitializingBean {
   
       @PostConstruct
       public void init() throws Throwable {
         throw new Throwable("init() : For purposes...");
       }
   
       @Override
       public void afterPropertiesSet() throws Exception {
         throw new Exception("afterPropertiesSet() : For purposes...");
       }
     }
   }
   ```

## Spring内建可查找的依赖

**AbstractApplicationContext 内建可查找的依赖：**

| Bean名称                    | Bean实例                        | 使用场景                |
| --------------------------- | ------------------------------- | ----------------------- |
| environment                 | Environment 对象                | 外部化配置以及 Profiles |
| systemProperties            | java.util.Properties对象        | java系统属性            |
| systemEnvironment           | java.util.Map对象               | 操作系统环境变量        |
| messageSource               | MessageSource对象               | 国际化文案              |
| lifecycleProcessor          | LifecycleProcessor对象          | Lifecycle Bean处理器    |
| applicationEventMulticaster | ApplicationEventMulticaster对象 | Spring 事件广播器       |

**注解驱动 Spring 应用上下文内建可查找的依赖：**

| Bean名称                                                     | Bean实例                                 | 使用场景                                                |
| ------------------------------------------------------------ | ---------------------------------------- | ------------------------------------------------------- |
| org.springframework.context.annotation.<br />internalConfigurationAnnotationProcessor | `ConfigurationClassPostProcessor`        | 处理spring的配置类                                      |
| org.springframework.context.annotation.<br />internalAutowiredAnnotationProcessor | `AutowiredAnnotationBeanPostProcessor`   | 处理`@Autowire@Value`注解                               |
| org.springframework.context.annotation.<br />internalCommonAnnotationProcessor | `CommonAnnotationBeanPostProcessor`      | （条件激活）处理` JSR-250 `注解，如 `@PostConstruct` 等 |
| org.springframework.context.annotation.<br />internalEventListenerProcessor | `EventListenerMethodProcessor`           | 处理标注 `@EventListener` 的Spring 事件监听方法         |
| org.springframework.context.annotation.<br />internalEventListenerFactory | `DefaultEventListenerFactory`            | `@EventListener`事件监听方法适配为`ApplicationListener` |
| org.springframework.context.annotation.<br />internalPresistenceAnnotationProcessor | `PresistenceAnnotationBeanPostProcessor` | （条件激活）处理 JPA 注解场景                           |

**show me the code**

```java
public class SpringBuiltInBeanDemo {

  public static void main(String[] args) {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
    applicationContext.register(SpringBuiltInBeanDemo.class);
    applicationContext.refresh();
    displayBeanTypes(applicationContext);
    applicationContext.close();
  }
  
  private static void displayBeanTypes(AnnotationConfigApplicationContext applicationContext){
    ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
    Iterator<String> beanNamesIterator = beanFactory.getBeanNamesIterator();
    while (beanNamesIterator.hasNext()){
      String beanName = beanNamesIterator.next();
      System.out.println(beanName + " = " + beanFactory.getBean(beanName));
    }
  }
}
```

