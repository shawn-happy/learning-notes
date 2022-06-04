package com.shawn.study.deep.in.spring.core.ioc.bean.definition.factory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class DefaultUserFactory implements UserFactory, InitializingBean, DisposableBean {

  @PostConstruct
  public void initByAnnotationPostConstruct() {
    System.out.println("DefaultUserFactory.init By Annotation PostConstruct");
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    System.out.println("DefaultUserFactory.init By InitializingBean.afterPropertiesSet method");
  }

  public void initMethod() {
    System.out.println("DefaultUserFactory.init Method");
  }

  @PreDestroy
  public void destroyByAnnotationPreDestroy() {
    System.out.println("DefaultUserFactory.destroy By Annotation PreDestroy");
  }

  @Override
  public void destroy() throws Exception {
    System.out.println("DefaultUserFactory.init By DisposableBean.destroy method");
  }

  public void destroyMethod() {
    System.out.println("DefaultUserFactory.destroy Method");
  }

  @Override
  public void finalize() throws Throwable {
    System.out.println("DefaultUserFactory.finalize");
  }
}
