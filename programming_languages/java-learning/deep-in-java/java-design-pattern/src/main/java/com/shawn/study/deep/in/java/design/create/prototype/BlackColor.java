package com.shawn.study.deep.in.java.design.create.prototype;

/**
 * @author shawn
 * @description:
 * @since 2020/7/19
 */
public class BlackColor extends Color {

  public BlackColor() {
    this.colorName = "black";
  }

  @Override
  void addColor() {
    System.out.println("Black color added");
  }
}
