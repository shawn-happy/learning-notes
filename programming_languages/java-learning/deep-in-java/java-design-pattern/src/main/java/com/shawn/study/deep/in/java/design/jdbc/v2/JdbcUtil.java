package com.shawn.study.deep.in.java.design.jdbc.v2;

import com.shawn.study.deep.in.java.design.jdbc.v1.JdbcOperator;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 重构 {@link JdbcOperator}
 *
 * @author shawn
 * @since 2020/8/14
 */
public class JdbcUtil {

  private static String url;
  private static String username;
  private static String password;
  private static String driverClassName;

  private JdbcUtil() {}

  static {
    try {
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      InputStream inputStream = classLoader.getResourceAsStream("jdbc.properties");
      Properties properties = new Properties();
      properties.load(inputStream);
      url = properties.getProperty("jdbc.url");
      username = properties.getProperty("jdbc.username");
      password = properties.getProperty("jdbc.password");
      driverClassName = properties.getProperty("jdbc.driverClassName");
      Class.forName(driverClassName);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static Connection getConnection() {
    try {
      return DriverManager.getConnection(url, username, password);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static final void close(Connection con, PreparedStatement ps, ResultSet rs) {
    try {
      if (rs != null) {
        rs.close();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (ps != null) {
          ps.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      } finally {
        try {
          if (con != null) {
            con.close();
          }
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
