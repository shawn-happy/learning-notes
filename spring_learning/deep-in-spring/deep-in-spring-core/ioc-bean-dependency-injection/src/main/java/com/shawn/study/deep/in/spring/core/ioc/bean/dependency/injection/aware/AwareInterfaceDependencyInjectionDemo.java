package com.shawn.study.deep.in.spring.core.ioc.bean.dependency.injection.aware;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AwareInterfaceDependencyInjectionDemo
    implements BeanFactoryAware, ApplicationContextAware {

  private static BeanFactory beanFactory;
  private static ApplicationContext applicationContext;

  public static void main(String[] args) {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    context.register(AwareInterfaceDependencyInjectionDemo.class);
    context.refresh();
    System.out.println(
        "beanFactory == context.getBeanFactory()?  ->   "
            + (beanFactory == context.getBeanFactory()));
    System.out.println("applicationContext == context?  ->   " + (context == applicationContext));
    context.close();
  }

  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    AwareInterfaceDependencyInjectionDemo.beanFactory = beanFactory;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    AwareInterfaceDependencyInjectionDemo.applicationContext = applicationContext;
  }
}
