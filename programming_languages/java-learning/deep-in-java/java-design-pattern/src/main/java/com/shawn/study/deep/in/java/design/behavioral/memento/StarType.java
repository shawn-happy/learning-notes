package com.shawn.study.deep.in.java.design.behavioral.memento;

/**
 * @author shawn
 * @since 2020/8/13
 */
public enum StarType {
  SUN("sun"),
  RED_GIANT("red giant"),
  WHITE_DWARF("white dwarf"),
  SUPERNOVA("supernova"),
  DEAD("dead star"),
  UNDEFINED("");

  private String title;

  StarType(String title) {
    this.title = title;
  }

  @Override
  public String toString() {
    return title;
  }
}
