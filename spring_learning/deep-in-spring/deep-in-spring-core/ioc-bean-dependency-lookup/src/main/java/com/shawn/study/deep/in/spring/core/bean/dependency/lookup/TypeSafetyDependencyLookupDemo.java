package com.shawn.study.deep.in.spring.core.bean.dependency.lookup;

import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TypeSafetyDependencyLookupDemo {
  public static void main(String[] args) {
    AnnotationConfigApplicationContext applicationContext =
        new AnnotationConfigApplicationContext();
    applicationContext.register(TypeSafetyDependencyLookupDemo.class);
    applicationContext.refresh();

    // 演示 BeanFactory#getBean 方法的安全性
    displayBeanFactoryGetBean(applicationContext);
    // 演示 ObjectFactory#getObject 方法的安全性
    displayObjectFactoryGetObject(applicationContext);
    // 演示 ObjectProvider#getIfAvaiable 方法的安全性
    displayObjectProviderIfAvailable(applicationContext);

    // 演示 ListableBeanFactory#getBeansOfType 方法的安全性
    displayListableBeanFactoryGetBeansOfType(applicationContext);
    // 演示 ObjectProvider Stream 操作的安全性
    displayObjectProviderStreamOps(applicationContext);

    // 关闭应用上下文
    applicationContext.close();
  }

  private static void displayObjectProviderStreamOps(
      AnnotationConfigApplicationContext applicationContext) {
    ObjectProvider<User> userObjectProvider = applicationContext.getBeanProvider(User.class);
    printBeansException(
        "displayObjectProviderStreamOps", () -> userObjectProvider.forEach(System.out::println));
  }

  private static void displayListableBeanFactoryGetBeansOfType(ListableBeanFactory beanFactory) {
    printBeansException(
        "displayListableBeanFactoryGetBeansOfType", () -> beanFactory.getBeansOfType(User.class));
  }

  private static void displayObjectProviderIfAvailable(
      AnnotationConfigApplicationContext applicationContext) {
    ObjectProvider<User> userObjectProvider = applicationContext.getBeanProvider(User.class);
    printBeansException(
        "displayObjectProviderIfAvailable", () -> userObjectProvider.getIfAvailable());
  }

  private static void displayObjectFactoryGetObject(
      AnnotationConfigApplicationContext applicationContext) {
    // ObjectProvider is ObjectFactory
    ObjectFactory<User> userObjectFactory = applicationContext.getBeanProvider(User.class);
    printBeansException("displayObjectFactoryGetObject", () -> userObjectFactory.getObject());
  }

  public static void displayBeanFactoryGetBean(BeanFactory beanFactory) {
    printBeansException("displayBeanFactoryGetBean", () -> beanFactory.getBean(User.class));
  }

  private static void printBeansException(String source, Runnable runnable) {
    System.err.println("==========================================");
    System.err.println("Source from :" + source);
    try {
      runnable.run();
    } catch (BeansException exception) {
      exception.printStackTrace();
    }
  }
}
