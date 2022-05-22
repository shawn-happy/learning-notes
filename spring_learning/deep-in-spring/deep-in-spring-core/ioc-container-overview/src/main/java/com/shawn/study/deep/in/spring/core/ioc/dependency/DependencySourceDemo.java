package com.shawn.study.deep.in.spring.core.ioc.dependency;

import com.shawn.study.deep.in.spring.core.ioc.repository.UserRepository;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DependencySourceDemo {

  private ApplicationContext applicationContext;

  public DependencySourceDemo() {
    applicationContext = new ClassPathXmlApplicationContext("classpath:/dependency-injection.xml");
  }

  public BeanFactory getApplicationContext() {
    return applicationContext;
  }

  public UserRepository getBeanFromXMl() {
    return applicationContext.getBean("userRepository", UserRepository.class);
  }

  public UserRepository getBeanAutoWiring() {
    return applicationContext.getBean("auto-user-repository", UserRepository.class);
  }
}
