package com.shawn.study.deep.in.spring.core.ioc.bean.scope.web;

import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class WebConfiguration {

  @Bean
  //    @RequestScope
  //    @SessionScope
  @ApplicationScope
  public User user() {
    User user = new User();
    user.setId("1");
    user.setName("Shawn");
    return user;
  }
}
