package com.shawn.study.deep.in.spring.core.ioc.bean.dependency.injection.constructor;

import com.shawn.study.deep.in.spring.core.ioc.bean.dependency.injection.domain.UserHolder;
import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

public class XmlDependencyConstructorInjectionDemo {
  public static void main(String[] args) {
    DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
    XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
    reader.loadBeanDefinitions("classpath:/dependency-constructor-injection.xml");
    UserHolder userHolder = beanFactory.getBean("userHolder-constructor", UserHolder.class);
    User user = userHolder.getUser();
    System.out.printf("通过xml配置bean，使用constructor方法手动注入user: %s\n", user);
  }
}
