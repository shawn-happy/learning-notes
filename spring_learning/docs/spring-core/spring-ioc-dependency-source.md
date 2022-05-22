## 依赖查找的来源

| 来源                       | 配置元数据                                                   |
| -------------------------- | ------------------------------------------------------------ |
| `Spring BeanDefinition`    | `<bean id="person" class="com.shawn.study.spring.ioc....Person">` |
|                            | `@Bean public Person person(){ return new Person();}`        |
|                            | `BeanDefinitionBuilder`                                      |
| 单例对象                   | API实现                                                      |
| spring内建的BeanDefinition |                                                              |
| Spring内建的单例对象       |                                                              |

### Spring内建可查找的依赖

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

## 依赖注入的来源

| 来源                    | 配置元数据                                                   |
| ----------------------- | ------------------------------------------------------------ |
| `Spring BeanDefinition` | `<bean id="person" class="com.shawn.study.spring.ioc....Person">` |
|                         | `@Bean public Person person(){ return new Person();}`        |
|                         | `BeanDefinitionBuilder`                                      |
| 单例对象                | API实现                                                      |
| 非spring容器管理对象    |                                                              |

## Spring容器管理和游离对象

| 来源                    | srping Bean对象 | 生命周期管理 | 配置元信息 | 使用场景           |
| ----------------------- | --------------- | ------------ | ---------- | ------------------ |
| `Spring BeanDefinition` | √               | √            | √          | 依赖查找，依赖注入 |
| 单例对象                | √               | ×            | ×          | 依赖查找，依赖注入 |
| `Resolvable Dependency` | ×               | ×            | ×          | 依赖注入           |

## Spring BeanDefinition作为依赖来源

* 元数据：`BeanDefinition`
* 注册：`BeanDefinitionRegistry#registerBeanDefinition`
* 类型：延迟和非延迟
* 顺序：Bean生命周期顺序按照注册顺序

## 单例对象作为依赖来源

* 来源：外部普通java对象(不一定是pojo)
* 注册：SingletonBeanRegistry#registerSingleton
* 无生命周期管理
* 无法实现延迟初始化bean。

## 非spring容器管理对象作为依赖来源

* 注册：`ConfigurableListableBeanFactory#registerResolvableDependency`
* 无生命周期管理
* 无法实现延迟初始化bean
* 无法通过依赖查找

## 外部配置作为依赖来源

* 类型：非常规spring对象依赖来源
* 无生命周期管理
* 无法实现延迟初始化bean
* 无法通过依赖查找