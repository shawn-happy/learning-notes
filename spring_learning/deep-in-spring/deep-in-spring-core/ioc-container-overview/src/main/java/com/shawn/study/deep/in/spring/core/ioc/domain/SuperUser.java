package com.shawn.study.deep.in.spring.core.ioc.domain;

import com.shawn.study.deep.in.spring.core.ioc.annotation.Super;

@Super
public class SuperUser extends User {
  private String idCard;

  public String getIdCard() {
    return idCard;
  }

  public void setIdCard(String idCard) {
    this.idCard = idCard;
  }

  @Override
  public String toString() {
    return "SuperUser{"
        + "id='"
        + getId()
        + '\''
        + ", name='"
        + getName()
        + '\''
        + ", age="
        + getAge()
        + ", address='"
        + getAddress()
        + '\''
        + ", idCard='"
        + getIdCard()
        + '}';
  }
}
