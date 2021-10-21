package com.shawn.design.structural.bridge;

/**
 * @author shawn
 * @description:
 * @since 2020/7/20
 */
public abstract class Shape {

  protected DrawAPI drawAPI;

  protected Shape(DrawAPI drawAPI) {
    this.drawAPI = drawAPI;
  }

  public abstract void draw();
}
