package com.shawn.study.deep.in.java.design.jdbc.entity;

import java.util.Date;

/**
 * @author shawn
 * @since 2020/8/14
 */
public class Coffee {

  private long id;

  private Date createTime;

  private Date updateTime;

  private String name;

  private long price;

  public Coffee() {}

  public Coffee(long id, Date createTime, Date updateTime, String name, long price) {
    this.id = id;
    this.createTime = createTime;
    this.updateTime = updateTime;
    this.name = name;
    this.price = price;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getPrice() {
    return price;
  }

  public void setPrice(long price) {
    this.price = price;
  }

  @Override
  public String toString() {
    return "Coffee{"
        + "id="
        + id
        + ", createTime="
        + createTime
        + ", updateTime="
        + updateTime
        + ", name='"
        + name
        + '\''
        + ", price="
        + price
        + '}';
  }
}
