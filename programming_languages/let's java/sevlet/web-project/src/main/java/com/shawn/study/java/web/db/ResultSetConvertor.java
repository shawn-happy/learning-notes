package com.shawn.study.java.web.db;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetConvertor<T> {

  T convert(ResultSet rs) throws SQLException;
}
