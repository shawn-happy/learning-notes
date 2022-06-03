package com.shawn.study.deep.in.spring.core.bean.dependency.source;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/** Resolvable Dependency */
public class ResolvableDependencySourceDemo {

  @Autowired private String value;

  @PostConstruct
  public void init() {
    System.out.println(value);
  }

  public static void main(String[] args) {

    AnnotationConfigApplicationContext applicationContext =
        new AnnotationConfigApplicationContext();

    applicationContext.register(ResolvableDependencySourceDemo.class);

    applicationContext.addBeanFactoryPostProcessor(
        beanFactory -> {
          // 注册 Resolvable Dependency
          beanFactory.registerResolvableDependency(String.class, "Hello,World");
        });

    applicationContext.refresh();

    applicationContext.close();
  }
}
