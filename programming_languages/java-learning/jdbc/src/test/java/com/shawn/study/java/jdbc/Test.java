package com.shawn.study.java.jdbc;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Test {
  public static void main(String[] args) {
    String url = "jdbc:mysql://172.27.67.41:33306/hypc-backend?serverTimezone=GMT%2B8";
    String username = "root";
    String password = "4paradigm";
    String driverClassName = "com.mysql.cj.jdbc.Driver";

    Connection connection = null;
    try {
      Class.forName(driverClassName);
      connection = DriverManager.getConnection(url, username, password);
      String sql = "show tables";
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery(sql);
      List<String> tables = new ArrayList<>();
      List<String> excludes =
          Arrays.asList(
              "cycle_setting",
              "data_stream_extension_relation",
              "data_stream_relation",
              "data_stream_relation_field_binding",
              "explore",
              "explore_history",
              "explore_suite");
      while (resultSet.next()) {
        String table = resultSet.getString(1);
        if (!excludes.contains(table)) {
          tables.add(table);
        }
      }
      String selectSQL =
          "select column_name, column_type, column_key from information_schema.columns where table_name = '%s' and TABLE_SCHEMA = 'hypc-backend'";
      Map<String, List<Map<String, String>>> tableColumnMapListMap = new HashMap<>();
      for (String table : tables) {
        ResultSet rs = statement.executeQuery(String.format(selectSQL, table));
        List<Map<String, String>> columnMapList = new ArrayList<>();
        while (rs.next()) {
          Map<String, String> columnMap = new HashMap<>();
          String columnName = rs.getString(1);
          String columnType = rs.getString(2);
          columnMap.put("column", columnName);
          columnMap.put("type", columnType);
          columnMapList.add(columnMap);
        }
        tableColumnMapListMap.put(table, columnMapList);
      }
      String path = "C:\\Users\\Lenovo\\Desktop\\4paradigm\\hypc-backend.json";
      StringBuilder builder = new StringBuilder();
      try (InputStream is = new FileInputStream(path);
          BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
        String str;
        while ((str = reader.readLine()) != null) {
          builder.append(str);
        }
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      Map map = JsonUtil.fromJson(builder.toString(), Map.class).get();
      String commentSQL = "alter table %s modify %s %s not null comment '%s'";
      List<String> alterSqlList = new ArrayList<>();
      if (!map.isEmpty()) {
        map.forEach(
            (table, columns) -> {
              if (columns instanceof ArrayList) {
                List list = (ArrayList) columns;
                list.forEach(
                    e -> {
                      if (e instanceof LinkedHashMap) {
                        Map columnCommentMap = (LinkedHashMap) e;
                        columnCommentMap.forEach(
                            (column, comment) -> {
                              List<Map<String, String>> columnMapLists =
                                  tableColumnMapListMap.get(table);
                              for (Map<String, String> columnMapList : columnMapLists) {
                                String columnName = columnMapList.get("column");
                                if (column.equals(columnName)) {
                                  String type = columnMapList.get("type");

                                  System.out.printf(
                                      commentSQL + "\n", table, column, type, comment);
                                  alterSqlList.add(
                                      String.format(commentSQL, table, column, type, comment));
                                }
                              }
                            });
                      }
                    });
              }
            });
      }

      alterSqlList.forEach(
          alterSQL -> {
            try {
              statement.execute(sql);
            } catch (SQLException e) {
              throw new RuntimeException(e);
            }
          });
    } catch (ClassNotFoundException | SQLException e) {
      throw new RuntimeException(e);
    } finally {
      close(connection);
    }
  }

  private static void close(AutoCloseable closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }
}
