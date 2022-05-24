package com.shawn.study.deep.in.spring.core.bean.dependency.lookup.exception;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class NoSuchBeanDefinitionExceptionDemo {

  public static void main(String[] args) {
    AnnotationConfigApplicationContext beanFactory = new AnnotationConfigApplicationContext();
    beanFactory.refresh();

    beanFactory.getBean("user");
    beanFactory.close();
  }
}
