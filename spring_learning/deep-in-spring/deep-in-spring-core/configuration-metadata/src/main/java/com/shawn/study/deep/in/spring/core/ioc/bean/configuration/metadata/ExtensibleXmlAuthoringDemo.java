package com.shawn.study.deep.in.spring.core.ioc.bean.configuration.metadata;

import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

public class ExtensibleXmlAuthoringDemo {

  public static void main(String[] args) {
    DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
    XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
    reader.loadBeanDefinitions("META-INF/user-custom.xml");
    User user = beanFactory.getBean(User.class);
    System.out.println(user);
  }
}
