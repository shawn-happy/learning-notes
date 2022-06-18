## Java 国际化标准实现

* 抽象实现 - `java.util.ResourceBundle`
  * Properties 资源实现 - `java.util.PropertyResourceBundle`  
  * 例举实现 - `java.util.ListResourceBundle`
* ResourceBundle核心特性
  * Key-Value 设计
  * 层次性设计
  * 缓存设计
  * 字符编码控制 - `java.util.ResourceBundle.Control(@since 1.6)`
  * Control SPI 扩展 - `java.util.spi.ResourceBundleControlProvider(@since 1.8)`

## Java 文本格式化

* 核心接口 - `java.text.MessageFormat`
* 基本用法
  * 设置消息格式模式- `new MessageFormat(...)`
  * 格式化 - `format(new Object[]{...})`
* 消息格式模式
  * 格式元素:`{ArgumentIndex (,FormatType,(FormatStyle))}`
  * FormatType:消息格式类型，可选项，每种类型在 number、date、time 和 choice 类型选其一
  * FormatStyle:消息格式风格，可选项，包括:short、medium、long、full、integer、currency、percent
* 高级特性
  * 重置消息格式模式
  * 重置 `java.util.Locale`
  * 重置 `java.text.Format`

## Spring 国际化接口

* `org.springframework.context.MessageSource`
  * 文案模板编码(code)
  * 文案模板参数(args)
  * 区域(Locale)
* `org.springframework.context.HierarchicalMessageSource`

### 具体实现

* 基于 ResourceBundle + MessageFormat 组合 MessageSource 实现 `org.springframework.context.support.ResourceBundleMessageSource`

* 可重载 Properties + MessageFormat 组合 MessageSource 实现`org.springframework.context.support.ReloadableResourceBundleMessageSource`

* `org.springframework.context.support.StaticMessageSource`
* `org.springframework.context.support.DelegatingMessageSource`

### 内建依赖

MessageSource 內建 Bean 可能来源

* 预注册 Bean 名称为:“messageSource”，类型为:MessageSource Bean

* 默认內建实现 - DelegatingMessageSource
  * 层次性查找 MessageSource 对象

### Spring boot的国际化实现

Spring Boot 为什么要新建 MessageSource Bean?

* AbstractApplicationContext 的实现决定了 MessageSource 內建实现 
* Spring Boot 通过外部化配置简化 MessageSource Bean 构建
* Spring Boot 基于 Bean Validation 校验非常普遍

### 如何实现配置自动更新 MessageSource

* Java NIO 2:`java.nio.file.WatchService`

* Java Concurrency : `java.util.concurrent.ExecutorService`

* Spring:`org.springframework.context.support.AbstractMessageSource`
