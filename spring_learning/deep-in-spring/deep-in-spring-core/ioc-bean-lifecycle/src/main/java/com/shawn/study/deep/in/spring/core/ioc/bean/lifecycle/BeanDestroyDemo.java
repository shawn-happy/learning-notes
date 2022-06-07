package com.shawn.study.deep.in.spring.core.ioc.bean.lifecycle;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;

public class BeanDestroyDemo {

  public static void main(String[] args) {
    DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
    beanFactory.addBeanPostProcessor(new UserInstantiationAwareBeanPostProcessor());
    beanFactory.addBeanPostProcessor(new UserDestructionAwareBeanPostProcessor());
    beanFactory.addBeanPostProcessor(new CommonAnnotationBeanPostProcessor());
    XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
    String[] locations = {"dependency-lookup.xml", "bean-life-cycle.xml"};
    beanDefinitionReader.loadBeanDefinitions(locations);
    // Bean初始化完成阶段
    beanFactory.preInstantiateSingletons();
    UserHolder userHolder = beanFactory.getBean(UserHolder.class);

    System.out.println(userHolder);

    // Bean销毁阶段
    beanFactory.destroyBean("userHolder", userHolder);
    // Bean销毁并不是gc
    System.out.println(userHolder);
  }
}
