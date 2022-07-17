package com.shawn.study.deep.in.java.jdbc;

import static com.shawn.study.deep.in.java.jdbc.SQLStatementConstants.CREATE_USERS_TABLE_DDL_SQL;
import static com.shawn.study.deep.in.java.jdbc.SQLStatementConstants.DELETE_USER_BY_ID;
import static com.shawn.study.deep.in.java.jdbc.SQLStatementConstants.DROP_USERS_TABLE_DDL_SQL;
import static com.shawn.study.deep.in.java.jdbc.SQLStatementConstants.INSERT_USER_DML_SQL;
import static com.shawn.study.deep.in.java.jdbc.SQLStatementConstants.SELECT_ALL_USERS;
import static com.shawn.study.deep.in.java.jdbc.SQLStatementConstants.SELECT_USER_BY_ID;
import static com.shawn.study.deep.in.java.jdbc.SQLStatementConstants.UPDATE_PASSWORD_BY_ID;

import com.shawn.study.deep.in.java.jdbc.template.JdbcTemplate;
import java.util.List;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class JdbcTemplateTests {
  private static JdbcTemplate jdbcTemplate;

  @BeforeClass
  public static void init() throws Exception {
    var driverDemo = new DriverDemo();
    jdbcTemplate = new JdbcTemplate(driverDemo.getConnection());
    jdbcTemplate.execute(CREATE_USERS_TABLE_DDL_SQL);
  }

  @AfterClass
  public static void destroy() {
    if (jdbcTemplate != null) {
      jdbcTemplate.execute(DROP_USERS_TABLE_DDL_SQL);
      jdbcTemplate.close();
    }
  }

  @Test
  public void test_insert() {
    Object[] params = new Object[] {"Shawn", "123456", 26, "SHANGHAI"};
    int insert = jdbcTemplate.update(INSERT_USER_DML_SQL, null);
    Assert.assertEquals(5, insert);
  }

  @Test
  public void test_delete() {
    Object[] params = new Object[] {1};
    int insert = jdbcTemplate.update(DELETE_USER_BY_ID, params);
    Assert.assertEquals(1, insert);
  }

  @Test
  public void test_update() {
    Object[] updateParams = new Object[] {2, "123456"};
    int update = jdbcTemplate.update(UPDATE_PASSWORD_BY_ID, updateParams);
    Assert.assertEquals(1, update);
  }

  @Test
  public void test_query_list() {
    List<User> users =
        jdbcTemplate.queryForList(
            SELECT_ALL_USERS,
            null,
            (rs, num) -> {
              User user = new User();
              user.setId(rs.getInt(1));
              user.setName(rs.getString(2));
              user.setPassword(rs.getString(3));
              user.setEmail(rs.getString(4));
              user.setPhoneNumber(rs.getString(5));
              return user;
            });
    Assert.assertNotNull(users);
    Assert.assertFalse(users.isEmpty());
  }

  @Test
  public void test_query_object() {
    Object[] params = new Object[] {2};
    User u =
        jdbcTemplate.queryForObject(
            SELECT_USER_BY_ID,
            params,
            (rs, num) -> {
              User user = new User();
              user.setId(rs.getInt(1));
              user.setName(rs.getString(2));
              user.setPassword(rs.getString(3));
              user.setEmail(rs.getString(4));
              user.setPhoneNumber(rs.getString(5));
              return user;
            });
    Assert.assertEquals("B", u.getName());
  }
}
