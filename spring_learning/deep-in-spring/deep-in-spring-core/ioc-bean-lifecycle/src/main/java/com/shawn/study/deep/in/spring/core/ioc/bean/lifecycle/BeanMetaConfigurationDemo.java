package com.shawn.study.deep.in.spring.core.ioc.bean.lifecycle;

import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;

/** 元信息配置阶段 */
public class BeanMetaConfigurationDemo {

  public static void main(String[] args) {}

  public User getBeanFromProperties() {
    DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
    PropertiesBeanDefinitionReader reader = new PropertiesBeanDefinitionReader(beanFactory);
    reader.loadBeanDefinitions("classpath:/bean.properties");
    return null;
  }
}
