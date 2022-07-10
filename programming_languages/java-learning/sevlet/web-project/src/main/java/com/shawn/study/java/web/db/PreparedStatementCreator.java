package com.shawn.study.java.web.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PreparedStatementCreator {
  PreparedStatement createPreparedStatement(Connection con) throws SQLException;
}
