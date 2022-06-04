package com.shawn.study.deep.in.spring.core.ioc.bean.dependency.injection.qualifier;

import com.shawn.study.deep.in.spring.core.ioc.bean.dependency.injection.annotation.UserGroup;
import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

public class QualifierDependencyInjectionDemo {

  @Autowired private List<User> noQualifierUsers;
  @Autowired @Qualifier private List<User> qualifierUsers;
  @Autowired @UserGroup private List<User> userGroupUsers;

  @Autowired
  @Qualifier("user-7")
  private User user;

  @Bean
  public User user1() {
    return createUser("1");
  }

  @Bean
  public User user2() {
    return createUser("2");
  }

  @Bean
  @Qualifier
  public User user3() {
    return createUser("3");
  }

  @Bean
  @Qualifier
  public User user4() {
    return createUser("4");
  }

  @Bean
  @UserGroup
  public User user5() {
    return createUser("5");
  }

  @Bean
  @UserGroup
  public User user6() {
    return createUser("6");
  }

  @Bean
  @Qualifier("user-7")
  public User user7() {
    return createUser("7");
  }

  public static void main(String[] args) {
    AnnotationConfigApplicationContext applicationContext =
        new AnnotationConfigApplicationContext();
    applicationContext.register(QualifierDependencyInjectionDemo.class);
    applicationContext.refresh();
    QualifierDependencyInjectionDemo demo =
        applicationContext.getBean(QualifierDependencyInjectionDemo.class);
    // 期待输出 user-7 Bean
    System.out.println("demo.user = " + demo.user);

    // 期待输出 user1 user2
    System.out.println("demo.noQualifierUsers = " + demo.noQualifierUsers);

    // 如果加上@UserGroup注解，预期输出 user3 + user4 + user5 + user6
    System.out.println("demo.qualifierPersons = " + demo.qualifierUsers);

    // 期待输出 user5 + user6 Bean
    System.out.println("demo.groupPersons = " + demo.userGroupUsers);
    applicationContext.close();
  }

  private static User createUser(String id) {
    User user = new User();
    user.setId(id);
    return user;
  }
}
