package com.shawn.study.deep.in.spring.core.ioc.domain;

public class Company {

  private String name;

  public Company(String name) {
    this.name = name;
  }

  public Company() {}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Company{" + "name='" + name + '\'' + '}';
  }
}
