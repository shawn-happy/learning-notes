[toc]

## Java标准资源管理

### Java 标准资源定位

| 职责         | 说明                                                         |
| ------------ | ------------------------------------------------------------ |
| 面向资源     | 文件系统、artifact(jar、war、ear 文件)以及远程资源(HTTP、FTP 等) |
| API 整合     | java.lang.ClassLoader#getResource、java.io.File 或 java.net.URL |
| 资源定位     | java.net.URL 或 java.net.URI                                 |
| 面向流式存储 | java.net.URLConnection                                       |
| 协议扩展     | java.net.URLStreamHandler 或 java.net.URLStreamHandlerFactory |

### Java URL 协议扩展

#### 基于 `java.net.URLStreamHandlerFactory`

![spring-resource-java-resource](./images/spring-resource-java-resource.png)

#### 基于 `java.net.URLStreamHandler`

##### JDK 1.8 內建协议实现

| 协议   | 实现类                                |
| ------ | ------------------------------------- |
| file   | `sun.net.www.protocol.file.Handler`   |
| ftp    | `sun.net.www.protocol.ftp.Handler`    |
| http   | `sun.net.www.protocol.http.Handler`   |
| https  | `sun.net.www.protocol.https.Handler`  |
| jar    | `sun.net.www.protocol.jar.Handler`    |
| mailto | `sun.net.www.protocol.mailto.Handler` |
| netdoc | `sun.net.www.protocol.netdoc.Handler` |

##### 实现类名必须为 “Handler”

| 实现类命名规则 | 说明                                                         |
| -------------- | ------------------------------------------------------------ |
| 默认           | `sun.net.www.protocol.${protocol}.Handler`                   |
| 自定义         | 通过 Java Properties java.protocol.handler.pkgs 指定实现类包名， 实现类名必须为“Handler”。如果存在多包名指定，通过分隔符 “\|” |

## Spring资源管理

### Spring Resource接口

| 类型       | 接口                                                  |
| ---------- | ----------------------------------------------------- |
| 输入流     | `org.springframework.core.io.InputStreamSource`       |
| 只读资源   | `org.springframework.core.io.Resource`                |
| 可写资源   | `org.springframework.core.io.WritableResource`        |
| 编码资源   | `org.springframework.core.io.support.EncodedResource` |
| 上下文资源 | `org.springframework.core.io.ContextResource`         |

### 内建实现

| 资源来源       | 资源协议       | 实现类                                                       |
| -------------- | -------------- | ------------------------------------------------------------ |
| Bean 定义      | 无             | `org.springframework.beans.factory.support.BeanDefinitionResource` |
| 数组           | 无             | `org.springframework.core.io.ByteArrayResource`              |
| 类路径         | `classpath:/`  | `org.springframework.core.io.ClassPathResource`              |
| 文件系统       | `file:/`       | `org.springframework.core.io.FileSystemResource`             |
| URL            | URL 支持的协议 | `org.springframework.core.io.UrlResource`                    |
| ServletContext | 无             | `org.springframework.web.context.support.ServletContextResource` |

### 可写资源接口

* `org.springframework.core.io.WritableResource`
  * `org.springframework.core.io.FileSystemResource`
  * `org.springframework.core.io.FileUrlResource`(@since 5.0.2)
  * `org.springframework.core.io.PathResource`(@since 4.0 & @Deprecated)

### 编码资源接口

* `org.springframework.core.io.support.EncodedResource`

### Spring 资源加载器

* `org.springframework.core.io.ResourceLoader`
  * `org.springframework.core.io.DefaultResourceLoader`
    * `org.springframework.core.io.FileSystemResourceLoader`
    * `org.springframework.core.io.ClassRelativeResourceLoader`
    * `org.springframework.context.support.AbstractApplicationContext`

### Spring 通配路径资源加载器

#### 通配路径 ResourceLoader

* `org.springframework.core.io.support.ResourcePatternResolver`
  * `org.springframework.core.io.support.PathMatchingResourcePatternResolver`

#### 路径匹配器

* `org.springframework.util.PathMatcher`
  * Ant 模式匹配实现 - `org.springframework.util.AntPathMatcher`

#### 扩展通配路径资源

* 实现`org.springframework.util.PathMatcher`
* 重置 `PathMatcher`
  * `PathMatchingResourcePatternResolver#setPathMatcher`

### 依赖注入Spring Resource

* 基于 `@Value` 实现

```java
@Value(“classpath:/...”)
private Resource resource;
```

### 依赖注入ResourceLoader

* 实现 ResourceLoaderAware 回调
* @Autowired 注入 ResourceLoader
* 注入 ApplicationContext 作为 ResourceLoader
