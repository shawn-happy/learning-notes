package com.shawn.study.deep.in.spring.core.ioc.domain;

import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class User {
  private String id;
  private String name;
  private int age;
  private String address;
  private Company company;
  private Properties context;
  private String contextAsText;

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

  public Company getCompany() {
    return company;
  }

  public void setCompany(Company company) {
    this.company = company;
  }

  public Properties getContext() {
    return context;
  }

  public void setContext(Properties context) {
    this.context = context;
  }

  public String getContextAsText() {
    return contextAsText;
  }

  public void setContextAsText(String contextAsText) {
    this.contextAsText = contextAsText;
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
        + ", company="
        + company
        + ", context="
        + context
        + ", contextAsText='"
        + contextAsText
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
