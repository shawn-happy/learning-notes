package com.shawn.study.deep.in.java.design.jdbc.core;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author shawn
 * @since 2020/8/16
 */
public interface RowMapper<T> {
  T rowMapper(ResultSet rs, int rowNum) throws SQLException;
}
