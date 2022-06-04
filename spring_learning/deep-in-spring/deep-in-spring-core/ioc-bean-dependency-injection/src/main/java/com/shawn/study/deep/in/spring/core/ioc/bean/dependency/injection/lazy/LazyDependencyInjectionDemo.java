package com.shawn.study.deep.in.spring.core.ioc.bean.dependency.injection.lazy;

import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

public class LazyDependencyInjectionDemo {

  @Autowired private User user2;

  @Autowired
  @Qualifier("user1")
  private ObjectProvider<User> personObjectProvider;

  @Bean
  private User user1() {
    return createUser("1");
  }

  @Bean
  private User user2() {
    return createUser("2");
  }

  private static User createUser(String id) {
    User user = new User();
    user.setId(id);
    return user;
  }

  public static void main(String[] args) {
    // 创建 BeanFactory 容器
    AnnotationConfigApplicationContext applicationContext =
        new AnnotationConfigApplicationContext();
    // 注册 Configuration Class（配置类） -> Spring Bean
    applicationContext.register(LazyDependencyInjectionDemo.class);
    System.out.println("spring application context ready to refresh...");
    // 启动 Spring 应用上下文
    applicationContext.refresh();
    System.out.println("spring application has finished to refresh...");
    LazyDependencyInjectionDemo bean =
        applicationContext.getBean(LazyDependencyInjectionDemo.class);
    System.out.println("before objectProvider.getIfUnique");
    User user = bean.personObjectProvider.getObject();
    System.out.println("user: " + user);
    System.out.println("user2: " + bean.user2);
    System.out.println("after objectProvider.getIfUnique");
    // 显示地关闭 Spring 应用上下文
    applicationContext.close();
  }
}
