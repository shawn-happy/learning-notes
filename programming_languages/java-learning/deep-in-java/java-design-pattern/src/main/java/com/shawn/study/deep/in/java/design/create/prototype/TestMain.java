package com.shawn.study.deep.in.java.design.create.prototype;

/**
 * @author shawn
 * @description:
 * @since 2020/7/19
 */
public class TestMain {

  public static void main(String[] args) {
    ColorStore.getColor("blue").addColor();
    ColorStore.getColor("black").addColor();
    ColorStore.getColor("black").addColor();
    ColorStore.getColor("blue").addColor();
  }
}
