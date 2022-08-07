package com.shawn.study.deep.in.java.design.behavioral.state.table;

public enum Event {
  GET_MUSHROOM(0),
  GET_CAPE(1),
  GET_FIRE(2),
  MEET_MONSTER(3);

  private int value;

  public int getValue() {
    return value;
  }

  Event(int value) {
    this.value = value;
  }
}
