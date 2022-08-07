package com.shawn.study.deep.in.java.design.jdbc.v3;

import com.shawn.study.deep.in.java.design.jdbc.core.RowMapper;
import com.shawn.study.deep.in.java.design.jdbc.v2.JdbcUtil;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 重构 {@link JdbcUtil} 主要封装成一个DML和DDL模板
 *
 * @author shawn
 * @since 2020/8/16
 */
public class JdbcTemplate {
  private static String url;
  private static String username;
  private static String password;
  private static String driverClassName;

  private JdbcTemplate() {}

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

  /**
   * select
   *
   * @param sql
   * @param params
   * @return
   */
  public static <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... params) {
    Connection connection = getConnection();
    PreparedStatement ps = null;
    ResultSet rs = null;
    List<T> list = new ArrayList<>();
    try {
      ps = connection.prepareStatement(sql);
      for (int i = 0; i < params.length; i++) {
        ps.setObject((i + 1), params[i]);
      }
      rs = ps.executeQuery();
      int rowNum = 0;
      while (rs.next()) {
        list.add(rowMapper.rowMapper(rs, rowNum++));
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      close(connection, ps, rs);
    }
    return list;
  }

  /**
   * insert, delete, update
   *
   * @param sql
   * @param params
   */
  public static void update(String sql, Object... params) {
    Connection connection = getConnection();
    PreparedStatement ps = null;
    try {
      ps = connection.prepareStatement(sql);
      for (int i = 0; i < params.length; i++) {
        ps.setObject((i + 1), params[i]);
      }
      ps.execute();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      close(connection, ps, null);
    }
  }
}
