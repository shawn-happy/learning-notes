package com.shawn.study.deep.in.java.design.structural.decorator;

/**
 * @author shawn
 * @description:
 * @since 2020/7/28
 */
public class Espresso extends Beverage{

  public Espresso(){
    description = "Espresso";
  }

  @Override
  public double cost() {
    return 1.99;
  }
}
