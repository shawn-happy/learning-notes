package com.shawn.design.structural.decorator;

/**
 * @author shawn
 * @description:
 * @since 2020/7/28
 */
public class StarbuzzCoffee {

  public static void main(String[] args) {
    Beverage espresso = new Espresso();
    System.out.println(espresso.getDescription() + ": $" + espresso.cost());

    Beverage houseBlend = new HouseBlend();
    houseBlend = new Mocha(houseBlend);
    System.out.println(houseBlend.getDescription() + ": $" + houseBlend.cost());
  }
}
