package com.shawn.study.deep.in.spring.core.ioc.bean.definition.factory;

import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import org.springframework.beans.factory.FactoryBean;

public class UserFactoryBean implements FactoryBean<User> {

  @Override
  public User getObject() throws Exception {
    return User.getInstance();
  }

  @Override
  public Class<?> getObjectType() {
    return User.class;
  }
}
