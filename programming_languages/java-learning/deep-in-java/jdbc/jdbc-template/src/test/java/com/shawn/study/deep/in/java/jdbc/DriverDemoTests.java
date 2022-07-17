package com.shawn.study.deep.in.java.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.Driver;
import java.util.Enumeration;
import org.junit.Test;

public class DriverDemoTests {

  private final DriverDemo demo = new DriverDemo();

  @Test
  public void testGetDriverByClassLoader() throws Exception {
    Enumeration<Driver> drivers = demo.getDriverByClassLoader();
    while (drivers.hasMoreElements()) {
      Driver driver = drivers.nextElement();
      if (driver instanceof org.h2.Driver) {
        assertNotNull(driver);
        int majorVersion = driver.getMajorVersion();
        assertEquals(2, majorVersion);
        int minorVersion = driver.getMinorVersion();
        assertEquals(1, minorVersion);
      }
    }
  }

  @Test
  public void testGetDriverBySystemProperties() throws Exception {
    Enumeration<Driver> drivers = demo.getDriverBySystemProperties();
    while (drivers.hasMoreElements()) {
      Driver driver = drivers.nextElement();
      if (driver instanceof org.h2.Driver) {
        assertNotNull(driver);
        int majorVersion = driver.getMajorVersion();
        assertEquals(2, majorVersion);
        int minorVersion = driver.getMinorVersion();
        assertEquals(1, minorVersion);
      }
    }
  }

  @Test
  public void testGetDriverBySPI() throws Exception {
    Enumeration<Driver> drivers = demo.getDriverBySPI();
    while (drivers.hasMoreElements()) {
      Driver driver = drivers.nextElement();
      if (driver instanceof org.h2.Driver) {
        assertNotNull(driver);
        int majorVersion = driver.getMajorVersion();
        assertEquals(2, majorVersion);
        int minorVersion = driver.getMinorVersion();
        assertEquals(1, minorVersion);
      }
    }
  }

  @Test
  public void testGetConnection() throws Exception {
    Connection connection = demo.getConnection();
    assertNotNull(connection);
    boolean closed = connection.isClosed();
    assertFalse(closed);
    connection.close();
    assertTrue(connection.isClosed());
  }
}
