package com.shawn.study.deep.in.spring.core.ioc.bean.dependency.injection.constructor;

import com.shawn.study.deep.in.spring.core.ioc.bean.dependency.injection.domain.UserHolder;
import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

public class AnnotationDependencyConstructorInjectionDemo {

  public static void main(String[] args) {
    AnnotationConfigApplicationContext applicationContext =
        new AnnotationConfigApplicationContext();
    applicationContext.register(AnnotationDependencyConstructorInjectionDemo.class);
    XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(applicationContext);
    reader.loadBeanDefinitions("classpath:/dependency-lookup.xml");
    applicationContext.refresh();
    UserHolder userHolder = applicationContext.getBean("userHolder", UserHolder.class);
    User user = userHolder.getUser();
    System.out.printf("通过annotation手动 constructor-args 注入user：%s\n", user);
    applicationContext.close();
  }

  @Bean
  public UserHolder userHolder(User user) {
    return new UserHolder(user);
  }
}
