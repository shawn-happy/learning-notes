package com.shawn.design.structural.bridge;

/**
 * @author shawn
 * @description:
 * @since 2020/7/20
 */
public class TestMain {

  public static void main(String[] args) {
    Shape redCircle = new Circle(100, 100, 10, new RedCircle());
    Shape greenCircle = new Circle(100, 100, 10, new GreenCircle());

    redCircle.draw();
    greenCircle.draw();
  }
}
