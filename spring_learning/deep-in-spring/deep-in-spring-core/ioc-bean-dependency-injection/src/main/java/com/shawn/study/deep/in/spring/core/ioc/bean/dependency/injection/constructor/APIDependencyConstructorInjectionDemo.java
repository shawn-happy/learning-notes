package com.shawn.study.deep.in.spring.core.ioc.bean.dependency.injection.constructor;

import com.shawn.study.deep.in.spring.core.ioc.bean.dependency.injection.domain.UserHolder;
import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class APIDependencyConstructorInjectionDemo {

  public static void main(String[] args) {
    AnnotationConfigApplicationContext applicationContext =
        new AnnotationConfigApplicationContext();
    XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(applicationContext);
    reader.loadBeanDefinitions("classpath:/dependency-lookup.xml");
    BeanDefinitionBuilder beanDefinitionBuilder =
        BeanDefinitionBuilder.genericBeanDefinition(UserHolder.class);
    beanDefinitionBuilder.addConstructorArgReference("user");
    BeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
    applicationContext.registerBeanDefinition("userHolder", beanDefinition);
    applicationContext.refresh();

    UserHolder userHolder = applicationContext.getBean("userHolder", UserHolder.class);
    User user = userHolder.getUser();
    System.out.printf("通过java api手动 constructor-args 注入user：%s\n", user);
    applicationContext.close();
  }
}
