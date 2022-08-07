package com.shawn.study.deep.in.java.design.jdbc.v4;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author shawn
 * @since 2020/8/16
 */
public class DatasourceUtil {

  public static final void close(Statement ps, ResultSet rs) {
    try {
      if (ps != null && !ps.isClosed()) {
        ps.close();
      }
      if (rs != null && !rs.isClosed()) {
        rs.close();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static final void close(Statement ps) {
    close(ps, null);
  }
}
