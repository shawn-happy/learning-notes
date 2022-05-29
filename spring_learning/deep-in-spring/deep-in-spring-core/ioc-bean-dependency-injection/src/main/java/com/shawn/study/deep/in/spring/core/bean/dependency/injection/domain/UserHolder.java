package com.shawn.study.deep.in.spring.core.bean.dependency.injection.domain;

import com.shawn.study.deep.in.spring.core.ioc.domain.User;

public class UserHolder {

  private User user;

  public UserHolder() {}

  public UserHolder(User user) {
    this.user = user;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public String toString() {
    return "UserHolder{" + "user=" + user + '}';
  }
}
