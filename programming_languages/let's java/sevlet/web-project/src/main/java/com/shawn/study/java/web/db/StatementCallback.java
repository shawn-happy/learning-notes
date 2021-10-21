package com.shawn.study.java.web.db;

import java.sql.SQLException;
import java.sql.Statement;

@FunctionalInterface
public interface StatementCallback<T> {

  T doInStatement(Statement statement) throws SQLException;
}
