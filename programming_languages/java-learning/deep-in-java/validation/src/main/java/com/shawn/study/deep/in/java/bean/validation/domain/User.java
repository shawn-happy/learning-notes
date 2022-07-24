package com.shawn.study.deep.in.java.bean.validation.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class User {

  @NotNull private String id;

  @Size(min = 6, max = 12)
  private String name;

  @Size(min = 6, max = 18)
  private String password;

  public User(String id, String name, String password) {
    this.id = id;
    this.name = name;
    this.password = password;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getPassword() {
    return password;
  }
}
