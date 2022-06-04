package com.shawn.study.deep.in.spring.core.ioc.bean.definition;

import com.shawn.study.deep.in.spring.core.ioc.bean.definition.factory.DefaultUserFactory;
import com.shawn.study.deep.in.spring.core.ioc.bean.definition.factory.UserFactory;
import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import java.util.Iterator;
import java.util.ServiceLoader;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 1. 通过构造器(配置元信息:XML、Java 注解和 Java API ) 2. 通过静态工厂方法(配置元信息:XML 和 Java API ) 3. 通过 Bean
 * 工厂方法(配置元信息:XML和 Java API ) 4. 通过 FactoryBean(配置元信息:XML、Java 注解和 Java API ) 5. 通过
 * ServiceLoaderFactoryBean(配置元信息:XML、Java 注解和 Java API ) 6. 通过
 * AutowireCapableBeanFactory#createBean(java.lang.Class, int, boolean) 7. 通过
 * BeanDefinitionRegistry#registerBeanDefinition(String,BeanDefinition) 参考 {@link
 * BeanDefinitionRegisterDemo#registerBeanDefinition(BeanDefinitionRegistry, String)}
 */
public class BeanInstantiationDemo {

  private ApplicationContext applicationContext;

  public BeanInstantiationDemo() {
    this.applicationContext =
        new ClassPathXmlApplicationContext("classpath:/bean-instantiation.xml");
  }

  /** 通过构造器(配置元信息:XML、Java 注解和 Java API ) */
  public User createUserByConstructor() {
    return applicationContext.getBean("user-by-constructor", User.class);
  }

  /** 通过静态工厂方法(配置元信息:XML 和 Java API ) */
  public User createUserByFactoryMethod() {
    return applicationContext.getBean("user-by-factory-method", User.class);
  }

  /** 通过 Bean 工厂方法(配置元信息:XML和 Java API ) */
  public User createUserByInstanceFactory() {
    return applicationContext.getBean("user-by-factory-class", User.class);
  }

  /** 通过 FactoryBean(配置元信息:XML、Java 注解和 Java API ) */
  public User createUserByFactoryBean() {
    return applicationContext.getBean("user-by-factory-bean", User.class);
  }

  /** 通过 ServiceLoaderFactoryBean(配置元信息:XML、Java 注解和 Java API ) */
  public User createUserByServiceLoader() {
    ServiceLoader<UserFactory> serviceLoader =
        applicationContext.getBean("user-factory-by-serviceLoader", ServiceLoader.class);
    Iterator<UserFactory> iterator = serviceLoader.iterator();
    return iterator.next().createUser();
  }

  /**
   * 通过 AutowireCapableBeanFactory#createBean(java.lang.Class, int, boolean) {@link
   * AutowireCapableBeanFactory#createBean(Class)}
   */
  public User createUserByAutowireCapableBeanFactory() {
    AutowireCapableBeanFactory autowireCapableBeanFactory =
        applicationContext.getAutowireCapableBeanFactory();
    DefaultUserFactory userFactory =
        autowireCapableBeanFactory.createBean(DefaultUserFactory.class);
    return userFactory.createUser();
  }
}
