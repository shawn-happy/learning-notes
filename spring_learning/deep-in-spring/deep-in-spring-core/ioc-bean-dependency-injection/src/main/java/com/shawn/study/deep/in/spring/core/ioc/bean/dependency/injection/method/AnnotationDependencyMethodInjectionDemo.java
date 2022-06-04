package com.shawn.study.deep.in.spring.core.ioc.bean.dependency.injection.method;

import com.shawn.study.deep.in.spring.core.ioc.bean.dependency.injection.domain.UserHolder;
import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import javax.annotation.Resource;
import javax.inject.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

public class AnnotationDependencyMethodInjectionDemo {

  @Bean
  public UserHolder userHolder() {
    User user = new User();
    user.setId("2");
    user.setName("jack");
    user.setAddress("beijing");
    user.setAge(26);
    return new UserHolder(user);
  }

  private UserHolder userHolder1;

  private UserHolder userHolder2;
  private UserHolder userHolder3;

  @Autowired
  public void init1(UserHolder userHolder) {
    this.userHolder1 = userHolder;
  }

  @Resource
  public void init2(UserHolder userHolder) {
    this.userHolder2 = userHolder;
  }

  @Inject
  public void init3(UserHolder userHolder) {
    this.userHolder3 = userHolder;
  }

  public static void main(String[] args) {
    AnnotationConfigApplicationContext applicationContext =
        new AnnotationConfigApplicationContext();
    applicationContext.register(AnnotationDependencyMethodInjectionDemo.class);
    XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(applicationContext);
    reader.loadBeanDefinitions("classpath:/dependency-lookup.xml");
    applicationContext.refresh();
    UserHolder userHolder = applicationContext.getBean("userHolder", UserHolder.class);
    System.out.printf("通过@Bean手动 method 注入userHolder：%s\n", userHolder);

    AnnotationDependencyMethodInjectionDemo demo =
        applicationContext.getBean(AnnotationDependencyMethodInjectionDemo.class);
    System.out.printf("通过@Autowired手动 method 注入userHolder1：%s\n", demo.userHolder1);
    System.out.printf("通过@Resource手动 method 注入userHolder2：%s\n", demo.userHolder2);
    System.out.printf("通过@Inject手动 method 注入userHolder3：%s\n", demo.userHolder3);
    applicationContext.close();
  }
}
