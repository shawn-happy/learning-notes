package com.shawn.study.deep.in.java.jdbc;

public class SQLStatementConstants {

  public static final String DROP_USERS_TABLE_DDL_SQL = "DROP TABLE users";

  public static final String CREATE_USERS_TABLE_DDL_SQL =
      "CREATE TABLE users"
          + "("
          + "    id          INT         NOT NULL PRIMARY KEY,"
          + "    name        VARCHAR(16) NOT NULL,"
          + "    password    VARCHAR(64) NOT NULL,"
          + "    email       VARCHAR(64) NOT NULL,"
          + "    phoneNumber VARCHAR(32) NOT NULL"
          + ")";

  public static final String INSERT_USER_DML_SQL =
      "INSERT INTO users(id, name, password, email, phoneNumber) VALUES "
          + "(1, 'A','******','a@gmail.com','1') , "
          + "(2, 'B','******','b@gmail.com','2') , "
          + "(3, 'C','******','c@gmail.com','3') , "
          + "(4, 'D','******','d@gmail.com','4') , "
          + "(5, 'E','******','e@gmail.com','5')";

  public static final String SELECT_ALL_USERS = "SELECT * FROM users";

  public static final String UPDATE_PASSWORD_BY_ID = "UPDATE users SET password = ? WHERE id = ?";

  public static final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE id = ?";

  public static final String DELETE_USER_BY_ID = "DELETE FROM users WHERE id = ?";
}
