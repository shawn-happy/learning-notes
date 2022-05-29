package com.shawn.study.deep.in.spring.core.bean.dependency.injection.common;

import com.shawn.study.deep.in.spring.core.bean.dependency.injection.domain.Pojo;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

public class JavaClassDependencyInjectionDemo {
  public static void main(String[] args) {
    DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
    XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
    reader.loadBeanDefinitions("classpath:/java-class-dependency-injection.xml");
    Pojo pojo = beanFactory.getBean("pojo", Pojo.class);
    System.out.printf("%s\n", pojo);
  }
}
