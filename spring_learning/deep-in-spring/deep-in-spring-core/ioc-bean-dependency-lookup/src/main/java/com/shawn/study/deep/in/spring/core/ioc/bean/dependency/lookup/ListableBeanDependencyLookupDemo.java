package com.shawn.study.deep.in.spring.core.ioc.bean.dependency.lookup;

import com.shawn.study.deep.in.spring.core.ioc.bean.dependency.lookup.annotation.Domain;
import com.shawn.study.deep.in.spring.core.ioc.bean.dependency.lookup.domain.Department;
import com.shawn.study.deep.in.spring.core.ioc.bean.dependency.lookup.domain.Employee;
import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import java.util.Arrays;
import java.util.Map;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

/** 集合类型依赖查找 */
public class ListableBeanDependencyLookupDemo {

  public static void main(String[] args) {
    AnnotationConfigApplicationContext beanFactory = new AnnotationConfigApplicationContext();
    beanFactory.register(ListableBeanDependencyLookupDemo.class);
    beanFactory.refresh();
    getBeanNamesByType(beanFactory);
    getBeansByType(beanFactory);
    getBeanNamesByAnnotation(beanFactory);
    getBeansByAnnotation(beanFactory);
    getBeansByNameAndAnnotation(beanFactory);
    beanFactory.close();
  }

  @Bean
  public User user1() {
    return User.getInstance();
  }

  @Bean
  public User user2() {
    return new User("2", "shawn2", 25, "beijing");
  }

  @Bean
  public Employee employee1() {
    return new Employee(1, "employee1", "employee1");
  }

  @Bean
  public Employee employee2() {
    return new Employee(2, "employee2", "employee2");
  }

  @Bean
  public Department department1() {
    return new Department(1, "department1", "department1");
  }

  @Bean
  public Department department2() {
    return new Department(2, "department2", "department2");
  }

  /** 获取同类型 Bean 名称列表 */
  private static void getBeanNamesByType(AnnotationConfigApplicationContext beanFactory) {
    String[] beanNames = beanFactory.getBeanNamesForType(User.class);
    System.out.println("根据Bean类型获取所有同类型的Bean名称：" + Arrays.toString(beanNames));
  }

  /** 获取同类型 Bean 实例列表 */
  private static void getBeansByType(AnnotationConfigApplicationContext beanFactory) {
    Map<String, User> beans = beanFactory.getBeansOfType(User.class);
    System.out.println("根据Bean类型获取所有同类型的Bean：" + beans);
  }

  /** Spring 3.0 获取标注类型 Bean 名称列表 */
  private static void getBeanNamesByAnnotation(AnnotationConfigApplicationContext beanFactory) {
    String[] names = beanFactory.getBeanNamesForAnnotation(Domain.class);
    System.out.println("根据Annotation获取所有标记为 @Domain 的Bean名称：" + Arrays.toString(names));
  }

  /** Spring 3.0 获取标注类型 Bean 列表 */
  private static void getBeansByAnnotation(AnnotationConfigApplicationContext beanFactory) {
    Map<String, Object> beans = beanFactory.getBeansWithAnnotation(Domain.class);
    System.out.println("根据Annotation类型获取所有标记为 @Domain 的Bean：" + beans);
  }

  /** 获取指定名称 + 标注类型 Bean 实例 */
  private static void getBeansByNameAndAnnotation(AnnotationConfigApplicationContext beanFactory) {
    Domain domain = beanFactory.findAnnotationOnBean("employee1", Domain.class);
    if (domain != null) {
      System.out.println("employee1 is @Domain bean");
    }
    domain = beanFactory.findAnnotationOnBean("user1", Domain.class);
    if (domain == null) {
      System.out.println("user1 is not @Domain bean");
    }
  }
}
