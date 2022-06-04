package com.shawn.study.deep.in.spring.core.ioc.bean.definition.factory;

import com.shawn.study.deep.in.spring.core.ioc.domain.User;

public interface UserFactory {
  default User createUser() {
    return User.getInstance();
  }
}
