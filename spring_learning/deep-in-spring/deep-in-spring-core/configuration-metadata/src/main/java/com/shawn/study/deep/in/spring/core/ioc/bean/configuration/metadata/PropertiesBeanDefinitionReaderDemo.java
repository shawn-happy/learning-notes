package com.shawn.study.deep.in.spring.core.ioc.bean.configuration.metadata;

import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.EncodedResource;

public class PropertiesBeanDefinitionReaderDemo {

  public static void main(String[] args) {
    // 创建 IoC 底层容器
    DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
    // 创建面向 Properties 资源的 BeanDefinitionReader 示例
    PropertiesBeanDefinitionReader beanDefinitionReader =
        new PropertiesBeanDefinitionReader(beanFactory);
    // Properties 资源加载默认通过 ISO-8859-1，实际存储 UTF-8
    ResourceLoader resourceLoader = new DefaultResourceLoader();
    // 通过指定的 ClassPath 获取 Resource 对象
    Resource resource =
        resourceLoader.getResource("classpath:/META-INF/user-bean-definitions.properties");
    // 转换成带有字符编码 EncodedResource 对象
    EncodedResource encodedResource = new EncodedResource(resource, "UTF-8");
    int beanDefinitionsCount = beanDefinitionReader.loadBeanDefinitions(encodedResource);
    System.out.printf("已记载 %d 个 BeanDefinition\n%n", beanDefinitionsCount);
    // 通过依赖查找获取 User Bean
    User user = beanFactory.getBean("user", User.class);
    System.out.println(user);
  }
}
