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

源代码入口:`org.springframework.beans.factory.xml.XmlBeanDefinitionReader#doLoadBeanDefinitions`

* 资源 - Resource
* 底层 - `org.springframework.beans.factory.xml.BeanDefinitionDocumentReader#registerBeanDefinitions`
  * XML解析-Java DOM Level 3 API
  * BeanDefinition 解析 - BeanDefinitionParserDelegate，具体解析的逻辑
  * BeanDefinition 注册 - BeanDefinitionRegistry
  * 默认实现`org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader`

```java
protected void doRegisterBeanDefinitions(Element root) {
		BeanDefinitionParserDelegate parent = this.delegate;
		this.delegate = createDelegate(getReaderContext(), root, parent);

		if (this.delegate.isDefaultNamespace(root)) {
			String profileSpec = root.getAttribute(PROFILE_ATTRIBUTE);
			if (StringUtils.hasText(profileSpec)) {
				String[] specifiedProfiles = StringUtils.tokenizeToStringArray(
						profileSpec, BeanDefinitionParserDelegate.MULTI_VALUE_ATTRIBUTE_DELIMITERS);
				if (!getReaderContext().getEnvironment().acceptsProfiles(specifiedProfiles)) {
					if (logger.isDebugEnabled()) {
						logger.debug("Skipped XML bean definition file due to specified profiles [" + profileSpec +
								"] not matching: " + getReaderContext().getResource());
					}
					return;
				}
			}
		}

  	// 前置处理xml
		preProcessXml(root);
    // 解析xml
		parseBeanDefinitions(root, this.delegate);
    // 后置处理xml
		postProcessXml(root);

		this.delegate = parent;
	}
```

源码：`org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader#parseBeanDefinitions`

```java
protected void parseBeanDefinitions(Element root, BeanDefinitionParserDelegate delegate) {
 if (delegate.isDefaultNamespace(root)) {
  NodeList nl = root.getChildNodes();
  for (int i = 0; i < nl.getLength(); i++) {
   Node node = nl.item(i);
   if (node instanceof Element) {
    Element ele = (Element) node;
    if (delegate.isDefaultNamespace(ele)) {
     // 解析beans, bean, import, alias
     parseDefaultElement(ele, delegate);
    }
    else {
     // 处理自定义元素，例如context,util,tx等spring内置的自定义注解，也可以是自己扩展了xml元素Extensible XML authoring
     delegate.parseCustomElement(ele);
    }
   }
  }
 }
 else {
  // 处理自定义元素，例如context,util,tx等spring内置的自定义注解，也可以是自己扩展了xml元素Extensible XML authoring
  delegate.parseCustomElement(root);
 }
}
```

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

源代码入口：`org.springframework.beans.factory.support.PropertiesBeanDefinitionReader#loadBeanDefinitions`

* 资源

  * 字节流 - Resource
  *  字符流 - EncodedResouce

* 底层

  * 存储 - java.util.Properties

  * BeanDefinition 解析 - API 内部实现

  * BeanDefinition 注册 - BeanDefinitionRegistry

```java
// org.springframework.beans.factory.support.PropertiesBeanDefinitionReader#registerBeanDefinitions
public int registerBeanDefinitions(Map<?, ?> map, @Nullable String prefix, String resourceDescription)
			throws BeansException {

		if (prefix == null) {
			prefix = "";
		}
		int beanCount = 0;

		for (Object key : map.keySet()) {
			if (!(key instanceof String)) {
				throw new IllegalArgumentException("Illegal key [" + key + "]: only Strings allowed");
			}
			String keyString = (String) key;
			if (keyString.startsWith(prefix)) {
				// Key is of form: prefix<name>.property
				String nameAndProperty = keyString.substring(prefix.length());
				// Find dot before property name, ignoring dots in property keys.
				int sepIdx = -1;
				int propKeyIdx = nameAndProperty.indexOf(PropertyAccessor.PROPERTY_KEY_PREFIX);
				if (propKeyIdx != -1) {
					sepIdx = nameAndProperty.lastIndexOf(SEPARATOR, propKeyIdx);
				}
				else {
					sepIdx = nameAndProperty.lastIndexOf(SEPARATOR);
				}
				if (sepIdx != -1) {
          // 生成beanName的逻辑
					String beanName = nameAndProperty.substring(0, sepIdx);
					if (logger.isTraceEnabled()) {
						logger.trace("Found bean name '" + beanName + "'");
					}
					if (!getRegistry().containsBeanDefinition(beanName)) {
						// If we haven't already registered it...
						registerBeanDefinition(beanName, map, prefix + beanName, resourceDescription);
						++beanCount;
					}
				}
				// 省略代码
			}
		}

		return beanCount;
	}

protected void registerBeanDefinition(String beanName, Map<?, ?> map, String prefix, String resourceDescription)
			throws BeansException {

		String className = null;
		String parent = null;
		String scope = BeanDefinition.SCOPE_SINGLETON;
		boolean isAbstract = false;
		boolean lazyInit = false;

		ConstructorArgumentValues cas = new ConstructorArgumentValues();
		MutablePropertyValues pvs = new MutablePropertyValues();

		for (Map.Entry<?, ?> entry : map.entrySet()) {
			String key = StringUtils.trimWhitespace((String) entry.getKey());
			if (key.startsWith(prefix + SEPARATOR)) {
				String property = key.substring(prefix.length() + SEPARATOR.length());
				if (CLASS_KEY.equals(property)) {
					className = StringUtils.trimWhitespace((String) entry.getValue());
				}
				else if (PARENT_KEY.equals(property)) {
					parent = StringUtils.trimWhitespace((String) entry.getValue());
				}
				else if (ABSTRACT_KEY.equals(property)) {
					String val = StringUtils.trimWhitespace((String) entry.getValue());
					isAbstract = TRUE_VALUE.equals(val);
				}
				else if (SCOPE_KEY.equals(property)) {
					// Spring 2.0 style
					scope = StringUtils.trimWhitespace((String) entry.getValue());
				}
				else if (SINGLETON_KEY.equals(property)) {
					// Spring 1.2 style
					String val = StringUtils.trimWhitespace((String) entry.getValue());
					scope = ("".equals(val) || TRUE_VALUE.equals(val) ? BeanDefinition.SCOPE_SINGLETON :
							BeanDefinition.SCOPE_PROTOTYPE);
				}
				else if (LAZY_INIT_KEY.equals(property)) {
					String val = StringUtils.trimWhitespace((String) entry.getValue());
					lazyInit = TRUE_VALUE.equals(val);
				}
				else if (property.startsWith(CONSTRUCTOR_ARG_PREFIX)) {
					if (property.endsWith(REF_SUFFIX)) {
						int index = Integer.parseInt(property.substring(1, property.length() - REF_SUFFIX.length()));
						cas.addIndexedArgumentValue(index, new RuntimeBeanReference(entry.getValue().toString()));
					}
					else {
						int index = Integer.parseInt(property.substring(1));
						cas.addIndexedArgumentValue(index, readValue(entry));
					}
				}
				else if (property.endsWith(REF_SUFFIX)) {
					// This isn't a real property, but a reference to another prototype
					// Extract property name: property is of form dog(ref)
					property = property.substring(0, property.length() - REF_SUFFIX.length());
					String ref = StringUtils.trimWhitespace((String) entry.getValue());

					// It doesn't matter if the referenced bean hasn't yet been registered:
					// this will ensure that the reference is resolved at runtime.
					Object val = new RuntimeBeanReference(ref);
					pvs.add(property, val);
				}
				else {
					// It's a normal bean property.
					pvs.add(property, readValue(entry));
				}
			}
		}

		if (logger.isTraceEnabled()) {
			logger.trace("Registering bean definition for bean name '" + beanName + "' with " + pvs);
		}

		// Just use default parent if we're not dealing with the parent itself,
		// and if there's no class name specified. The latter has to happen for
		// backwards compatibility reasons.
		if (parent == null && className == null && !beanName.equals(this.defaultParentBean)) {
			parent = this.defaultParentBean;
		}

		try {
			AbstractBeanDefinition bd = BeanDefinitionReaderUtils.createBeanDefinition(
					parent, className, getBeanClassLoader());
			bd.setScope(scope);
			bd.setAbstract(isAbstract);
			bd.setLazyInit(lazyInit);
			bd.setConstructorArgumentValues(cas);
			bd.setPropertyValues(pvs);
			getRegistry().registerBeanDefinition(beanName, bd);
		}
		catch (ClassNotFoundException ex) {
			throw new CannotLoadBeanClassException(resourceDescription, beanName, className, ex);
		}
		catch (LinkageError err) {
			throw new CannotLoadBeanClassException(resourceDescription, beanName, className, err);
		}
	}
```



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

```java
private <T> void doRegisterBean(Class<T> beanClass, @Nullable String name,
			@Nullable Class<? extends Annotation>[] qualifiers, @Nullable Supplier<T> supplier,
			@Nullable BeanDefinitionCustomizer[] customizers) {

		AnnotatedGenericBeanDefinition abd = new AnnotatedGenericBeanDefinition(beanClass);
		if (this.conditionEvaluator.shouldSkip(abd.getMetadata())) {
			return;
		}

		abd.setInstanceSupplier(supplier);
  	
		ScopeMetadata scopeMetadata = this.scopeMetadataResolver.resolveScopeMetadata(abd);
		abd.setScope(scopeMetadata.getScopeName());
  	// bean name生成逻辑
		String beanName = (name != null ? name : this.beanNameGenerator.generateBeanName(abd, this.registry));
		// BeanDefinition处理
		AnnotationConfigUtils.processCommonDefinitionAnnotations(abd);
		if (qualifiers != null) {
			for (Class<? extends Annotation> qualifier : qualifiers) {
				if (Primary.class == qualifier) {
					abd.setPrimary(true);
				}
				else if (Lazy.class == qualifier) {
					abd.setLazyInit(true);
				}
				else {
					abd.addQualifier(new AutowireCandidateQualifier(qualifier));
				}
			}
		}
		if (customizers != null) {
			for (BeanDefinitionCustomizer customizer : customizers) {
				customizer.customize(abd);
			}
		}

		BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(abd, beanName);
  	// Bean Scope解析
		definitionHolder = AnnotationConfigUtils.applyScopedProxyMode(scopeMetadata, definitionHolder, this.registry);
    // 注册BeanDefinition -> DefaultListableBeanFactory
		BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, this.registry);
	}
```



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

```java
// 代码入口org.springframework.context.annotation.ConfigurationClassParser#doProcessConfigurationClass
protected final SourceClass doProcessConfigurationClass(ConfigurationClass configClass, SourceClass sourceClass)
			throws IOException {

		if (configClass.getMetadata().isAnnotated(Component.class.getName())) {
			// Recursively process any member (nested) classes first
			processMemberClasses(configClass, sourceClass);
		}

		// Process any @PropertySource annotations
		for (AnnotationAttributes propertySource : AnnotationConfigUtils.attributesForRepeatable(
				sourceClass.getMetadata(), PropertySources.class,
				org.springframework.context.annotation.PropertySource.class)) {
			if (this.environment instanceof ConfigurableEnvironment) {
				processPropertySource(propertySource);
			}
			else {
				logger.info("Ignoring @PropertySource annotation on [" + sourceClass.getMetadata().getClassName() +
						"]. Reason: Environment must implement ConfigurableEnvironment");
			}
		}

		// Process any @ComponentScan annotations
		Set<AnnotationAttributes> componentScans = AnnotationConfigUtils.attributesForRepeatable(
				sourceClass.getMetadata(), ComponentScans.class, ComponentScan.class);
		if (!componentScans.isEmpty() &&
				!this.conditionEvaluator.shouldSkip(sourceClass.getMetadata(), ConfigurationPhase.REGISTER_BEAN)) {
			for (AnnotationAttributes componentScan : componentScans) {
				// The config class is annotated with @ComponentScan -> perform the scan immediately
				Set<BeanDefinitionHolder> scannedBeanDefinitions =
						this.componentScanParser.parse(componentScan, sourceClass.getMetadata().getClassName());
				// Check the set of scanned definitions for any further config classes and parse recursively if needed
				for (BeanDefinitionHolder holder : scannedBeanDefinitions) {
					BeanDefinition bdCand = holder.getBeanDefinition().getOriginatingBeanDefinition();
					if (bdCand == null) {
						bdCand = holder.getBeanDefinition();
					}
					if (ConfigurationClassUtils.checkConfigurationClassCandidate(bdCand, this.metadataReaderFactory)) {
						parse(bdCand.getBeanClassName(), holder.getBeanName());
					}
				}
			}
		}

		// Process any @Import annotations
		processImports(configClass, sourceClass, getImports(sourceClass), true);

		// Process any @ImportResource annotations
		AnnotationAttributes importResource =
				AnnotationConfigUtils.attributesFor(sourceClass.getMetadata(), ImportResource.class);
		if (importResource != null) {
			String[] resources = importResource.getStringArray("locations");
			Class<? extends BeanDefinitionReader> readerClass = importResource.getClass("reader");
			for (String resource : resources) {
				String resolvedResource = this.environment.resolveRequiredPlaceholders(resource);
				configClass.addImportedResource(resolvedResource, readerClass);
			}
		}

		// Process individual @Bean methods
		Set<MethodMetadata> beanMethods = retrieveBeanMethodMetadata(sourceClass);
		for (MethodMetadata methodMetadata : beanMethods) {
			configClass.addBeanMethod(new BeanMethod(methodMetadata, configClass));
		}

		// Process default methods on interfaces
		processInterfaces(configClass, sourceClass);

		// Process superclass, if any
		if (sourceClass.getMetadata().hasSuperClass()) {
			String superclass = sourceClass.getMetadata().getSuperClassName();
			if (superclass != null && !superclass.startsWith("java") &&
					!this.knownSuperclasses.containsKey(superclass)) {
				this.knownSuperclasses.put(superclass, configClass);
				// Superclass found, return its annotation metadata and recurse
				return sourceClass.getSuperClass();
			}
		}

		// No superclass -> processing is complete
		return null;
	}
```

## Spring 外部化配置元信息

### 基于 Properties 资源装载外部化配置

* 注解驱动
  * `@org.springframework.context.annotation.PropertySource`
  * `@org.springframework.context.annotation.PropertySources` 
* API 编程
  * `org.springframework.core.env.PropertySource`
  * `org.springframework.core.env.PropertySources`

```java
@Bean
  public User user(
      @Value("${user.id}") String id,
      @Value("${user.name}") String name,
      @Value("${user.age}") int age,
      @Value("${user.address}") String address) {
    User user = new User();
    user.setId(id);
    user.setName(name);
    user.setAge(age);
    user.setAddress(address);
    return user;
  }

  public static void main(String[] args) {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

    // 扩展 Environment 中的 PropertySources
    // 添加 PropertySource 操作必须在 refresh 方法之前完成
    Map<String, Object> propertiesSource = new HashMap<>();
    propertiesSource.put("user.name", "shawn");
    org.springframework.core.env.PropertySource propertySource =
        new MapPropertySource("first-property-source", propertiesSource);
    context.getEnvironment().getPropertySources().addFirst(propertySource);

    // 注册当前类作为 Configuration Class
    context.register(PropertySourceDemo.class);
    // 启动 Spring 应用上下文
    context.refresh();
    // beanName 和 bean 映射
    Map<String, User> usersMap = context.getBeansOfType(User.class);
    for (Map.Entry<String, User> entry : usersMap.entrySet()) {
      System.out.printf("User Bean name : %s , content : %s \n", entry.getKey(), entry.getValue());
    }
    System.out.println(context.getEnvironment().getPropertySources());
    // 关闭 Spring 应用上下文
    context.close();
  }
```

### 基于 YAML 资源装载外部化配置

* API 编程
  * `org.springframework.beans.factory.config.YamlProcessor`
  * `org.springframework.beans.factory.config.YamlMapFactoryBean`
  * `org.springframework.beans.factory.config.YamlPropertiesFactoryBean`

1. 创建yaml文件

   ```yaml
   user:
     id: user-yaml
     name: shawn
     age: 26
     address: shanghai
   ```

2. 继承PropertySourceFactory重写createPropertySource方法

   ```java
   public class YamlPropertySourceFactory implements PropertySourceFactory {
     @Override
     public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
       YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
       yamlPropertiesFactoryBean.setResources(resource.getResource());
       Properties yamlProperties = yamlPropertiesFactoryBean.getObject();
       return new PropertiesPropertySource(name, yamlProperties);
     }
   }
   ```

3. 具体实现类

   ```java
   @PropertySource(
       name = "yamlPropertySource",
       value = "classpath:/META-INF/user.yaml",
       factory = YamlPropertySourceFactory.class)
   public class AnnotatedYamlPropertySourceDemo {
   
     @Bean
     public User user(
         @Value("${user.id}") String id,
         @Value("${user.name}") String name,
         @Value("${user.age}") int age,
         @Value("${user.address}") String address) {
       User user = new User();
       user.setId(id);
       user.setName(name);
       user.setAge(age);
       user.setAddress(address);
       return user;
     }
   
     public static void main(String[] args) {
       AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
       context.register(AnnotatedYamlPropertySourceDemo.class);
       context.refresh();
       User user = context.getBean("user", User.class);
       System.out.println(user);
       context.close();
     }
   }
   ```

4. 源码入口：`org.springframework.context.annotation.ConfigurationClassParser#processPropertySource`

   ```java
   private void processPropertySource(AnnotationAttributes propertySource) throws IOException {
   		String name = propertySource.getString("name");
   		if (!StringUtils.hasLength(name)) {
   			name = null;
   		}
   		String encoding = propertySource.getString("encoding");
   		if (!StringUtils.hasLength(encoding)) {
   			encoding = null;
   		}
   		String[] locations = propertySource.getStringArray("value");
   		Assert.isTrue(locations.length > 0, "At least one @PropertySource(value) location is required");
   		boolean ignoreResourceNotFound = propertySource.getBoolean("ignoreResourceNotFound");
   
   		Class<? extends PropertySourceFactory> factoryClass = propertySource.getClass("factory");
   		PropertySourceFactory factory = (factoryClass == PropertySourceFactory.class ?
   				DEFAULT_PROPERTY_SOURCE_FACTORY : BeanUtils.instantiateClass(factoryClass));
   
   		for (String location : locations) {
   			try {
   				String resolvedLocation = this.environment.resolveRequiredPlaceholders(location);
   				Resource resource = this.resourceLoader.getResource(resolvedLocation);
   				addPropertySource(factory.createPropertySource(name, new EncodedResource(resource, encoding)));
   			}
   			catch (IllegalArgumentException | FileNotFoundException | UnknownHostException ex) {
   				// Placeholders not resolvable or resource not found when trying to open it
   				if (ignoreResourceNotFound) {
   					if (logger.isInfoEnabled()) {
   						logger.info("Properties location [" + location + "] not resolvable: " + ex.getMessage());
   					}
   				}
   				else {
   					throw ex;
   				}
   			}
   		}
   	}
   ```

## Extensible XML authoring

### Spring XML 扩展

* 编写 XML Schema 文件:定义 XML 结构

  * 主要编写xsd文件，需要熟练xsd编写格式
  * 路径需要与NamespaceHandler实现类一致
    * `org.springframework.beans.factory.xml.DefaultNamespaceHandlerResolver#DEFAULT_HANDLER_MAPPINGS_LOCATION`

* 自定义 NamespaceHandler 实现:命名空间绑定

  * 继承`org.springframework.beans.factory.xml.NamespaceHandlerSupport` - 实现init方法
  * 注册BeanDefinitionParser-`org.springframework.beans.factory.xml.NamespaceHandlerSupport#registerBeanDefinitionParser`
  * 定义 namespace 与 NamespaceHandler 的映射 - 在`META-INF/spring.handlers`文件里编写映射规则，需要注意的是路径和文件名必须是`META-INF/spring.handlers`。
  * 具体参考实现 - spring-context.xsd的实现 
    * `org.springframework.context.config.ContextNamespaceHandler`
    * `org/springframework/spring-context/5.2.2.RELEASE/spring-context-5.2.2.RELEASE.jar!/META-INF/spring.handlers`

* 自定义 BeanDefinitionParser 实现:XML 元素与 BeanDefinition 解析

  * 继承`org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser`-实现`getBeanClass`和`doParse`方法

* 注册 XML 扩展:命名空间与 XML Schema 映射

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <beans
    xmlns="http://www.springframework.org/schema/beans"
    xmlns:users="http://com.shawn.deep.in.spring.core/schema/users"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
          https://www.springframework.org/schema/beans/spring-beans.xsd
          http://com.shawn.deep.in.spring.core/schema/users
          http://com.shawn.deep.in.spring.core/schema/users.xsd">
  
  
    <users:user id="user-custom-element" name="shawn" age="27" address="shanghai"/>
  
  </beans>
  ```

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

```java
	public BeanDefinition parseCustomElement(Element ele, @Nullable BeanDefinition containingBd) {
		String namespaceUri = getNamespaceURI(ele);
		if (namespaceUri == null) {
			return null;
		}
		NamespaceHandler handler = this.readerContext.getNamespaceHandlerResolver().resolve(namespaceUri);
		if (handler == null) {
			error("Unable to locate Spring NamespaceHandler for XML schema namespace [" + namespaceUri + "]", ele);
			return null;
		}
		return handler.parse(ele, new ParserContext(this.readerContext, this, containingBd));
	}
```

### 缺点

* 高复杂度:开发人员需要熟悉 XML Schema，spring.handlers，spring.schemas 以 及 Spring API
* 嵌套元素支持较弱:通常需要使用方法递归或者其嵌套解析的方式处理嵌套(子)元素
* XML 处理性能较差:Spring XML 基于 DOM Level 3 API 实现，该 API 便于理解，然而性能较差
* XML 框架移植性差:很难适配高性能和便利性的 XML 框架，如 JAXB