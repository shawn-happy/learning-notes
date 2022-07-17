package com.shawn.study.deep.in.java.jdbc.template;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PreparedStatementCallback<T> {
  T doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException;
}
