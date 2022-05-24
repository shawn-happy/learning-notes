package com.shawn.study.deep.in.spring.core.bean.dependency.lookup.domain;

import com.shawn.study.deep.in.spring.core.bean.dependency.lookup.annotation.Domain;

@Domain
public class Employee {
  private int id;
  private String code;
  private String name;

  public Employee() {}

  public Employee(int id, String code, String name) {
    this.id = id;
    this.code = code;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Employee{" + "id=" + id + ", code='" + code + '\'' + ", name='" + name + '\'' + '}';
  }
}
