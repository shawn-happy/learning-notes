package com.shawn.study.deep.in.java.jdbc.template;

import java.sql.SQLException;
import java.sql.Statement;

@FunctionalInterface
public interface StatementCallback<T> {

  T doInStatement(Statement statement) throws SQLException;
}
