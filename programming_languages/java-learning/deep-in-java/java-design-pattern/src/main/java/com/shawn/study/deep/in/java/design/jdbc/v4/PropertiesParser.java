package com.shawn.study.deep.in.java.design.jdbc.v4;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author shawn
 * @since 2020/8/23
 */
public class PropertiesParser implements Parser {

  private static final String propertiesName = "jdbc.properties";

  public PropertiesParser(){
    super();
  }

  @Override
  public DataSourcePoolConfig parse() {
    InputStream inputStream =
        Thread.currentThread().getContextClassLoader().getResourceAsStream(propertiesName);
    Properties p = new Properties();
    try {
      p.load(inputStream);
    } catch (IOException e) {
      e.printStackTrace();
    }
    String url = p.getProperty("jdbc.url");
    String username = p.getProperty("jdbc.username");
    String password = p.getProperty("jdbc.password");
    String driverClassName = p.getProperty("jdbc.driverClassName");
    int maxActive =
        parseInt(
            p,
            "jdbc.maxActive",
            DataSourceConstant.DEFAULT_MAX_CONNECTION,
            "illegal property 'jdbc.maxActive'");
    int minIdle =
        parseInt(
            p,
            "jdbc.minIdle",
            DataSourceConstant.DEFAULT_MIN_IDLE,
            "illegal property 'jdbc.minIdle'");
    int initSize =
        parseInt(
            p,
            "jdbc.initSize",
            DataSourceConstant.DEFAULT_INIT_SIZE,
            "illegal property 'jdbc.initSize'");
    return
        DataSourcePoolConfig.builder()
            .url(url)
            .username(username)
            .password(password)
            .driverClassName(driverClassName)
            .maxActive(maxActive)
            .minIdle(minIdle)
            .initSize(initSize)
            .build();
  }

  private int parseInt(Properties p, String key, int defaultValue, String errorMsg) {
    String property = p.getProperty("jdbc.initSize");
    int value = 0;
    if (property == null || property.trim().isEmpty()) {
      value = defaultValue;
    } else {
      try {
        value = Integer.parseInt(key);
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException(errorMsg);
      }
    }
    return value;
  }
}
