package com.shawn.study.deep.in.spring.core.ioc.bean.configuration.metadata;

import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

@PropertySource(
    name = "yamlPropertySource",
    value = "classpath:/META-INF/user.yaml",
    factory = YamlPropertySourceFactory.class)
public class AnnotatedYamlPropertySourceDemo {

  @Bean
  public User user(
      @Value("${user.id}") String id,
      @Value("${user.name}") String name,
      @Value("${user.age}") int age,
      @Value("${user.address}") String address) {
    User user = new User();
    user.setId(id);
    user.setName(name);
    user.setAge(age);
    user.setAddress(address);
    return user;
  }

  public static void main(String[] args) {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    context.register(AnnotatedYamlPropertySourceDemo.class);
    context.refresh();
    User user = context.getBean("user", User.class);
    System.out.println(user);
    context.close();
  }
}
