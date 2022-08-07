package com.shawn.study.deep.in.java.design.create.prototype;

/**
 * @author shawn
 * @description:
 * @since 2020/7/19
 */
public class BlueColor extends Color {

  public BlueColor() {
    this.colorName = "blue";
  }

  @Override
  void addColor() {
    System.out.println("Blue color added");
  }
}
