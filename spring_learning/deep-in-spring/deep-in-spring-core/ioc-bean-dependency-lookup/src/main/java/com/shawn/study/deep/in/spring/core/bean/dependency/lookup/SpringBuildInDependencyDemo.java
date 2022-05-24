package com.shawn.study.deep.in.spring.core.bean.dependency.lookup;

import java.util.Arrays;
import java.util.Iterator;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/** spring内建依赖 */
public class SpringBuildInDependencyDemo {

  public static void main(String[] args) {
    AnnotationConfigApplicationContext applicationContext =
        new AnnotationConfigApplicationContext();
    applicationContext.refresh();
    // spring 内建Bean对象
    String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
    Arrays.stream(beanDefinitionNames)
        .forEach(
            name ->
                System.out.printf("name: %s, bean: %s\n", name, applicationContext.getBean(name)));

    ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
    Iterator<String> beanNamesIterator = beanFactory.getBeanNamesIterator();
    while (beanNamesIterator.hasNext()) {
      String beanName = beanNamesIterator.next();
      System.out.printf("name: %s, bean: %s\n", beanName, applicationContext.getBean(beanName));
    }
  }
}
