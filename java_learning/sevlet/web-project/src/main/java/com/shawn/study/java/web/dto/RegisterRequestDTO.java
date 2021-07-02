package com.shawn.study.java.web.dto;

public class RegisterRequestDTO {

  private String username;
  private String address;

  public RegisterRequestDTO() {}

  public RegisterRequestDTO(String username, String address) {
    this.username = username;
    this.address = address;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }
}
