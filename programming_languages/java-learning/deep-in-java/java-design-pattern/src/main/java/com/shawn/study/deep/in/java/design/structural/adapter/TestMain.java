package com.shawn.study.deep.in.java.design.structural.adapter;

/**
 * @author shawn
 * @description:
 * @since 2020/7/29
 */
public class TestMain {

  public static void main(String[] args) {
    Captain captain = new Captain(new FishingBoatAdapter());
    captain.row();
  }
}
