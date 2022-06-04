package com.shawn.study.deep.in.spring.core.ioc.bean.dependency.lookup;

import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

public class ObjectProviderDemo {
  public static void main(String[] args) {
    AnnotationConfigApplicationContext applicationContext =
        new AnnotationConfigApplicationContext();
    applicationContext.register(ObjectProviderDemo.class);
    applicationContext.refresh();
    lookupByObjectProvider(applicationContext);
    lookupIfAvailable(applicationContext);
    lookupByStreamOps(applicationContext);

    // 关闭应用上下文
    applicationContext.close();
  }

  private static void lookupByStreamOps(AnnotationConfigApplicationContext applicationContext) {
    ObjectProvider<String> objectProvider = applicationContext.getBeanProvider(String.class);
    objectProvider.stream().forEach(System.out::println);
  }

  private static void lookupIfAvailable(AnnotationConfigApplicationContext applicationContext) {
    ObjectProvider<User> userObjectProvider = applicationContext.getBeanProvider(User.class);
    User user = userObjectProvider.getIfAvailable(User::getInstance);
    System.out.println("当前 User 对象：" + user);
  }

  @Bean
  @Primary
  public String helloWorld() {
    return "Hello,World";
  }

  @Bean
  public String message() {
    return "Message";
  }

  private static void lookupByObjectProvider(
      AnnotationConfigApplicationContext applicationContext) {
    ObjectProvider<String> objectProvider = applicationContext.getBeanProvider(String.class);
    System.out.println(objectProvider.getObject());
  }
}
