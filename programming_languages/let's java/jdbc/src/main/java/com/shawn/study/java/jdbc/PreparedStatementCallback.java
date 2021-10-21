package com.shawn.study.java.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PreparedStatementCallback<T> {
  T doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException;
}
