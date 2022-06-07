package com.shawn.study.deep.in.spring.core.ioc.bean.lifecycle;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

public class BeanPopulatePropertyValuesDemo {

  public static void main(String[] args) {
    DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
    beanFactory.addBeanPostProcessor(new UserInstantiationAwareBeanPostProcessor());
    XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
    String[] locations = {"dependency-lookup.xml", "bean-life-cycle.xml"};
    beanDefinitionReader.loadBeanDefinitions(locations);
    UserHolder userHolder = beanFactory.getBean(UserHolder.class);
    System.out.println(userHolder);
  }
}
