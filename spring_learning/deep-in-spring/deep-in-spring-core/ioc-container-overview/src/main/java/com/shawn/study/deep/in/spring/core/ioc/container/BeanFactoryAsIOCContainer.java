package com.shawn.study.deep.in.spring.core.ioc.container;

import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

public class BeanFactoryAsIOCContainer {

  public static void main(String[] args) {
    DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
    // DefaultListableBeanFactory implement BeanDefinitionRegistry
    XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(beanFactory);
    xmlReader.loadBeanDefinitions("classpath:/dependency-lookup.xml");
    User user = (User) beanFactory.getBean("user");
    System.out.println(user);
  }
}
