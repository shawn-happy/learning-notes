package com.shawn.study.deep.in.java.design.structural.decorator;

/**
 * @author shawn
 * @description:
 * @since 2020/7/28
 */
public class Mocha extends CondimentDecorator {

  private Beverage beverage;

  public Mocha(Beverage beverage) {
    this.beverage = beverage;
  }

  @Override
  public String getDescription() {
    return beverage.getDescription() + ", Mocha";
  }

  @Override
  public double cost() {
    return 0.2 + beverage.cost();
  }
}
