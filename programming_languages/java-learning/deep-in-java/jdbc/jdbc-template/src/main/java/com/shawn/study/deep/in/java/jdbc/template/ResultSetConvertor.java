package com.shawn.study.deep.in.java.jdbc.template;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetConvertor<T> {

  T convert(ResultSet rs) throws SQLException;
}
