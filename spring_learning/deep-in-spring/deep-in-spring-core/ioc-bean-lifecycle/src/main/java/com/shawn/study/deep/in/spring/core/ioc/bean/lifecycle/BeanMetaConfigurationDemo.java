package com.shawn.study.deep.in.spring.core.ioc.bean.lifecycle;

import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;

/** 元信息配置阶段 */
public class BeanMetaConfigurationDemo {

  public static void main(String[] args) {
    getBeanFromProperties();
  }

  private static void getBeanFromProperties() {
    DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
    PropertiesBeanDefinitionReader reader = new PropertiesBeanDefinitionReader(beanFactory);
    Resource resource = new ClassPathResource("bean.properties");
    EncodedResource encodedResource = new EncodedResource(resource, "UTF-8");
    reader.loadBeanDefinitions(encodedResource);
    int beanDefinitionCount = beanFactory.getBeanDefinitionCount();
    System.out.println("bean num: " + beanDefinitionCount);
    User user = beanFactory.getBean(User.class);
    System.out.println("user bean: " + user);
  }
}
