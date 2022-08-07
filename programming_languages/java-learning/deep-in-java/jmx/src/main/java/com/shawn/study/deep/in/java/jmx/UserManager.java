package com.shawn.study.deep.in.java.jmx;

public class UserManager implements UserManagerMBean {

  public final User user;

  public UserManager(User user) {
    this.user = user;
  }

  @Override
  public int getId() {
    return user.getId();
  }

  @Override
  public void setId(int id) {
    user.setId(id);
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  @Override
  public void setUsername(String username) {
    user.setUsername(username);
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public void setPassword(String password) {
    user.setPassword(password);
  }

  @Override
  public String getAddress() {
    return user.getAddress();
  }

  @Override
  public void setAddress(String address) {
    user.setAddress(address);
  }

  @Override
  public String toString() {
    return user.toString();
  }
}
