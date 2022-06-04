package com.shawn.study.deep.in.spring.core.ioc.bean.dependency.lookup.exception;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanDefinitionStoreExceptionDemo {

  public static void main(String[] args) {
    new ClassPathXmlApplicationContext("classpath:/beans.xml");
  }
}
