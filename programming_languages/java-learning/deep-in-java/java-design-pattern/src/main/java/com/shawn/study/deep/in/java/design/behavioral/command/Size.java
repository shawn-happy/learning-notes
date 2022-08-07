package com.shawn.study.deep.in.java.design.behavioral.command;

/**
 * @author shawn
 * @since 2020/8/12
 */
public enum Size {
  SMALL("small"),
  NORMAL("normal");

  private String title;

  Size(String title) {
    this.title = title;
  }

  @Override
  public String toString() {
    return title;
  }
}
