package com.shawn.study.deep.in.spring.core.ioc.domain;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class User {
  private String id;
  private String name;
  private int age;
  private String address;

  public User() {}

  public User(String id, String name, int age, String address) {
    this.id = id;
    this.name = name;
    this.age = age;
    this.address = address;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return "User{"
        + "id='"
        + id
        + '\''
        + ", name='"
        + name
        + '\''
        + ", age="
        + age
        + ", address='"
        + address
        + '\''
        + '}';
  }

  @PostConstruct
  public void init() {
    System.out.println("用户 id:" + id + " 正在初始化中...");
  }

  @PreDestroy
  public void destroy() {
    System.out.println("用户 id: " + id + " 正在销毁中...");
  }

  public static User getInstance() {
    User user = new User();
    user.setId("1");
    user.setName("Shawn");
    user.setAge(26);
    user.setAddress("SHANGHAI");
    return user;
  }
}
