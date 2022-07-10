package com.shawn.design.behavioral.command;

/**
 * @author shawn
 * @since 2020/8/12
 */
public class Goblin extends Target {

  public Goblin() {
    setSize(Size.NORMAL);
    setVisibility(Visibility.VISIBLE);
  }

  @Override
  public String toString() {
    return "Goblin";
  }
}
