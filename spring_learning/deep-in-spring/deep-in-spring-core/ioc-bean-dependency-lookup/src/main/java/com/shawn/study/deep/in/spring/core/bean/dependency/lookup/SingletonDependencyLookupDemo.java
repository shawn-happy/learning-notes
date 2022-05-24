package com.shawn.study.deep.in.spring.core.bean.dependency.lookup;

import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

/** 单一类型依赖查找 */
public class SingletonDependencyLookupDemo {

  public static void main(String[] args) {
    AnnotationConfigApplicationContext beanFactory = new AnnotationConfigApplicationContext();
    beanFactory.register(SingletonDependencyLookupDemo.class);
    beanFactory.refresh();

    getUserByName(beanFactory);
    getUserByType(beanFactory);
    getUserByTypeWithLazy(beanFactory);
    getUserByNameAndType(beanFactory);
  }

  @Bean
  public User user() {
    return User.getInstance();
  }

  /** 根据 Bean 名称查找 */
  private static void getUserByName(AnnotationConfigApplicationContext beanFactory) {
    User user = (User) beanFactory.getBean("user");
    System.out.println("根据Bean名称查找：" + user);
  }

  /** 根据 Bean 类型查找（实时） */
  private static void getUserByType(AnnotationConfigApplicationContext beanFactory) {
    User user = beanFactory.getBean(User.class);
    System.out.println("根据Bean 类型实时查找：" + user);
  }

  /** 根据 Bean 类型查找（延迟） */
  private static void getUserByTypeWithLazy(AnnotationConfigApplicationContext beanFactory) {
    ObjectProvider<User> beanProvider = beanFactory.getBeanProvider(User.class);
    User user = beanProvider.getIfAvailable();
    System.out.println("根据Bean 类型延迟查找：" + user);
  }

  /** 根据 Bean 名称和类型查找 */
  private static void getUserByNameAndType(AnnotationConfigApplicationContext beanFactory) {
    User user = beanFactory.getBean("user", User.class);
    System.out.println("根据Bean 名称和类型查找：" + user);
  }
}
