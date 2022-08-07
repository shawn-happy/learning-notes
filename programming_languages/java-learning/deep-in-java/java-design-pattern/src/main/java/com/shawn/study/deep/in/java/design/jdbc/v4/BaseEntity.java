package com.shawn.study.deep.in.java.design.jdbc.v4;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shawn
 * @since 2020/8/23
 */
public class BaseEntity extends HashMap<String, Object> implements Serializable {

  private static final long serialVersionUID = -2689062767790807572L;

  private String tableName;
  private String keyField;

  public BaseEntity() {}

  public BaseEntity(String tableName, String keyField) {
    this.tableName = tableName;
    this.keyField = keyField;
  }

  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public String getKeyField() {
    return keyField;
  }

  public void setKeyField(String keyField) {
    this.keyField = keyField;
  }

  private Map<String, Object> recOldval = null;

  // 记录修改历史值
  private void ChangeValue(String column, Object Oldval) {
    if (recOldval == null) {
      recOldval = new HashMap<String, Object>();
    }
    if (recOldval.containsKey(column)) {
      // 二次修改，不记录中间值
      return;
    }
    rowState = DataRowState.Modified;
    recOldval.put(column, Oldval);
  }

  public String[] getChangedKeys() {
    return recOldval.keySet().toArray(new String[0]);
  }

  public Object getOldValue(String key) {
    return recOldval.get(key);
  }

  public enum DataRowState {
    Detached(1),
    Unchanged(2),
    Added(4),
    Deleted(8),
    Modified(16);
    int value;

    DataRowState(int s) {
      value = s;
    }

    public int getValue() {
      return value;
    }
  }

  private DataRowState rowState = DataRowState.Added;

  /** 取得行状态 */
  public DataRowState getRowState() {
    return rowState;
  }

  public void setRowState(DataRowState rowState) {
    this.rowState = rowState;
  }

  public void AcceptChanges() {
    rowState = DataRowState.Unchanged;
    recOldval = null;
  }

  /** */
  public BaseEntity set(String column, Object value) {
    column = keyDeal(column);
    Object orgVal = get(column);
    if (orgVal == null && value == null) {
      return this;
    }
    if (orgVal != null && orgVal.equals(value)) {
      return this;
    }

    ChangeValue(column, orgVal);
    super.put(column, value);
    return this;
  }

  @SuppressWarnings("unchecked")
  public <T> T get(String column) {
    T result = (T) super.get(keyDeal(column));
    if (result == null) {
      result = (T) super.get(column);
    }
    return result;
  }

  public Object get(Object key) {
    return super.get(keyDeal(key.toString()));
  }

  private String keyDeal(String key) {
    return key.toLowerCase(); // .replace("_", "");
  }

  @Override
  public Object put(String key, Object value) {
    key = keyDeal(key);
    return super.put(key, value);
  }

  /** Get column of mysql type: varchar, char, enum, set, text, tinytext, mediumtext, longtext */
  public String getStr(String column) {
    return ConvertUtil.convertDataType(String.class, get(column));
  }

  /** Get column of mysql type: int, integer, tinyint(n) n > 1, smallint, mediumint */
  public Integer getInt(String column) {
    return ConvertUtil.convertDataType(Integer.class, get(column));
  }

  /** Get column of mysql type: bigint */
  public Long getLong(String column) {
    return ConvertUtil.convertDataType(Long.class, get(column));
  }

  /** Get column of mysql type: unsigned bigint */
  public java.math.BigInteger getBigInteger(String column) {
    return (java.math.BigInteger) get(column);
  }

  /** Get column of mysql type: date, year */
  public java.util.Date getDate(String column) {
    return ConvertUtil.convertDataType(Date.class, get(column));
  }

  /** Get column of mysql type: time */
  public java.sql.Time getTime(String column) {
    return (java.sql.Time) get(column);
  }

  /** Get column of mysql type: timestamp, datetime */
  public java.sql.Timestamp getTimestamp(String column) {
    return (java.sql.Timestamp) get(column);
  }

  /** Get column of mysql type: real, double */
  public Double getDouble(String column) {
    return ConvertUtil.convertDataType(Double.class, get(column));
  }

  /** Get column of mysql type: float */
  public Float getFloat(String column) {
    return ConvertUtil.convertDataType(Float.class, get(column));
  }

  /** Get column of mysql type: bit, tinyint(1) */
  public Boolean getBoolean(String column) {
    return ConvertUtil.convertDataType(Boolean.class, get(column));
  }

  /** Get column of mysql type: decimal, numeric */
  public java.math.BigDecimal getBigDecimal(String column) {
    return ConvertUtil.convertDataType(BigDecimal.class, get(column));
  }

  /**
   * Get column of mysql type: binary, varbinary, tinyblob, blob, mediumblob, longblob I have not
   * finished the test.
   */
  public byte[] getBytes(String column) {
    return (byte[]) get(column);
  }

  /** Get column of any type that extends from Number */
  public Number getNumber(String column) {
    return (Number) get(column);
  }
}
