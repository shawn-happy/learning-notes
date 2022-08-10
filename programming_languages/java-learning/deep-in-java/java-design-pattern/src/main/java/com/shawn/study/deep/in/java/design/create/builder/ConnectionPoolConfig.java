package com.shawn.study.deep.in.java.design.create.builder;

public class ConnectionPoolConfig {

  /** jdbc connection url */
  private String url;

  /** database username */
  private String username;

  /** database password */
  private String password;

  /** database drive class */
  private String driveClassName;

  /** Maximum number of connections */
  private int maxTotal;

  /** Maximum number of idle resources */
  private int maxIdle;

  /** Minimum number of idle resources */
  private int minIdle;

  /** Default Maximum number of connections */
  private static final int DEFAULT_MAX_TOTAL = 8;

  /** Default Maximum number of idle resources */
  private static final int DEFAULT_MAX_IDLE = 8;

  /** Default Minimum number of idle resources */
  private static final int DEFAULT_MIN_IDLE = 0;

  public ConnectionPoolConfig() {}

  public ConnectionPoolConfig(String url, String username, String password, String driveClassName) {
    this(url, username, password, password, DEFAULT_MAX_TOTAL, DEFAULT_MAX_IDLE, DEFAULT_MIN_IDLE);
  }

  public ConnectionPoolConfig(
      String url,
      String username,
      String password,
      String driveClassName,
      int maxTotal,
      int maxIdle,
      int minIdle) {

    isEmpty("url", url);
    this.url = url;

    isEmpty("username", username);
    this.username = username;

    this.password = password;

    isEmpty("driveClassName", driveClassName);
    this.driveClassName = driveClassName;

    if (maxTotal <= 0) {
      throw new IllegalArgumentException("maxTotal should be positive!");
    }
    this.maxTotal = maxTotal;
    if (maxIdle < 0) {
      throw new IllegalArgumentException("maxIdle should not be negative!");
    }
    this.maxIdle = maxIdle;
    if (minIdle < 0) {
      throw new IllegalArgumentException("minIdle should not be negative!");
    }
    this.minIdle = minIdle;
  }

  private void isEmpty(String key, String value) {
    if (null == value || value.length() == 0) {
      throw new IllegalArgumentException(key + " should not be empty!");
    }
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    isEmpty("url", url);
    this.url = url;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    isEmpty("username", username);
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getDriveClassName() {
    return driveClassName;
  }

  public void setDriveClassName(String driveClassName) {
    isEmpty("driveClassName", driveClassName);
    this.driveClassName = driveClassName;
  }

  public int getMaxTotal() {
    return maxTotal;
  }

  public void setMaxTotal(int maxTotal) {
    if (maxTotal <= 0) {
      throw new IllegalArgumentException("maxTotal should be positive!");
    }
    this.maxTotal = maxTotal;
  }

  public int getMaxIdle() {
    return maxIdle;
  }

  public void setMaxIdle(int maxIdle) {
    if (maxIdle < 0) {
      throw new IllegalArgumentException("maxIdle should not be negative!");
    }
    this.maxIdle = maxIdle;
  }

  public int getMinIdle() {
    return minIdle;
  }

  public void setMinIdle(int minIdle) {
    if (minIdle < 0) {
      throw new IllegalArgumentException("minIdle should not be negative!");
    }
    this.minIdle = minIdle;
  }
}
