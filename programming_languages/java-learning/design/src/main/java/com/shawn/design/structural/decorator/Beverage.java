package com.shawn.design.structural.decorator;

/**
 * @author shawn
 * @description:
 * @since 2020/7/28
 */
public abstract class Beverage {

  String description = "Unknown Beverage";

  public String getDescription(){
    return description;
  }

  public abstract double cost();

}
