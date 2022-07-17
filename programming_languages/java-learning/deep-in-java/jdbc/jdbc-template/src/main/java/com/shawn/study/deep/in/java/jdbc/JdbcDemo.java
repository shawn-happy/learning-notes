package com.shawn.study.deep.in.java.jdbc;

import static com.shawn.study.deep.in.java.jdbc.SQLStatementConstants.CREATE_USERS_TABLE_DDL_SQL;
import static com.shawn.study.deep.in.java.jdbc.SQLStatementConstants.DELETE_USER_BY_ID;
import static com.shawn.study.deep.in.java.jdbc.SQLStatementConstants.DROP_USERS_TABLE_DDL_SQL;
import static com.shawn.study.deep.in.java.jdbc.SQLStatementConstants.INSERT_USER_DML_SQL;
import static com.shawn.study.deep.in.java.jdbc.SQLStatementConstants.SELECT_ALL_USERS;
import static com.shawn.study.deep.in.java.jdbc.SQLStatementConstants.SELECT_USER_BY_ID;
import static com.shawn.study.deep.in.java.jdbc.SQLStatementConstants.UPDATE_PASSWORD_BY_ID;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JdbcDemo {

  private final DriverDemo demo = new DriverDemo();

  public boolean dropTable() {
    try (Connection connection = demo.getConnection();
        Statement statement = connection.createStatement()) {
      return statement.execute(DROP_USERS_TABLE_DDL_SQL);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean createTable() {
    try (Connection connection = demo.getConnection();
        Statement statement = connection.createStatement()) {
      return statement.execute(CREATE_USERS_TABLE_DDL_SQL);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public int insert() {
    try (Connection connection = demo.getConnection();
        Statement statement = connection.createStatement()) {
      return statement.executeUpdate(INSERT_USER_DML_SQL);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public List<User> findAll() {
    try (Connection connection = demo.getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(SELECT_ALL_USERS)) {
      List<User> users = new ArrayList<>();
      while (rs.next()) {
        int id = rs.getInt(1);
        String name = rs.getString(2);
        String password = rs.getString(3);
        String email = rs.getString(4);
        String phoneNumber = rs.getString(5);
        users.add(new User(id, name, password, email, phoneNumber));
      }
      return users;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public int update(int id, String password) {
    try (Connection connection = demo.getConnection();
        PreparedStatement ps = connection.prepareStatement(UPDATE_PASSWORD_BY_ID)) {
      ps.setString(1, password);
      ps.setInt(2, id);
      return ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public User findById(int id) {
    try (Connection connection = demo.getConnection();
        PreparedStatement ps = connection.prepareStatement(SELECT_USER_BY_ID)) {
      ps.setInt(1, id);
      ResultSet rs = ps.executeQuery();
      List<User> users = new ArrayList<>();
      while (rs.next()) {
        int userId = rs.getInt(1);
        String name = rs.getString(2);
        String password = rs.getString(3);
        String email = rs.getString(4);
        String phoneNumber = rs.getString(5);
        users.add(new User(userId, name, password, email, phoneNumber));
      }
      rs.close();
      return users.isEmpty() ? null : users.iterator().next();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public int delete(int id) {
    try (Connection connection = demo.getConnection();
        PreparedStatement ps = connection.prepareStatement(DELETE_USER_BY_ID)) {
      ps.setInt(1, id);
      return ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public int deleteWithTransaction(int id) {
    Connection connection = null;
    PreparedStatement ps = null;
    try {
      connection = demo.getConnection();
      ps = connection.prepareStatement(DELETE_USER_BY_ID);
      connection.setAutoCommit(false);
      ps.setInt(1, id);
      int i = ps.executeUpdate();
      double d = 1 / 0;
      connection.commit();
      return i;
    } catch (SQLException e) {
      try {
        if (connection != null) {
          connection.rollback();
        }
      } catch (SQLException rollbackException) {
        throw new RuntimeException(rollbackException);
      }
      throw new RuntimeException(e);
    } finally {
      try {
        if (ps != null) {
          ps.close();
        }
        if (connection != null) {
          connection.close();
        }
      } catch (SQLException rollbackException) {
        throw new RuntimeException(rollbackException);
      }
    }
  }
}
