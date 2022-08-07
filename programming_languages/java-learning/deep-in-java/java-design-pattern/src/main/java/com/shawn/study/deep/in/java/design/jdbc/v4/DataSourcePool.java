package com.shawn.study.deep.in.java.design.jdbc.v4;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shawn
 * @since 2020/8/16
 */
public class DataSourcePool {

  private static Logger log = LoggerFactory.getLogger(DataSourcePool.class);

  private static ThreadLocal<List<Connection>> localDs = new ThreadLocal<>();

  private final Parser parser;

  private final DataSourcePoolConfig config;

  private Connection conn = null;

  private static volatile DataSourcePool INSTANCE;

  private DataSourcePool(Parser parser) {
    this.parser = parser;
    config = parser.parse();
  }

  public static DataSourcePool getInstance() {
    if (INSTANCE == null) {
      synchronized (DataSourcePool.class) {
        if (INSTANCE == null) {
          return new DataSourcePool(new PropertiesParser());
        }
      }
    }
    return INSTANCE;
  }

  public Connection getConnection() {
    List<Connection> operlst = localDs.get();
    if (operlst == null) {
      operlst = new ArrayList<>();
      localDs.set(operlst);
    }
    if (operlst.size() == 0) {
      operlst.add(getInstance().newConnection());
      return operlst.get(0);
    } else {
      return getInstance().newConnection();
    }
  }

  private Connection newConnection() {
    if (conn == null) {
      try {
        DriverManager.registerDriver(
            Driver.class.cast(Class.forName(config.getDriverClassName()).newInstance()));
        conn =
            DriverManager.getConnection(
                config.getUrl(), config.getUsername(), config.getPassword());
      } catch (Exception e) {
        log.error("获取连接出错", e);
        throw new RuntimeException(e);
      }
    }
    return conn;
  }

  public void release() {
    try {
      if (conn == null || conn.isClosed()) return;
      conn.close();
    } catch (SQLException e) {
      log.error("关闭连接失败", e);
    } finally {
      List<Connection> operlst = localDs.get();
      if (operlst != null) {
        for (int i = 0; i < operlst.size(); i++) {
          if (operlst.get(i) == conn) {
            operlst.remove(i);
            i--;
          }
        }
      }
    }
  }

  public void closeAll() {
    List<Connection> operlst = localDs.get();
    if (operlst == null) return;
    try {
      for (Connection item : operlst) {
        if (item == null || item.isClosed()) {
          continue;
        }
        item.close();
      }
    } catch (SQLException e) {
      log.error("关闭连接失败", e);
    }
    operlst.clear();
  }
}
