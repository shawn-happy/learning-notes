package com.shawn.study.deep.in.java.jdbc.template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PreparedStatementCreator {
  PreparedStatement createPreparedStatement(Connection con) throws SQLException;
}
