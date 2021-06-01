package com.shawn.design.behavioral.command;

/**
 * @author shawn
 * @since 2020/8/12
 */
public enum Visibility {
  VISIBLE("visible"),
  INVISIBLE("invisible");

  private String title;

  Visibility(String title) {
    this.title = title;
  }

  @Override
  public String toString() {
    return title;
  }
}
