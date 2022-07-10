package com.shawn.study.java.jdbc;

import java.sql.SQLException;
import java.sql.Statement;

@FunctionalInterface
public interface StatementCallback<T> {

  T doInStatement(Statement statement) throws SQLException;
}
