package com.shawn.study.deep.in.spring.core.bean.dependency.injection.setter;

import com.shawn.study.deep.in.spring.core.bean.dependency.injection.domain.UserHolder;
import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

public class AnnotationDependencySetterInjectionDemo {

  public static void main(String[] args) {
    AnnotationConfigApplicationContext applicationContext =
        new AnnotationConfigApplicationContext();
    applicationContext.register(AnnotationDependencySetterInjectionDemo.class);
    XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(applicationContext);
    reader.loadBeanDefinitions("classpath:/dependency-lookup.xml");
    applicationContext.refresh();
    UserHolder userHolder = applicationContext.getBean("userHolder", UserHolder.class);
    User user = userHolder.getUser();
    System.out.printf("通过annotation手动setter注入user：%s\n", user);
    applicationContext.close();
  }

  @Bean
  public UserHolder userHolder(User user) {
    UserHolder userHolder = new UserHolder();
    userHolder.setUser(user);
    return userHolder;
  }
}
