package com.shawn.study.deep.in.java.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class JdbcDemoTests {

  private static final JdbcDemo demo = new JdbcDemo();

  @BeforeClass
  public static void createTable() {
    boolean table = demo.createTable();
    assertFalse(table);
  }

  @AfterClass
  public static void dropTable() {
    boolean dropTable = demo.dropTable();
    assertFalse(dropTable);
  }

  @Test
  public void jdbcTests() {
    int insert = demo.insert();
    assertEquals(5, insert);

    List<User> allUsers = demo.findAll();
    assertNotNull(allUsers);
    assertEquals(5, allUsers.size());

    int update = demo.update(1, "123456");
    assertEquals(1, update);

    User user = demo.findById(1);
    assertNotNull(user);
    assertEquals("123456", user.getPassword());

    int delete = demo.delete(1);
    assertEquals(1, delete);

    user = demo.findById(1);
    assertNull(user);

    try {
      demo.deleteWithTransaction(2);
    } catch (Exception e) {
      User user2 = demo.findById(2);
      assertNotNull(user2);
    }
  }
}
