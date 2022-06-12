[toc]

## Spring配置元信息

### Spring Bean 定义元信息

* GenericBeanDefinition:通用型 BeanDefinition
  * GenericBeanDefinition一站式BeanDefinition
  * GenericBeanDefinition继承了AbstractBeanDefinition
  * GenericBeanDefinition主要新增的属性是parentName，其余均继承于AbstractBeanDefinition
* RootBeanDefinition：无 Parent 的 BeanDefinition 或者合并后 BeanDefinition
  * GenericBeanDefinition无parentName属性会被克隆成RootBeanDefinition
  * GenericBeanDefinition有parentName属性经过合并后变成RootBeanDefinition
  * RootBeanDefinition提供了很多缓存作用的字段
* AnnotatedBeanDefinition:注解标注的 BeanDefinition
  * 扩展了BeanDefinition
  * AnnotationMetadata
  * MethodMetadata

代码入口：`org.springframework.beans.factory.xml.BeanDefinitionParser#parse`

### Spring Bean 属性元信息

* Bean 属性元信息 - PropertyValues
  * 可修改实现 - MutablePropertyValues
  * 元素成员 - PropertyValue
* Bean 属性上下文存储 - AttributeAccessor
* Bean 元信息元素 - BeanMetadataElement

```java
    // BeanDefinition 的定义（声明）
    BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(User.class);
    beanDefinitionBuilder.addPropertyValue("name", "Shawn");
    // 获取 AbstractBeanDefinition
    AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
    // 附加属性（不影响 Bean populate、initialize）
    beanDefinition.setAttribute("name", "shawn");
    // 当前 BeanDefinition 来自于何方（辅助作用）
    beanDefinition.setSource(BeanConfigurationMetadataDemo.class);

    DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

    beanFactory.addBeanPostProcessor(new BeanPostProcessor() {
      @Override
      public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (ObjectUtils.nullSafeEquals("user", beanName) && User.class.equals(bean.getClass())) {
          BeanDefinition bd = beanFactory.getBeanDefinition(beanName);
          if (BeanConfigurationMetadataDemo.class.equals(bd.getSource())) { // 通过 source 判断来
            // 属性（存储）上下文
            String name = (String) bd.getAttribute("name");
            User user = (User) bean;
            user.setName(name);
          }
        }
        return bean;
      }
    });

    // 注册 User 的 BeanDefinition
    beanFactory.registerBeanDefinition("user", beanDefinition);

    User user = beanFactory.getBean("user", User.class);

    System.out.println(user);
```

### Spring 容器配置元信息

#### xml-beans

| beans 元素属性              | 默认值     | 使用场景                                                     |
| --------------------------- | ---------- | ------------------------------------------------------------ |
| profile                     | null(留空) | Spring Profiles 配置值                                       |
| default-lazy-init           | default    | 当 outter beans “default-lazy-init” 属 性存在时，继承该值，否则为“false” |
| default-merge               | default    | 当 outter beans “default-merge” 属性存在时，继承该值，否则为“false” |
| default-autowire            | default    | 当 outter beans “default-autowire” 属性存在时，继承该值，否则为“no” |
| default-autowire-candidates | null(留空) | 默认 Spring Beans 名称 pattern                               |
| default-init-method         | null(留空) | 默认 Spring Beans 自定义初始化方法                           |
| default-destroy-method      | null(留空) | 默认 Spring Beans 自定义销毁方法                             |

入口：org.springframework.beans.factory.xml.BeanDefinitionParserDelegate#populateDefaults

```java
public static final String DEFAULT_LAZY_INIT_ATTRIBUTE = "default-lazy-init";

public static final String DEFAULT_MERGE_ATTRIBUTE = "default-merge";

public static final String DEFAULT_AUTOWIRE_ATTRIBUTE = "default-autowire";

public static final String DEFAULT_AUTOWIRE_CANDIDATES_ATTRIBUTE = "default-autowire-candidates";

public static final String DEFAULT_INIT_METHOD_ATTRIBUTE = "default-init-method";

public static final String DEFAULT_DESTROY_METHOD_ATTRIBUTE = "default-destroy-method";

/**
* 可能beans嵌套beans，子beans找不到默认值就去parent beans里找。
*/
protected void populateDefaults(DocumentDefaultsDefinition defaults, @Nullable DocumentDefaultsDefinition parentDefaults, Element root) {
		String lazyInit = root.getAttribute(DEFAULT_LAZY_INIT_ATTRIBUTE);
		if (isDefaultValue(lazyInit)) {
			// Potentially inherited from outer <beans> sections, otherwise falling back to false.
			lazyInit = (parentDefaults != null ? parentDefaults.getLazyInit() : FALSE_VALUE);
		}
		defaults.setLazyInit(lazyInit);

		String merge = root.getAttribute(DEFAULT_MERGE_ATTRIBUTE);
		if (isDefaultValue(merge)) {
			// Potentially inherited from outer <beans> sections, otherwise falling back to false.
			merge = (parentDefaults != null ? parentDefaults.getMerge() : FALSE_VALUE);
		}
		defaults.setMerge(merge);

		String autowire = root.getAttribute(DEFAULT_AUTOWIRE_ATTRIBUTE);
		if (isDefaultValue(autowire)) {
			// Potentially inherited from outer <beans> sections, otherwise falling back to 'no'.
			autowire = (parentDefaults != null ? parentDefaults.getAutowire() : AUTOWIRE_NO_VALUE);
		}
		defaults.setAutowire(autowire);

		if (root.hasAttribute(DEFAULT_AUTOWIRE_CANDIDATES_ATTRIBUTE)) {
			defaults.setAutowireCandidates(root.getAttribute(DEFAULT_AUTOWIRE_CANDIDATES_ATTRIBUTE));
		}
		else if (parentDefaults != null) {
			defaults.setAutowireCandidates(parentDefaults.getAutowireCandidates());
		}

		if (root.hasAttribute(DEFAULT_INIT_METHOD_ATTRIBUTE)) {
			defaults.setInitMethod(root.getAttribute(DEFAULT_INIT_METHOD_ATTRIBUTE));
		}
		else if (parentDefaults != null) {
			defaults.setInitMethod(parentDefaults.getInitMethod());
		}

		if (root.hasAttribute(DEFAULT_DESTROY_METHOD_ATTRIBUTE)) {
			defaults.setDestroyMethod(root.getAttribute(DEFAULT_DESTROY_METHOD_ATTRIBUTE));
		}
		else if (parentDefaults != null) {
			defaults.setDestroyMethod(parentDefaults.getDestroyMethod());
		}

		defaults.setSource(this.readerContext.extractSource(root));
	}
```

#### Xml-context

| xml元素                          | 使用场景                               |
| -------------------------------- | -------------------------------------- |
| <context:annotation-config />    | 激活 Spring 注解驱动                   |
| <context:component-scan />       | Spring @Component 以及自定义注解扫描   |
| <context:load-time-weaver />     | 激活 Spring LoadTimeWeaver             |
| <context:mbean-export />         | 暴露 Spring Beans 作为 JMX Beans       |
| <context:mbean-server />         | 将当前平台作为 MBeanServer             |
| <context:property-placeholder /> | 加载外部化配置资源作为 Spring 属性配置 |
| <context:property-override />    | 利用外部化配置资源覆盖 Spring 属性值   |

源代码入口：`org.springframework.context.config.ContextNamespaceHandler`

* `org.springframework.context.annotation.AnnotationConfigBeanDefinitionParser`：负责解析`<context:annotation-config />`
* `org.springframework.context.annotation.ComponentScanBeanDefinitionParser`:负责解析`<context:component-scan />`
* `org.springframework.context.config.LoadTimeWeaverBeanDefinitionParser`:负责解析`<context:load-time-weaver />`
* `org.springframework.context.config.MBeanExportBeanDefinitionParser`负责解析`<context:mbean-export />`
* `org.springframework.context.config.MBeanServerBeanDefinitionParser`负责解析`<context:mbean-server />`
* `org.springframework.context.config.PropertyPlaceholderBeanDefinitionParser`负责解析`<context:property-placeholder />`
* `org.springframework.context.config.PropertyOverrideBeanDefinitionParser`负责解析`<context:property-override />`

#### Xml-util

| xml元素                | 使用场景                                                     |
| ---------------------- | ------------------------------------------------------------ |
| <util:list />          | \<bean id="" class = ""\>\<property\>\<util:list />\</property\>\</bean\> |
| <util:set />           | \<bean id="" class = ""\>\<property\>\<util:set />\</property\>\</bean\> |
| <util:map />           | \<bean id="" class = ""\>\<property\>\<util:map />\</property\>\</bean\> |
| <util:properties />    | \<bean id="" class = ""\>\<property\>\<util:properties />\</property\>\</bean\> |
| <util:constant />      | \<bean id="" class = ""\>\<property\>\<util:constant />\</property\>\</bean\> |
| <util:property-path /> | \<bean id="" class = ""\>\<property\>\<util:property-path />\</property\>\</bean\> |

源代码入口：`org.springframework.beans.factory.xml.UtilNamespaceHandler`

* `org.springframework.beans.factory.xml.UtilNamespaceHandler.ListBeanDefinitionParser`负责解析`<util:list />`
* `org.springframework.beans.factory.xml.UtilNamespaceHandler.SetBeanDefinitionParser`负责解析`<util:set />`
* `org.springframework.beans.factory.xml.UtilNamespaceHandler.MapBeanDefinitionParser`负责解析`<util:map />`
* `org.springframework.beans.factory.xml.UtilNamespaceHandler.PropertiesBeanDefinitionParser`负责解析`<util:properties />`
* `org.springframework.beans.factory.xml.UtilNamespaceHandler.ConstantBeanDefinitionParser`负责解析`<util:constant />`
* `org.springframework.beans.factory.xml.UtilNamespaceHandler.PropertyPathBeanDefinitionParser`负责解析`<util:property-path>`

对应的FactoryBean

```java
import org.springframework.beans.factory.config.FieldRetrievingFactoryBean;
import org.springframework.beans.factory.config.ListFactoryBean;
import org.springframework.beans.factory.config.MapFactoryBean;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.beans.factory.config.PropertyPathFactoryBean;
import org.springframework.beans.factory.config.SetFactoryBean;
```

### 基于 XML 资源装载 Spring Bean 配置元信息

#### Spring Bean 配置元信息

| xml元素               | 使用场景                                       |
| --------------------- | ---------------------------------------------- |
| <beans:beans />       | 单 XML 资源下的多个 Spring Beans 配置          |
| <beans:bean />        | 单个 Spring Bean 定义(BeanDefinition)配置      |
| <beans:alias />       | 为 Spring Bean 定义(BeanDefinition)映射别名    |
| <beans:import />      | 加载外部 Spring XML 配置资源                   |
| <beans:description /> | 为Spring Bean 定义(BeanDefinition)添加描述信息 |

#### 底层实现

源代码入口:`org.springframework.beans.factory.xml.XmlBeanDefinitionReader`

* 资源 - Resource
* 底层 - BeanDefinitionDocumentReader
  * XML解析-Java DOM Level 3 API
  * BeanDefinition 解析 - BeanDefinitionParserDelegate 
  * BeanDefinition 注册 - BeanDefinitionRegistry



### 基于 Properties 资源装载 Spring Bean 配置元信息

#### Spring Bean配置元信息

| properties属性名 | 使用场景                        |
| ---------------- | ------------------------------- |
| (class)          | Bean 类全称限定名               |
| (abstract)       | 是否为抽象的 BeanDefinition     |
| (parent)         | 指定 parent BeanDefinition 名称 |
| (lazy-init)      | 是否为延迟初始化                |
| (ref)            | 引用其他 Bean 的名称            |
| (scope)          | 设置 Bean 的 scope 属性         |
| ${n}             | n 表示第 n+1 个构造器参数       |

#### 底层实现

源代码入口：`org.springframework.beans.factory.support.PropertiesBeanDefinitionReader`

* 资源

  * 字节流 - Resource
  *  字符流 - EncodedResouce

* 底层

  * 存储 - java.util.Properties

  * BeanDefinition 解析 - API 内部实现

  * BeanDefinition 注册 - BeanDefinitionRegistry

### 基于 Java Annotation 资源装载 Spring Bean 配置元信息

#### Spring Bean模式注解

| Spring注解     | 使用场景           | 起始版本 |
| -------------- | ------------------ | -------- |
| @Repository    | 数据仓储模式注解   | 2.0      |
| @Component     | 通用组件模式注解   | 2.5      |
| @Service       | 服务模式注解       | 2.5      |
| @Controller    | Web 控制器模式注解 | 2.5      |
| @Configuration | 配置类模式注解     | 3.0      |

#### Spring Bean定义注解

| Spring注解 | 使用场景                                          | 起始版本 |
| ---------- | ------------------------------------------------- | -------- |
| @Bean      | 替换 XML 元素 \<bean\>                            | 3.0      |
| @DependsOn | 替代 XML 属性 \<bean depends-on="..."/\>          | 3.0      |
| @Lazy      | 替代 XML 属性 \<bean lazy-init="true\|falses" /\> | 3.0      |
| @Primary   | 替换 XML 元素 \<bean primary="true\|false" /\>    | 3.0      |
| @Role      | 替换 XML 元素 \<bean role="..." /\>               | 3.1      |
| @Lookup    | 替代 XML 属性 \<bean lookup-method="..."\>        | 4.1      |

#### Spring Bean依赖注入注解

| Spring注解 | 使用场景                            | 起始版本 |
| ---------- | ----------------------------------- | -------- |
| @Autowired | Bean 依赖注入，支持多种依赖查找方式 | 2.5      |
| @Qualifier | 细粒度的 @Autowired 依赖查找        | 2.5      |

| Java 注解 | 场景说明          | 起始版本 |
| --------- | ----------------- | -------- |
| @Resource | 类似于 @Autowired | 2.5      |
| @Inject   | 类似于 @Autowired | 2.5      |

#### Spring Bean条件装配注解

| Spring 注解  | 场景说明       | 起始版本 |
| ------------ | -------------- | -------- |
| @Profile     | 配置化条件装配 | 3.1      |
| @Conditional | 编程条件装配   | 4.0      |

#### Spring Bean生命周期回调注解

| Spring 注解    | 场景说明                                                     | 起始版本 |
| -------------- | ------------------------------------------------------------ | -------- |
| @PostConstruct | 替换 XML 元素\<bean init-method="..." \> 或 InitializingBean | 2.5      |
| @PreDestroy    | 替换 XML 元素 \<bean destroy-method="..." /\> 或 DisposableBean | 2.5      |

#### 底层实现

源代码入口`org.springframework.context.annotation.AnnotatedBeanDefinitionReader`

* 资源
  * 类对象 - java.lang.Class

* 底层
  * 条件评估 - ConditionEvaluator
  * Bean 范围解析 - ScopeMetadataResolver
  * BeanDefinition 解析 - 内部 API 实现
  * BeanDefinition 处理 - AnnotationConfigUtils.processCommonDefinitionAnnotations
  * BeanDefinition 注册 - BeanDefinitionRegistry

## Spring IoC 容器配置元信息

### 基于 XML 资源装载 Spring IoC 容器配置元信息

| 命名空间 | 所属模块       | Schema 资源 URL                                              |
| -------- | -------------- | ------------------------------------------------------------ |
| beans    | spring-beans   | https://www.springframework.org/schema/beans/spring-beans.xsd |
| context  | spring-context | https://www.springframework.org/schema/context/spring-context.xsd |
| aop      | spring-aop     | https://www.springframework.org/schema/aop/spring-aop.xsd    |
| tx       | spring-tx      | https://www.springframework.org/schema/tx/spring-tx.xsd      |
| util     | spring-beans   | https://www.springframework.org/schema/util/spring-util.xsd  |
| tool     | spring-beans   | https://www.springframework.org/schema/tool/spring-tool.xsd  |

### 基于 Java 注解装载 Spring IoC 容器配置元信息

#### Spring IoC容器装配注解

| Spring 注解     | 场景说明                                    | 起始版本 |
| --------------- | ------------------------------------------- | -------- |
| @ImportResource | 替换 XML 元素 \<import\>                    | 3.0      |
| @Import         | 导入 Configuration Class                    | 3.0      |
| @ComponentScan  | 扫描指定 package 下标注 Spring 模式注解的类 | 3.1      |

#### Spring IoC配置属性注解

| Spring 注解      | 场景说明                         | 起始版本 |
| ---------------- | -------------------------------- | -------- |
| @PropertySource  | 配置属性抽象 PropertySource 注解 | 3.1      |
| @PropertySources | @PropertySource 集合注解         | 4.0      |

## Spring 外部化配置元信息

### 基于 Properties 资源装载外部化配置

* 注解驱动
  * `@org.springframework.context.annotation.PropertySource`
  * `@org.springframework.context.annotation.PropertySources` 
* API 编程
  * `org.springframework.core.env.PropertySource`
  * `org.springframework.core.env.PropertySources`

### 基于 YAML 资源装载外部化配置

* API 编程
  * `org.springframework.beans.factory.config.YamlProcessor`
  * `org.springframework.beans.factory.config.YamlMapFactoryBean`
  * `org.springframework.beans.factory.config.YamlPropertiesFactoryBean`

## Spring Profile 元信息

## Extensible XML authoring

### Spring XML 扩展

* 编写 XML Schema 文件:定义 XML 结构
* 自定义 NamespaceHandler 实现:命名空间绑定
* 自定义 BeanDefinitionParser 实现:XML 元素与 BeanDefinition 解析
* 注册 XML 扩展:命名空间与 XML Schema 映射

### 触发时机

* AbstractApplicationContext#obtainFreshBeanFactory
  * AbstractRefreshableApplicationContext#refreshBeanFactory
    * AbstractXmlApplicationContext#loadBeanDefinitions
      * ...
        * XmlBeanDefinitionReader#doLoadBeanDefinitions
          * ...
            * BeanDefinitionParserDelegate#parseCustomElement

### 核心流程

BeanDefinitionParserDelegate#parseCustomElement(org.w3c.dom.Element, BeanDefinition)

* 获取 namespace
* 通过 namespace 解析 NamespaceHandler
* 构造 ParserContext
* 解析元素，获取 BeanDefinintion