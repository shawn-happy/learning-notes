package com.shawn.study.deep.in.spring.core.ioc.bean.dependency.lookup.config;

import com.shawn.study.deep.in.spring.core.ioc.bean.dependency.lookup.domain.Employee;
import org.springframework.context.annotation.Bean;

public class ChildConfig {

  @Bean
  public Employee employee1() {
    return new Employee(1, "employee1", "employee1");
  }

  //  @Bean
  //  @Primary
  //  public Employee employee2() {
  //    return new Employee(2, "employee2", "employee2");
  //  }

}
