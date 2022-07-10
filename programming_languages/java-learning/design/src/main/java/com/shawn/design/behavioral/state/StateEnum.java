package com.shawn.design.behavioral.state;

/**
 * @author shawn
 * @since 2020/8/8
 */
public enum StateEnum {
  SMALL(0),
  SUPER(1),
  FIRE(2),
  CAPE(3);

  private int value;

  StateEnum(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
