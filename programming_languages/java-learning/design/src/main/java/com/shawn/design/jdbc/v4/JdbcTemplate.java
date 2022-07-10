package com.shawn.design.jdbc.v4;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shawn
 * @since 2020/8/16
 */
public class JdbcTemplate {

  public JdbcTemplate() {}

  public <T> List<T> queryList(String sql, Class<T> clazz, Object... args) {
    List<T> ret = new ArrayList<T>();
    PreparedStatement stat = null;
    ResultSet rst = null;
    DataSourcePool pool = DataSourcePool.getInstance();
    try {
      stat = pool.getConnection().prepareStatement(sql);
      // 设置参数
      for (int i = 0; i < args.length; i++) {
        stat.setObject(i + 1, args[i]);
      }
      rst = stat.executeQuery();
      ret = SqlHelper.rowMapper(rst, clazz);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      pool.release();
      DatasourceUtil.close(stat, rst);
    }

    return ret;
  }

  public List<BaseEntity> queryList(String sql, int pageNum, int pageSize, Object... args) {
    return queryList(sql, pageNum, pageSize, BaseEntity.class, args);
  }

  public <T> List<T> queryList(
      String sql, int pageNum, int pageSize, Class<T> clazz, Object... args) {
    List<T> ret = new ArrayList<T>();
    PreparedStatement stat = null;
    ResultSet rst = null;
    DataSourcePool pool = DataSourcePool.getInstance();
    try {
      stat = pool.getConnection().prepareStatement(sql);
      // 设置参数
      for (int i = 0; i < args.length; i++) {
        stat.setObject(i + 1, args[i]);
      }
      rst = stat.executeQuery();
      ret = SqlHelper.rowMapper(rst, pageNum, pageSize, clazz);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      pool.release();
      DatasourceUtil.close(stat, rst);
    }

    return ret;
  }

  public BaseEntity queryOne(String sql, Object... args) {
    List<BaseEntity> lst = queryList(sql, 0, 1, args);
    if (lst.size() > 0) {
      return lst.get(0);
    }
    return null;
  }

  public <T> T queryOne(String sql, Class<T> clazz, Object... args) {
    List<T> lst = queryList(sql, 0, 1, clazz, args);
    if (lst.size() > 0) {
      return lst.get(0);
    }
    return null;
  }

  public void delete(BaseEntity dataRow) {
    String TableName = dataRow.getTableName();
    String keyFields = dataRow.getKeyField();
    if (!StringUtil.isNotBlank(TableName) || !StringUtil.isNotBlank(keyFields)) {
      throw new RuntimeException("未设置表名或主健，无法执行删除操作");
    }
    delete(dataRow, TableName, keyFields);
  }

  public void delete(BaseEntity dataRow, String TableName, String keyFields) {
    String sql = "delete from " + TableName + " where " + keyFields + "=?";
    executeSql(sql, dataRow.get(keyFields));
    dataRow.setRowState(BaseEntity.DataRowState.Deleted);
  }

  public void update(BaseEntity dataRow) {
    update(dataRow, false);
  }

  public void update(BaseEntity dataRow, String TableName, String keyFields) {
    update(dataRow, TableName, keyFields, false);
  }

  public void update(BaseEntity dataRow, boolean checkDirty) {
    String TableName = dataRow.getTableName();
    String keyFields = dataRow.getKeyField();
    if (!StringUtil.isNotBlank(TableName) || !StringUtil.isNotBlank(keyFields)) {
      throw new RuntimeException("未设置表名或主健，无法执行更新操作");
    }
    update(dataRow, TableName, keyFields, checkDirty);
  }

  public void update(BaseEntity dataRow, String TableName, String keyFields, boolean checkDirty) {
    if (dataRow.getRowState() == BaseEntity.DataRowState.Unchanged) {
      return;
    }

    String[] keys = dataRow.getChangedKeys();

    if (checkDirty) { // 检查脏数据
      String fields = "";
      for (String fd : keys) fields += fd + ",";
      fields = fields.substring(0, fields.length() - 1);
      String sql = "select " + fields + " from " + TableName + " where " + keyFields + "=?";
      Object keyvalue = dataRow.get(keyFields);
      BaseEntity oldrow = this.queryOne(sql, keyvalue);
      // 比较数据
      String equal = "";
      for (String fd : keys) {
        Object val1 = dataRow.getOldValue(fd);
        Object val2 = oldrow.get(fd);
        if (val1 == null && val2 == null) {
          continue;
        }
        if (val1 != null && !val1.equals(val2)) {
          equal += "原值：" + val1 + "，现库中值为：" + val2;
        }
      }
      if (!StringUtil.isBlank(equal)) {
        throw new RuntimeException("有脏数据:" + equal);
      }
    }

    String sql = "update " + TableName + " set ";
    Object[] args = new Object[keys.length + 1];
    for (int i = 0; i < keys.length; i++) {
      sql += keys[i] + "=?,";
      args[i] = dataRow.get(keys[i]);
    }
    sql = sql.substring(0, sql.length() - 1);
    sql += " where " + keyFields + "=?";
    args[keys.length] = dataRow.get(keyFields);
    executeSql(sql, args);
    dataRow.AcceptChanges();
  }

  public void insert(BaseEntity dataRow) {
    String TableName = dataRow.getTableName();
    if (!StringUtil.isNotBlank(TableName)) {
      throw new RuntimeException("未设置表名或主健，无法执行插入操作");
    }
    insert(dataRow, TableName);
  }

  public void insert(BaseEntity dataRow, String TableName) {
    if (dataRow.getRowState() != BaseEntity.DataRowState.Added
        && dataRow.getRowState() != BaseEntity.DataRowState.Modified) {
      return;
    }

    String[] keys = dataRow.keySet().toArray(new String[0]);

    String sql = "insert into " + TableName + "(";
    Object[] args = new Object[keys.length];
    String pamlst = "";
    for (int i = 0; i < keys.length; i++) {
      sql += keys[i] + ",";
      pamlst += "?,";
      args[i] = dataRow.get(keys[i]);
    }
    sql = sql.substring(0, sql.length() - 1);
    pamlst = pamlst.substring(0, pamlst.length() - 1);
    sql += ") values(" + pamlst + ")";
    executeSql(sql, args);
    dataRow.AcceptChanges();
  }

  public String executeString(String sql, Object... args) {
    Object obj = executeObject(sql, args);
    if (obj == null) {
      return "";
    }
    return obj.toString();
  }

  public int executeInt(String sql, Object... args) {
    try {
      return Integer.parseInt(executeString(sql, args));
    } catch (Exception e) {
      return 0;
    }
  }

  public Object executeObject(String sql, Object... args) {
    BaseEntity dr = queryOne(sql, args);
    if (dr != null) {
      return dr.get(dr.keySet().iterator().next());
    }
    return null;
  }

  public boolean executeSql(String sql, Object... args) {
    PreparedStatement stat = null;
    DataSourcePool pool = DataSourcePool.getInstance();
    try {
      stat = pool.getConnection().prepareStatement(sql);
      // 设置参数
      for (int i = 0; i < args.length; i++) {
        stat.setObject(i + 1, args[i]);
      }
      return stat.execute();
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      pool.release();
      DatasourceUtil.close(stat);
    }
  }
}
