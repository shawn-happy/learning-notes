package com.shawn.study.java.web.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PreparedStatementCallback<T> {
  T doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException;
}
