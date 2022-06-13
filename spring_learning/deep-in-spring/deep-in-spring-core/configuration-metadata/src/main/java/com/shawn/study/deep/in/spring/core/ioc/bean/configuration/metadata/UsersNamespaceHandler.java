package com.shawn.study.deep.in.spring.core.ioc.bean.configuration.metadata;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class UsersNamespaceHandler extends NamespaceHandlerSupport {

  @Override
  public void init() {
    registerBeanDefinitionParser("user", new UserBeanDefinitionParser());
  }
}
