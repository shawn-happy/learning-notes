package com.shawn.study.java.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * user entity
 *
 * @author Shawn
 * @since 1.0.0
 */
@Entity
@Table(name = "user")
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column private String username;
  @Column private String password;
  @Column private int age;
  @Column private String address;

  public UserEntity() {}

  public UserEntity(String username, String password, int age, String address) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.age = age;
    this.address = address;
  }

  public UserEntity(int id, String username, String password, int age, String address) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.age = age;
    this.address = address;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  @Override
  public String toString() {
    return "UserEntity{"
        + "id="
        + id
        + ", username='"
        + username
        + '\''
        + ", password='"
        + password
        + '\''
        + ", age="
        + age
        + ", address='"
        + address
        + '\''
        + '}';
  }
}
