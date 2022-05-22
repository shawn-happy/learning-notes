package com.shawn.study.deep.in.spring.core.bean.definition.config;

import org.springframework.context.annotation.Import;

@Import(ImportConfig.class)
public class BeanConfig {

  public void doSth() {
    System.out.println("@import test");
  }
}
