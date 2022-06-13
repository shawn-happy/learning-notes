package com.shawn.study.deep.in.spring.core.ioc.bean.configuration.metadata;

import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.MapPropertySource;

@PropertySource("classpath:/META-INF/user-bean-definitions.properties")
public class PropertySourceDemo {

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

    // 扩展 Environment 中的 PropertySources
    // 添加 PropertySource 操作必须在 refresh 方法之前完成
    Map<String, Object> propertiesSource = new HashMap<>();
    propertiesSource.put("user.name", "shawn");
    org.springframework.core.env.PropertySource propertySource =
        new MapPropertySource("first-property-source", propertiesSource);
    context.getEnvironment().getPropertySources().addFirst(propertySource);

    // 注册当前类作为 Configuration Class
    context.register(PropertySourceDemo.class);
    // 启动 Spring 应用上下文
    context.refresh();
    // beanName 和 bean 映射
    Map<String, User> usersMap = context.getBeansOfType(User.class);
    for (Map.Entry<String, User> entry : usersMap.entrySet()) {
      System.out.printf("User Bean name : %s , content : %s \n", entry.getKey(), entry.getValue());
    }
    System.out.println(context.getEnvironment().getPropertySources());
    // 关闭 Spring 应用上下文
    context.close();
  }
}
