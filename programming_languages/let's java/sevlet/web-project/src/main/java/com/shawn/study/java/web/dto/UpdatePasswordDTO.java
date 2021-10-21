package com.shawn.study.java.web.dto;

public class UpdatePasswordDTO {

  private int id;
  private String newPassword;

  public UpdatePasswordDTO() {}

  public UpdatePasswordDTO(int id, String newPassword) {
    this.id = id;
    this.newPassword = newPassword;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }
}
