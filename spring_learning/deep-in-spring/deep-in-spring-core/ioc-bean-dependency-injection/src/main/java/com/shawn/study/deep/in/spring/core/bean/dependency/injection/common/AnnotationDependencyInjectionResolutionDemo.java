package com.shawn.study.deep.in.spring.core.bean.dependency.injection.common;

import com.shawn.study.deep.in.spring.core.bean.dependency.injection.annotation.UserInject;
import com.shawn.study.deep.in.spring.core.bean.dependency.injection.annotation.UserOptional;
import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Resource;
import javax.inject.Inject;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

public class AnnotationDependencyInjectionResolutionDemo {

  @Autowired private User user;
  @Resource private User resourceUser;
  @Autowired private Map<String, User> userMap;
  @Inject private User injectUser;
  @Autowired private ObjectProvider<User> userObjectProvider;
  @UserOptional private Optional<User> userOptional;
  @UserInject private User customUser;

  @Autowired @Lazy private User lazyUser;

  @Bean
  @Order(Ordered.LOWEST_PRECEDENCE - 3)
  @Scope
  public static AutowiredAnnotationBeanPostProcessor beanPostProcessor() {
    AutowiredAnnotationBeanPostProcessor beanPostProcessor =
        new AutowiredAnnotationBeanPostProcessor();
    Set<Class<? extends Annotation>> autowiredAnnotationTypes = new LinkedHashSet<>();
    autowiredAnnotationTypes.add(Autowired.class);
    autowiredAnnotationTypes.add(Inject.class);
    autowiredAnnotationTypes.add(UserInject.class);
    beanPostProcessor.setAutowiredAnnotationTypes(autowiredAnnotationTypes);
    return beanPostProcessor;
  }

  public static void main(String[] args) {
    AnnotationConfigApplicationContext applicationContext =
        new AnnotationConfigApplicationContext();
    XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(applicationContext);
    reader.loadBeanDefinitions("classpath:/dependency-lookup.xml");
    applicationContext.register(AnnotationDependencyInjectionResolutionDemo.class);
    applicationContext.refresh();
    AnnotationDependencyInjectionResolutionDemo demo =
        applicationContext.getBean(AnnotationDependencyInjectionResolutionDemo.class);
    System.out.println("demo.user: " + demo.user);
    System.out.println("demo.lazyUser: " + demo.lazyUser);
    System.out.println("demo.resourceUser: " + demo.resourceUser);
    System.out.println("demo.userMap: " + demo.userMap);
    System.out.println("demo.injectUser: " + demo.injectUser);
    System.out.println("demo.userObjectProvider: " + demo.userObjectProvider.getIfAvailable());
    System.out.println("demo.userOptional: " + demo.userOptional.orElse(null));
    System.out.println("demo.customUser: " + demo.customUser);
    applicationContext.close();
  }
}
