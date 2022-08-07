package com.shawn.study.deep.in.java.design.jdbc.v4;

import java.util.Date;

/**
 * @author shawn
 * @since 2020/8/23
 */
public class Coffee extends BaseEntity {

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
    return super.getLong("id");
  }

  public void setId(long id) {
    super.set("id", id);
    this.id = id;
  }

  public Date getCreateTime() {
    return super.getDate("create_time");
  }

  public void setCreateTime(Date createTime) {
    super.set("create_time", createTime);
    this.createTime = createTime;
  }

  public Date getUpdateTime() {
    return super.getDate("update_time");
  }

  public void setUpdateTime(Date updateTime) {
    super.set("update_time", updateTime);
    this.updateTime = updateTime;
  }

  public String getName() {
    return super.getStr("name");
  }

  public void setName(String name) {
    super.set("name", name);
    this.name = name;
  }

  public long getPrice() {
    return super.getLong("price");
  }

  public void setPrice(long price) {
    super.set("price", price);
    this.price = price;
  }

  @Override
  public String toString() {
    return "Coffee{"
        + "id="
        + getId()
        + ", createTime="
        + getCreateTime()
        + ", updateTime="
        + getUpdateTime()
        + ", name='"
        + getName()
        + '\''
        + ", price="
        + getPrice()
        + '}';
  }
}
