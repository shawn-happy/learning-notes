package com.shawn.study.deep.in.spring.core.ioc.bean.lifecycle;

import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

public class BeanInstantiationDemo {

  @Autowired private UserHolder userHolder;

  @Bean
  public UserHolder userHolder(User user) {
    return new UserHolder(user);
  }

  @Autowired
  public BeanInstantiationDemo(User user) {
    System.out.println(user);
  }

  public static void main(String[] args) {
    AnnotationConfigApplicationContext applicationContext =
        new AnnotationConfigApplicationContext();
    applicationContext.register(BeanInstantiationDemo.class);
    ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
    beanFactory.addBeanPostProcessor(new UserInstantiationAwareBeanPostProcessor());
    XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(applicationContext);
    reader.loadBeanDefinitions("classpath:/dependency-lookup.xml");
    applicationContext.refresh();
    BeanInstantiationDemo demo = applicationContext.getBean(BeanInstantiationDemo.class);
    System.out.printf("通过@Autowired手动 field 注入userHolder：%s\n", demo.userHolder);
    applicationContext.close();
  }
}
