package com.shawn.study.java.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test2 {

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
          "select column_name, column_type, column_key from information_schema.columns where table_name = '%s' and table_schema = 'hypc-backend'";
      Map<String, List<Map<String, String>>> tableMap = new HashMap<>();
      for (String table : tables) {
        ResultSet rs = statement.executeQuery(String.format(selectSQL, table));
        List<Map<String, String>> columnMapList = new ArrayList<>();
        while (rs.next()) {
          Map<String, String> columnMap = new HashMap<>();
          String columnName = rs.getString(1);
          columnMap.put(columnName, "comments");
          columnMapList.add(columnMap);
        }
        tableMap.put(table, columnMapList);
      }
      System.out.println(JsonUtil.toJson(tableMap).get());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
