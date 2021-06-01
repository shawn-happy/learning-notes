package com.shawn.design.behavioral.iterator;

import java.util.Date;

/**
 * @author shawn
 * @since 2020/8/10
 */
public class Item {

  private long id;
  private String name;
  private String idCard;
  private Date birthday;
  private int age;

  public Item(long id, String name, String idCard, Date birthday, int age) {
    this.id = id;
    this.name = name;
    this.idCard = idCard;
    this.birthday = birthday;
    this.age = age;
  }

  @Override
  public String toString() {
    return "Item{"
        + "id="
        + id
        + ", name='"
        + name
        + '\''
        + ", idCard='"
        + idCard
        + '\''
        + ", birthday="
        + birthday
        + ", age="
        + age
        + '}';
  }
}
