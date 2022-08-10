package com.shawn.study.deep.in.java.design.create.builder;

public class ConnectionPoolConfigBuilderDemo {

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

  public ConnectionPoolConfigBuilderDemo(Builder builder) {
    this.url = builder.url;
    this.username = builder.username;
    this.password = builder.password;
    this.driveClassName = builder.driveClassName;
    this.maxTotal = builder.maxTotal;
    this.maxIdle = builder.maxIdle;
    this.minIdle = builder.minIdle;
  }

  private static void isEmpty(String key, String value) {
    if (null == value || value.length() == 0) {
      throw new IllegalArgumentException(key + " should not be empty!");
    }
  }

  public String getUrl() {
    return url;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getDriveClassName() {
    return driveClassName;
  }

  public int getMaxTotal() {
    return maxTotal;
  }

  public int getMaxIdle() {
    return maxIdle;
  }

  public int getMinIdle() {
    return minIdle;
  }

  public static class Builder {

    /** Default Maximum number of connections */
    private static final int DEFAULT_MAX_TOTAL = 8;

    /** Default Maximum number of idle resources */
    private static final int DEFAULT_MAX_IDLE = 8;

    /** Default Minimum number of idle resources */
    private static final int DEFAULT_MIN_IDLE = 0;

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

    public ConnectionPoolConfigBuilderDemo build() {
      isEmpty("url", url);
      isEmpty("username", username);
      isEmpty("password", password);
      isEmpty("driveClassName", driveClassName);

      if (maxTotal <= 0) {
        throw new IllegalArgumentException("maxTotal should be positive!");
      }
      if (maxIdle < 0) {
        throw new IllegalArgumentException("maxIdle should not be negative!");
      }
      if (minIdle < 0) {
        throw new IllegalArgumentException("minIdle should not be negative!");
      }

      if (maxIdle > maxTotal) {
        throw new IllegalArgumentException("maxTotal should more then or equals maxIdle");
      }

      if (minIdle > maxTotal || minIdle > maxIdle) {
        throw new IllegalArgumentException(
            "maxTotal and maxIdle should more then or equals minIdle");
      }
      return new ConnectionPoolConfigBuilderDemo(this);
    }

    public Builder setUrl(String url) {
      isEmpty("url", url);
      this.url = url;
      return this;
    }

    public Builder setUsername(String username) {
      isEmpty("username", username);
      this.username = username;
      return this;
    }

    public Builder setPassword(String password) {
      this.password = password;
      return this;
    }

    public Builder setDriveClassName(String driveClassName) {
      isEmpty("driveClassName", driveClassName);
      this.driveClassName = driveClassName;
      return this;
    }

    public Builder setMaxTotal(int maxTotal) {
      if (maxTotal <= 0) {
        throw new IllegalArgumentException("maxTotal should be positive!");
      }
      this.maxTotal = maxTotal;
      return this;
    }

    public Builder setMaxIdle(int maxIdle) {
      if (maxIdle < 0) {
        throw new IllegalArgumentException("maxIdle should not be negative!");
      }
      this.maxIdle = maxIdle;
      return this;
    }

    public Builder setMinIdle(int minIdle) {
      if (minIdle < 0) {
        throw new IllegalArgumentException("minIdle should not be negative!");
      }
      this.minIdle = minIdle;
      return this;
    }
  }
}
