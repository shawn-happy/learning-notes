package com.shawn.study.java.web.entity;

/**
 * user entity
 *
 * @author Shawn
 * @since 1.0.0
 */
public class UserEntity {

  private int id;
  private String username;
  private String password;
  private String address;

  public UserEntity() {}

  public UserEntity(int id, String username, String password, String address) {
    this.id = id;
    this.username = username;
    this.password = password;
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
}
