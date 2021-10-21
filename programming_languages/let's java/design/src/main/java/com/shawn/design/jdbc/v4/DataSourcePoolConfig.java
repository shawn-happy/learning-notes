package com.shawn.design.jdbc.v4;

/**
 * @author shawn
 * @since 2020/8/16
 */
public class DataSourcePoolConfig {

  private String url;

  private String username;

  private String password;

  private String driverClassName;

  private int maxActive;

  private int minIdle;

  private int initSize;

  public String getUrl() {
    return url;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getDriverClassName() {
    return driverClassName;
  }

  public int getMaxActive() {
    return maxActive;
  }

  public int getMinIdle() {
    return minIdle;
  }

  public int getInitSize() {
    return initSize;
  }

  public static DataSourcePoolConfigBuilder builder() {
    return new DataSourcePoolConfigBuilder();
  }

  public static class DataSourcePoolConfigBuilder {

    private String url;

    private String username;

    private String password;

    private String driverClassName;

    /** 最大连接数 */
    private int maxActive;

    /** 最小空闲数 */
    private int minIdle;

    private int initSize;

    public DataSourcePoolConfigBuilder url(String url) {
      if (url == null || url.trim().isEmpty()) {
        throw new IllegalArgumentException("url can not be null");
      }
      this.url = url.trim();
      return this;
    }

    public DataSourcePoolConfigBuilder username(String username) {
      if (username == null || username.trim().isEmpty()) {
        throw new IllegalArgumentException("username can not be null");
      }
      this.username = username.trim();
      return this;
    }

    public DataSourcePoolConfigBuilder password(String password) {
      if (password == null || password.trim().isEmpty()) {
        throw new IllegalArgumentException("password can not be null");
      }
      this.password = password.trim();
      return this;
    }

    public DataSourcePoolConfigBuilder driverClassName(String driverClassName) {
      if (driverClassName == null || driverClassName.trim().isEmpty()) {
        throw new IllegalArgumentException("driverClassName can not be null");
      }
      this.driverClassName = driverClassName.trim();
      return this;
    }

    public DataSourcePoolConfigBuilder maxActive(int maxActive) {
      if (maxActive == 0) {
        throw new IllegalArgumentException("maxActive can not set zero");
      }
      if (maxActive < 0) {
        throw new IllegalArgumentException("maxActive must > 0");
      }
      if (maxActive < this.minIdle) {
        throw new IllegalArgumentException(
            "maxActive less than minIdle, " + maxActive + " < " + this.minIdle);
      }
      this.maxActive = maxActive;
      return this;
    }

    public DataSourcePoolConfigBuilder minIdle(int minIdle) {
      if (minIdle > this.maxActive) {
        throw new IllegalArgumentException(
            "minIdle greater than maxActive, " + maxActive + " < " + this.minIdle);
      }

      if (minIdle < 0) {
        throw new IllegalArgumentException("minIdle must > 0");
      }
      this.minIdle = minIdle;
      return this;
    }

    public DataSourcePoolConfigBuilder initSize(int initSize) {
      if (initSize > this.maxActive) {
        throw new IllegalArgumentException(
            "initSize greater than maxActive, " + maxActive + " < " + this.initSize);
      }

      if (initSize < 0) {
        throw new IllegalArgumentException("initSize must > 0");
      }
      this.initSize = initSize;
      return this;
    }

    public DataSourcePoolConfig build() {
      DataSourcePoolConfig dataSourceConfig = new DataSourcePoolConfig();
      dataSourceConfig.url = this.url;
      dataSourceConfig.username = this.username;
      dataSourceConfig.password = this.password;
      dataSourceConfig.driverClassName = this.driverClassName;
      dataSourceConfig.maxActive = this.maxActive;
      dataSourceConfig.minIdle = this.minIdle;
      dataSourceConfig.initSize = this.initSize;
      return dataSourceConfig;
    }
  }
}
