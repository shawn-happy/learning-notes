package com.shawn.study.deep.in.spring.core.ioc.bean.configuration.metadata;

import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

@ImportResource("classpath:/dependency-lookup.xml")
@Import(User.class)
@PropertySource("classpath:/META-INF/user-bean-definitions.properties") // Java 8+ @Repeatable 支持
@PropertySource("classpath:/META-INF/user-bean-definitions.properties")
// @PropertySources(@PropertySource(...))
public class AnnotatedSpringIoCContainerMetadataConfigurationDemo {

  /**
   * user.name 是 Java Properties 默认存在，当前用户：mercyblitz，而非配置文件中定义"小马哥"
   *
   * @param id
   * @param name
   * @return
   */
  @Bean
  public User configuredUser(
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
    // 注册当前类作为 Configuration Class
    context.register(AnnotatedSpringIoCContainerMetadataConfigurationDemo.class);
    // 启动 Spring 应用上下文
    context.refresh();
    // beanName 和 bean 映射
    Map<String, User> usersMap = context.getBeansOfType(User.class);
    for (Map.Entry<String, User> entry : usersMap.entrySet()) {
      System.out.printf("User Bean name : %s , content : %s \n", entry.getKey(), entry.getValue());
    }
    // 关闭 Spring 应用上下文
    context.close();
  }
}
