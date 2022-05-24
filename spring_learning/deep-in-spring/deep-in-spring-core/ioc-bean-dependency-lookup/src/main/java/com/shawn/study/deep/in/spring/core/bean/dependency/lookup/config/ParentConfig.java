package com.shawn.study.deep.in.spring.core.bean.dependency.lookup.config;

import com.shawn.study.deep.in.spring.core.bean.dependency.lookup.domain.Department;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

public class ParentConfig {

  @Bean
  @Primary
  public Department department1() {
    return new Department(1, "department1", "department1");
  }

  @Bean
  public Department department2() {
    return new Department(2, "department2", "department2");
  }
}
