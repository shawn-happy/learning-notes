package com.shawn.study.deep.in.spring.core.ioc.dependency;

import com.shawn.study.deep.in.spring.core.ioc.repository.UserRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DependencyInjectionDemo {

  private ApplicationContext applicationContext;

  public DependencyInjectionDemo() {
    applicationContext = new ClassPathXmlApplicationContext("classpath:/dependency-injection.xml");
  }

  public UserRepository injectWithCode() {
    return applicationContext.getBean("userRepository", UserRepository.class);
  }

  public UserRepository injectWithAutoWiring() {
    return applicationContext.getBean("auto-user-repository", UserRepository.class);
  }
}
