package com.shawn.study.deep.in.spring.core.bean.dependency.source;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;

/** 外部配置作为依赖注入来源 */
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
