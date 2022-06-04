package com.shawn.study.deep.in.spring.core.ioc.bean.dependency.lookup.exception;

import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

public class NoUniqueBeanDefinitionExceptionDemo {

  public static void main(String[] args) {
    AnnotationConfigApplicationContext applicationContext =
        new AnnotationConfigApplicationContext();
    applicationContext.register(NoUniqueBeanDefinitionExceptionDemo.class);
    applicationContext.refresh();

    try {
      applicationContext.getBean(String.class);
    } catch (NoUniqueBeanDefinitionException e) {
      System.err.printf(
          " Spring 应用上下文存在%d个 %s 类型的 Bean，具体原因：%s%n",
          e.getNumberOfBeansFound(), String.class.getName(), e.getMessage());
    }

    applicationContext.close();
  }

  @Bean
  public String bean1() {
    return "1";
  }

  @Bean
  public String bean2() {
    return "2";
  }

  @Bean
  public String bean3() {
    return "3";
  }
}
