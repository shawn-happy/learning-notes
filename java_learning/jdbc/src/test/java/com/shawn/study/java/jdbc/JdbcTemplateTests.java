package com.shawn.study.java.jdbc;

import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JdbcTemplateTests {

  private JdbcTemplate jdbcTemplate;

  private static final String URL =
      "jdbc:mysql://127.0.0.1:3306/practice?useUnicode=true&characterEncoding=UTF-8&useSSL=false&autoReconnect=true&failOverReadOnly=false&serverTimezone=GMT%2B8";
  private static final String USERNAME = "root";
  private static final String PASSWORD = "123456";
  private static final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";

  private static final String CREATE_TABLE_USER =
      "CREATE TABLE IF NOT EXISTS user("
          + "id INT(11) PRIMARY KEY AUTO_INCREMENT,"
          + "username VARCHAR(45) NOT NULL,"
          + "password VARCHAR(45) NOT NULL,"
          + "age INT(11) NOT NULL,"
          + "address VARCHAR(45) NOT NULL"
          + ")";

  private static final String DROP_TABLE_USER = "DROP TABLE IF EXISTS user";

  private static final String DROP_TABLE_ERROR = "DROP TABLE a";

  private static final String INSERT_USER =
      "INSERT INTO user (username, password, age, address) values (?,?,?,?)";

  private static final String DELETE_USER_BY_USERNAME = "DELETE FROM user WHERE username = ?";

  private static final String UPDATE_USER_AGE = "UPDATE user SET age = ? WHERE username = ?";

  private static final String QUERY_ALL_USERS =
      "SELECT id, username, password, age, address FROM user";

  private static final String QUERY_USER_BY_ID =
      "SELECT id, username, password, age, address FROM user WHERE id = ?";

  @Before
  public void init() {
    jdbcTemplate = new JdbcTemplate(URL, USERNAME, PASSWORD, DRIVER_CLASS_NAME);
    jdbcTemplate.execute(CREATE_TABLE_USER);
  }

  @After
  public void destroy() {
    if (jdbcTemplate != null) {
      jdbcTemplate.execute(DROP_TABLE_USER);
      jdbcTemplate.close();
    }
  }

  @Test
  public void test_create_table() {
    jdbcTemplate.execute(CREATE_TABLE_USER);
  }

  @Test
  public void test_drop_table() {
    jdbcTemplate.execute(CREATE_TABLE_USER);
    jdbcTemplate.execute(DROP_TABLE_USER);
  }

  @Test(expected = RuntimeException.class)
  public void test_drop_table_exception() {
    jdbcTemplate.execute(DROP_TABLE_ERROR);
  }

  @Test
  public void test_insert() {
    Object[] params = new Object[] {"Shawn", "123456", 26, "SHANGHAI"};
    int insert = jdbcTemplate.update(INSERT_USER, params);
    Assert.assertEquals(1, insert);
  }

  @Test
  public void test_delete() {
    Object[] params = new Object[] {"Shawn", "123456", 26, "SHANGHAI"};
    int insert = jdbcTemplate.update(INSERT_USER, params);
    Assert.assertEquals(1, insert);

    Object[] deleteParams = new Object[] {"Shawn"};
    int delete = jdbcTemplate.update(DELETE_USER_BY_USERNAME, deleteParams);
    Assert.assertEquals(1, delete);
  }

  @Test
  public void test_update() {
    Object[] params = new Object[] {"Shawn", "123456", 26, "SHANGHAI"};
    int insert = jdbcTemplate.update(INSERT_USER, params);
    Assert.assertEquals(1, insert);
    Object[] updateParams = new Object[] {27, "Shawn"};
    int update = jdbcTemplate.update(UPDATE_USER_AGE, updateParams);
    Assert.assertEquals(1, update);
  }

  @Test
  public void test_query_list() {
    Object[] params = new Object[] {"Shawn", "123456", 26, "SHANGHAI"};
    int insert = jdbcTemplate.update(INSERT_USER, params);
    Assert.assertEquals(1, insert);

    params = new Object[] {"Jack", "654321", 27, "BEIJING"};
    insert = jdbcTemplate.update(INSERT_USER, params);
    Assert.assertEquals(1, insert);

    List<User> users =
        jdbcTemplate.queryForList(
            QUERY_ALL_USERS,
            null,
            (rs, num) -> {
              User user = new User();
              user.setAge(rs.getInt(1));
              user.setUsername(rs.getString(2));
              user.setPassword(rs.getString(3));
              user.setAge(rs.getInt(4));
              user.setAddress(rs.getString(5));
              return user;
            });
    Assert.assertEquals(2, users.size());
  }

  @Test
  public void test_query_object() {
    Object[] params = new Object[] {"Shawn", "123456", 26, "SHANGHAI"};
    int insert = jdbcTemplate.update(INSERT_USER, params);
    Assert.assertEquals(1, insert);

    params = new Object[] {"Jack", "654321", 27, "BEIJING"};
    insert = jdbcTemplate.update(INSERT_USER, params);
    Assert.assertEquals(1, insert);

    params = new Object[] {2};
    User u =
        jdbcTemplate.queryForObject(
            QUERY_USER_BY_ID,
            params,
            (rs, num) -> {
              User user = new User();
              user.setAge(rs.getInt(1));
              user.setUsername(rs.getString(2));
              user.setPassword(rs.getString(3));
              user.setAge(rs.getInt(4));
              user.setAddress(rs.getString(5));
              return user;
            });
    Assert.assertEquals("Jack", u.getUsername());
  }

  @Test
  public void test_execute_query() {
    Object[] params = new Object[] {"Shawn", "123456", 26, "SHANGHAI"};
    int insert = jdbcTemplate.update(INSERT_USER, params);
    Assert.assertEquals(1, insert);

    params = new Object[] {"Jack", "654321", 27, "BEIJING"};
    insert = jdbcTemplate.update(INSERT_USER, params);
    Assert.assertEquals(1, insert);

    List<User> users =
        jdbcTemplate.executeQuery(
            QUERY_ALL_USERS,
            (rs, num) -> {
              User user = new User();
              user.setAge(rs.getInt(1));
              user.setUsername(rs.getString(2));
              user.setPassword(rs.getString(3));
              user.setAge(rs.getInt(4));
              user.setAddress(rs.getString(5));
              return user;
            });
    Assert.assertEquals(2, users.size());
  }

  @Test(expected = RuntimeException.class)
  public void test_query_object_results_gt_one() {
    Object[] params = new Object[] {"Shawn", "123456", 26, "SHANGHAI"};
    int insert = jdbcTemplate.update(INSERT_USER, params);
    Assert.assertEquals(1, insert);

    params = new Object[] {"Jack", "654321", 27, "BEIJING"};
    insert = jdbcTemplate.update(INSERT_USER, params);
    Assert.assertEquals(1, insert);

    User u =
        jdbcTemplate.queryForObject(
            QUERY_ALL_USERS,
            null,
            (rs, num) -> {
              User user = new User();
              user.setAge(rs.getInt(1));
              user.setUsername(rs.getString(2));
              user.setPassword(rs.getString(3));
              user.setAge(rs.getInt(4));
              user.setAddress(rs.getString(5));
              return user;
            });
    Assert.assertEquals("Shawn", u.getUsername());
  }

  @Test(expected = RuntimeException.class)
  public void test_query_object_results_equals_zero() {
    Object[] params = new Object[] {"Shawn", "123456", 26, "SHANGHAI"};
    int insert = jdbcTemplate.update(INSERT_USER, params);
    Assert.assertEquals(1, insert);

    params = new Object[] {"Jack", "654321", 27, "BEIJING"};
    insert = jdbcTemplate.update(INSERT_USER, params);
    Assert.assertEquals(1, insert);

    params = new Object[] {3};
    User u =
        jdbcTemplate.queryForObject(
            QUERY_USER_BY_ID,
            params,
            (rs, num) -> {
              User user = new User();
              user.setAge(rs.getInt(1));
              user.setUsername(rs.getString(2));
              user.setPassword(rs.getString(3));
              user.setAge(rs.getInt(4));
              user.setAddress(rs.getString(5));
              return user;
            });
  }
}
