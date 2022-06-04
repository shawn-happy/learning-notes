## 依赖查找的来源

### 配置BeanDefinition

* `<bean id="person" class="com.shawn.study.spring.ioc....Person">`
* `@Bean public Person person(){ return new Person();}`
* `BeanDefinitionBuilder`

### 注册SingletonBean

* `org.springframework.beans.factory.support.DefaultSingletonBeanRegistry#registerSingleton`

### Spring内建SingletonBean

| Bean名称                    | Bean实例                        | 使用场景                |
| --------------------------- | ------------------------------- | ----------------------- |
| environment                 | Environment 对象                | 外部化配置以及 Profiles |
| systemProperties            | java.util.Properties对象        | java系统属性            |
| systemEnvironment           | java.util.Map对象               | 操作系统环境变量        |
| messageSource               | MessageSource对象               | 国际化文案              |
| lifecycleProcessor          | LifecycleProcessor对象          | Lifecycle Bean处理器    |
| applicationEventMulticaster | ApplicationEventMulticaster对象 | Spring 事件广播器       |

源代码来源：

1. `org.springframework.context.support.AbstractApplicationContext#prepareBeanFactory`

```java
protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		//...
		// Register default environment beans.
		if (!beanFactory.containsLocalBean(ENVIRONMENT_BEAN_NAME)) {
			beanFactory.registerSingleton(ENVIRONMENT_BEAN_NAME, getEnvironment());
		}
		if (!beanFactory.containsLocalBean(SYSTEM_PROPERTIES_BEAN_NAME)) {
			beanFactory.registerSingleton(SYSTEM_PROPERTIES_BEAN_NAME, getEnvironment().getSystemProperties());
		}
		if (!beanFactory.containsLocalBean(SYSTEM_ENVIRONMENT_BEAN_NAME)) {
			beanFactory.registerSingleton(SYSTEM_ENVIRONMENT_BEAN_NAME, getEnvironment().getSystemEnvironment());
		}
	}
```

2. `org.springframework.context.support.AbstractApplicationContext#initMessageSource`
3. `org.springframework.context.support.AbstractApplicationContext#initApplicationEventMulticaster`
4. `org.springframework.context.support.AbstractApplicationContext#initLifecycleProcessor`

```java
protected void initMessageSource() {
		// ...
		else {
			// Use empty MessageSource to be able to accept getMessage calls.
			DelegatingMessageSource dms = new DelegatingMessageSource();
			dms.setParentMessageSource(getInternalParentMessageSource());
			this.messageSource = dms;
			beanFactory.registerSingleton(MESSAGE_SOURCE_BEAN_NAME, this.messageSource);
			if (logger.isTraceEnabled()) {
				logger.trace("No '" + MESSAGE_SOURCE_BEAN_NAME + "' bean, using [" + this.messageSource + "]");
			}
		}
	}

	protected void initApplicationEventMulticaster() {
		// ...
		else {
			this.applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
			beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, this.applicationEventMulticaster);
			if (logger.isTraceEnabled()) {
				logger.trace("No '" + APPLICATION_EVENT_MULTICASTER_BEAN_NAME + "' bean, using " +
						"[" + this.applicationEventMulticaster.getClass().getSimpleName() + "]");
			}
		}
	}

	protected void initLifecycleProcessor() {
		// ...
		else {
			DefaultLifecycleProcessor defaultProcessor = new DefaultLifecycleProcessor();
			defaultProcessor.setBeanFactory(beanFactory);
			this.lifecycleProcessor = defaultProcessor;
			beanFactory.registerSingleton(LIFECYCLE_PROCESSOR_BEAN_NAME, this.lifecycleProcessor);
			if (logger.isTraceEnabled()) {
				logger.trace("No '" + LIFECYCLE_PROCESSOR_BEAN_NAME + "' bean, using " +
						"[" + this.lifecycleProcessor.getClass().getSimpleName() + "]");
			}
		}
	}
```

### Spring内建BeanDefinition

| Bean名称                                                     | Bean实例                                 | 使用场景                                                |
| ------------------------------------------------------------ | ---------------------------------------- | ------------------------------------------------------- |
| org.springframework.context.annotation.<br />internalConfigurationAnnotationProcessor | `ConfigurationClassPostProcessor`        | 处理spring的配置类                                      |
| org.springframework.context.annotation.<br />internalAutowiredAnnotationProcessor | `AutowiredAnnotationBeanPostProcessor`   | 处理`@Autowire@Value`注解                               |
| org.springframework.context.annotation.<br />internalCommonAnnotationProcessor | `CommonAnnotationBeanPostProcessor`      | （条件激活）处理` JSR-250 `注解，如 `@PostConstruct` 等 |
| org.springframework.context.annotation.<br />internalEventListenerProcessor | `EventListenerMethodProcessor`           | 处理标注 `@EventListener` 的Spring 事件监听方法         |
| org.springframework.context.annotation.<br />internalEventListenerFactory | `DefaultEventListenerFactory`            | `@EventListener`事件监听方法适配为`ApplicationListener` |
| org.springframework.context.annotation.<br />internalPresistenceAnnotationProcessor | `PresistenceAnnotationBeanPostProcessor` | （条件激活）处理 JPA 注解场景                           |

`org.springframework.context.annotation.AnnotationConfigUtils#registerAnnotationConfigProcessors()`

```java
public static Set<BeanDefinitionHolder> registerAnnotationConfigProcessors(
			BeanDefinitionRegistry registry, @Nullable Object source) {
    // ..
    Set<BeanDefinitionHolder> beanDefs = new LinkedHashSet<>(8);
  
		// ConfigurationClassPostProcessor
		if (!registry.containsBeanDefinition(CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME)) {
			RootBeanDefinition def = new RootBeanDefinition(ConfigurationClassPostProcessor.class);
			def.setSource(source);
			beanDefs.add(registerPostProcessor(registry, def, CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME));
		}
		// AutowiredAnnotationBeanPostProcessor
		if (!registry.containsBeanDefinition(AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME)) {
			RootBeanDefinition def = new RootBeanDefinition(AutowiredAnnotationBeanPostProcessor.class);
			def.setSource(source);
			beanDefs.add(registerPostProcessor(registry, def, AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME));
		}

		// Check for JSR-250 support, and if present add the CommonAnnotationBeanPostProcessor.
		if (jsr250Present && !registry.containsBeanDefinition(COMMON_ANNOTATION_PROCESSOR_BEAN_NAME)) {
			RootBeanDefinition def = new RootBeanDefinition(CommonAnnotationBeanPostProcessor.class);
			def.setSource(source);
			beanDefs.add(registerPostProcessor(registry, def, COMMON_ANNOTATION_PROCESSOR_BEAN_NAME));
		}

		// Check for JPA support, and if present add the PersistenceAnnotationBeanPostProcessor.
		if (jpaPresent && !registry.containsBeanDefinition(PERSISTENCE_ANNOTATION_PROCESSOR_BEAN_NAME)) {
			RootBeanDefinition def = new RootBeanDefinition();
			try {
				def.setBeanClass(ClassUtils.forName(PERSISTENCE_ANNOTATION_PROCESSOR_CLASS_NAME,
						AnnotationConfigUtils.class.getClassLoader()));
			}
			catch (ClassNotFoundException ex) {
				throw new IllegalStateException(
						"Cannot load optional framework class: " + PERSISTENCE_ANNOTATION_PROCESSOR_CLASS_NAME, ex);
			}
			def.setSource(source);
			beanDefs.add(registerPostProcessor(registry, def, PERSISTENCE_ANNOTATION_PROCESSOR_BEAN_NAME));
		}

    // EventListenerMethodProcessor
		if (!registry.containsBeanDefinition(EVENT_LISTENER_PROCESSOR_BEAN_NAME)) {
			RootBeanDefinition def = new RootBeanDefinition(EventListenerMethodProcessor.class);
			def.setSource(source);
			beanDefs.add(registerPostProcessor(registry, def, EVENT_LISTENER_PROCESSOR_BEAN_NAME));
		}

    // DefaultEventListenerFactory
		if (!registry.containsBeanDefinition(EVENT_LISTENER_FACTORY_BEAN_NAME)) {
			RootBeanDefinition def = new RootBeanDefinition(DefaultEventListenerFactory.class);
			def.setSource(source);
			beanDefs.add(registerPostProcessor(registry, def, EVENT_LISTENER_FACTORY_BEAN_NAME));
		}

		return beanDefs;
	}
```

## 依赖注入的来源

### 配置BeanDefinition

* `<bean id="person" class="com.shawn.study.spring.ioc....Person">`
* `@Bean public Person person(){ return new Person();}`
* `BeanDefinitionBuilder`

### 注册SingletonBean

* `org.springframework.beans.factory.support.DefaultSingletonBeanRegistry#registerSingleton`

### Spring内建BeanDefinition

同依赖查找

### Spring内建SingletonBean

同依赖查找

### Spring内建ResolvableDependency

`org.springframework.context.support.AbstractApplicationContext#prepareBeanFactory`

```java
protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		// ...
		// BeanFactory interface not registered as resolvable type in a plain factory.
		// MessageSource registered (and found for autowiring) as a bean.
		beanFactory.registerResolvableDependency(BeanFactory.class, beanFactory);
		beanFactory.registerResolvableDependency(ResourceLoader.class, this);
		beanFactory.registerResolvableDependency(ApplicationEventPublisher.class, this);
		beanFactory.registerResolvableDependency(ApplicationContext.class, this);
    // ...
	}
```

### 外部配置

* `@Value`

1. 创建bean.properties配置文件

   ```properties
   user.id = 10
   usr.name = 小明
   user.address = 上海
   user.age = 27
   ```

2. 创建测试类

   ```java
   @Configuration
   @PropertySource(value = "classpath:/bean.properties", encoding = "UTF-8")
   public class ExternalConfigurationDependencySourceDemo {
     @Value("${user.id:-1}")
     private Long id;
   
     // ${user.name}配置优先级
     @Value("${usr.name}")
     private String name;
   
     @Value("${user.address}")
     private String address;
   
     @Value("${user.resource:classpath://default.properties}")
     private Resource resource;
   
     public static void main(String[] args) {
       AnnotationConfigApplicationContext applicationContext =
           new AnnotationConfigApplicationContext();
       applicationContext.register(ExternalConfigurationDependencySourceDemo.class);
   
       applicationContext.refresh();
   
       ExternalConfigurationDependencySourceDemo demo =
           applicationContext.getBean(ExternalConfigurationDependencySourceDemo.class);
   
       System.out.println("demo.id = " + demo.id);
       System.out.println("demo.name = " + demo.name);
       System.out.println("demo.address = " + demo.address);
       System.out.println("demo.resource = " + demo.resource);
   
       applicationContext.close();
     }
   }
   ```

## 依赖来源总结

### Spring容器管理和游离对象

| 来源                    | srping Bean对象 | 生命周期管理 | 配置元信息 | 使用场景           |
| ----------------------- | --------------- | ------------ | ---------- | ------------------ |
| `Spring BeanDefinition` | √               | √            | √          | 依赖查找，依赖注入 |
| 单例对象                | √               | ×            | ×          | 依赖查找，依赖注入 |
| `Resolvable Dependency` | ×               | ×            | ×          | 依赖注入           |

### Spring BeanDefinition作为依赖来源

* 元数据：`BeanDefinition`
* 注册：`BeanDefinitionRegistry#registerBeanDefinition`
* 类型：延迟和非延迟
* 顺序：Bean生命周期顺序按照注册顺序

### 单例对象作为依赖来源

* 来源：外部普通java对象(不一定是pojo)
* 注册：SingletonBeanRegistry#registerSingleton
* 无生命周期管理
* 无法实现延迟初始化bean。

### 非spring容器管理对象作为依赖来源

* 注册：`ConfigurableListableBeanFactory#registerResolvableDependency`
* 无生命周期管理
* 无法实现延迟初始化bean
* 无法通过依赖查找

### 外部配置作为依赖来源

* 类型：非常规spring对象依赖来源
* 无生命周期管理
* 无法实现延迟初始化bean
* 无法通过依赖查找

### 依赖注入和依赖查找的依赖来源区别

* 相同的依赖来源：
  * BeanDefinition(配置，Spring内建)
  * SingletonBean(配置，Spring内建)
* 不同的依赖来源：依赖注入比依赖查找多了两个依赖来源，ResolvableDependency和外部配置。