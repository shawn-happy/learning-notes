package com.shawn.study.deep.in.spring.core.bean.definition.config;

import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import org.springframework.context.annotation.Bean;

public class ImportConfig {

  @Bean(name = "importUser")
  public User user() {
    return new User("shawn", "shawn", 26, "shanghai");
  }
}
