package com.shawn.study.deep.in.spring.core.bean.dependency.injection.field;

import com.shawn.study.deep.in.spring.core.bean.dependency.injection.domain.UserHolder;
import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import javax.annotation.Resource;
import javax.inject.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

public class AnnotationDependencyFieldInjectionDemo {

  @Autowired private UserHolder userHolder;

  @Resource private UserHolder userHolder2;

  @Inject private User user;

  @Bean
  public UserHolder userHolder(User user) {
    return new UserHolder(user);
  }

  @Bean
  public UserHolder userHolder2() {
    User user = new User();
    user.setId("2");
    user.setName("jack");
    user.setAddress("beijing");
    user.setAge(26);
    return new UserHolder(user);
  }

  public static void main(String[] args) {
    AnnotationConfigApplicationContext applicationContext =
        new AnnotationConfigApplicationContext();
    applicationContext.register(AnnotationDependencyFieldInjectionDemo.class);
    XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(applicationContext);
    reader.loadBeanDefinitions("classpath:/dependency-lookup.xml");
    applicationContext.refresh();
    AnnotationDependencyFieldInjectionDemo demo =
        applicationContext.getBean(AnnotationDependencyFieldInjectionDemo.class);
    System.out.printf("通过@Autowired手动 field 注入userHolder：%s\n", demo.userHolder);
    System.out.printf("通过@Autowired手动 field 注入userHolder2：%s\n", demo.userHolder2);
    System.out.printf("通过@Autowired手动 field user：%s\n", demo.user);
    applicationContext.close();
  }
}
