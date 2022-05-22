package com.shawn.study.deep.in.spring.core.ioc.container;

import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

public class ApplicationContextAsIOCContainer {

  public static void main(String[] args) {
    AnnotationConfigApplicationContext applicationContext =
        new AnnotationConfigApplicationContext();
    // 扫描该类同包和子包下面的所有bean
    applicationContext.register(ApplicationContextAsIOCContainer.class);
    // 初始化容器
    applicationContext.refresh();
    User user = (User) applicationContext.getBean("user");
    System.out.println(user);
    applicationContext.close();
  }

  @Bean
  public User user() {
    User user = new User();
    user.setId("1");
    user.setName("Shawn");
    user.setAge(26);
    user.setAddress("SHANGHAI");
    return user;
  }
}
