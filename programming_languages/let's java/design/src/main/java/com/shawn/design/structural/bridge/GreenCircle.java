package com.shawn.design.structural.bridge;

/**
 * @author shawn
 * @description:
 * @since 2020/7/20
 */
public class GreenCircle implements DrawAPI {
  @Override
  public void drawCircle(int radius, int x, int y) {
    System.out.println(
        "Drawing Circle[ color: green, radius: " + radius + ", x: " + x + ", " + y + "]");
  }
}
