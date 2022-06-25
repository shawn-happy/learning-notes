package com.shawn.study.deep.in.spring.core.annotation;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class HelloWorldImportSelector implements ImportSelector {

  @Override
  public String[] selectImports(AnnotationMetadata importingClassMetadata) {
    return new String[] {"com.shawn.study.deep.in.spring.core.annotation.HelloWorldConfiguration"};
  }
}
