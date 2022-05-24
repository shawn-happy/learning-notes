package com.shawn.study.deep.in.spring.core.bean.dependency.lookup.exception;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BeanCreationExceptionDemo {
  public static void main(String[] args) {
    AnnotationConfigApplicationContext applicationContext =
        new AnnotationConfigApplicationContext();

    BeanDefinitionBuilder beanDefinitionBuilder =
        BeanDefinitionBuilder.genericBeanDefinition(POJO.class);
    applicationContext.registerBeanDefinition(
        "errorBean", beanDefinitionBuilder.getBeanDefinition());

    applicationContext.refresh();

    applicationContext.close();
  }

  static class POJO implements InitializingBean {

    @PostConstruct // CommonAnnotationBeanPostProcessor
    public void init() throws Throwable {
      throw new Throwable("init() : For purposes...");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
      throw new Exception("afterPropertiesSet() : For purposes...");
    }
  }
}
