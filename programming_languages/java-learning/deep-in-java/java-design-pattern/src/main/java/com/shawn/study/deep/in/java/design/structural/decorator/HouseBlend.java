package com.shawn.study.deep.in.java.design.structural.decorator;

/**
 * @author shawn
 * @description:
 * @since 2020/7/28
 */
public class HouseBlend extends Beverage {

  public HouseBlend() {
    description = "house blend coffee";
  }

  @Override
  public double cost() {
    return 0.89;
  }
}
