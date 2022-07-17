package com.shawn.study.deep.in.java.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Logger;

public class DriverDemo {

  private static final Logger LOGGER = Logger.getLogger(DriverDemo.class.getName());

  public Enumeration<Driver> getDriverByClassLoader() throws Exception {
    Class.forName("org.h2.Driver");
    printDriverCount(DriverManager.getDrivers());
    return DriverManager.getDrivers();
  }

  public Enumeration<Driver> getDriverBySystemProperties() {
    System.setProperty("jdbc.drivers", "org.h2.Driver");
    printDriverCount(DriverManager.getDrivers());
    return DriverManager.getDrivers();
  }

  public Enumeration<Driver> getDriverBySPI() {
    printDriverCount(DriverManager.getDrivers());
    return DriverManager.getDrivers();
  }

  public Connection getConnection() throws SQLException {
    return DriverManager.getConnection("jdbc:h2:~/test", "root", "root");
  }

  private void printDriverCount(Enumeration<Driver> drivers) {
    int i = 0;
    while (drivers.hasMoreElements()) {
      drivers.nextElement();
      i++;
    }
    LOGGER.info("driver count: " + i);
  }
}
