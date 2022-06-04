package com.shawn.study.deep.in.spring.core.ioc.bean.dependency.lookup.exception;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BeanInstantiationExceptionDemo {

  public static void main(String[] args) {
    AnnotationConfigApplicationContext applicationContext =
        new AnnotationConfigApplicationContext();

    BeanDefinitionBuilder beanDefinitionBuilder =
        BeanDefinitionBuilder.genericBeanDefinition(CharSequence.class);
    applicationContext.registerBeanDefinition(
        "errorBean", beanDefinitionBuilder.getBeanDefinition());

    applicationContext.refresh();

    applicationContext.close();
  }
}
