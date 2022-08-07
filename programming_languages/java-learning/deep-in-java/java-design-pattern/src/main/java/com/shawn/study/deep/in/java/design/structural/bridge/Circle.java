package com.shawn.study.deep.in.java.design.structural.bridge;

/**
 * @author shawn
 * @description:
 * @since 2020/7/20
 */
public class Circle extends Shape {
  private int x, y, radius;

  public Circle(int x, int y, int radius, DrawAPI drawAPI) {
    super(drawAPI);
    this.x = x;
    this.y = y;
    this.radius = radius;
  }

  public void draw() {
    drawAPI.drawCircle(radius, x, y);
  }
}
