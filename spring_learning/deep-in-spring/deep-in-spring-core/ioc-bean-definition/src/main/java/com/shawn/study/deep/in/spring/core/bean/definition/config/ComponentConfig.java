package com.shawn.study.deep.in.spring.core.bean.definition.config;

import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ComponentConfig {
  public void doSth() {
    System.out.println("@Componet Test");
  }

  @Bean
  public User user() {
    return User.getInstance();
  }
}
