package com.shawn.design.jdbc.v4;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shawn
 * @since 2020/8/23
 */
public class SqlHelper {

  public static <T> List<T> rowMapper(ResultSet rst, Class<T> clazz) {
    List<T> res = new ArrayList<>();
    try {
      if (rst != null && !rst.isClosed()) {
        while (rst.next()) {
          res.add(mapper(rst, clazz));
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return res;
  }

  public static <T> List<T> rowMapper(ResultSet rst, int pageNum, int pageSize, Class<T> clazz) {
    List<T> res = new ArrayList<>();
    int startIndex = pageNum * pageSize;
    try {
      if (startIndex > 0) {
        // 跳到指定行
        rst.absolute(startIndex + 1);
      }
      int p = 0;
      while (rst.next()) {
        res.add(mapper(rst, clazz));
        p++;
        if (p >= pageSize) {
          break;
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return res;
  }

  private static <T> T mapper(ResultSet rst, Class<T> clazz) throws Exception {
    BaseEntity obj = (BaseEntity) clazz.newInstance();
    ResultSetMetaData rsmd = rst.getMetaData();
    int cols = rsmd.getColumnCount();
    for (int i = 1; i <= cols; i++) {
      obj.put(rsmd.getColumnLabel(i), rst.getObject(i));
    }
    obj.AcceptChanges(); // 首次读出后数据状态不变
    return (T) obj;
  }
}
