package com.shawn.design.structural.adapter;

/**
 * @author shawn
 * @description:
 * @since 2020/7/29
 */
public class FishingBoatAdapter implements RowingBoat {

  private FishingBoat boat;

  public FishingBoatAdapter() {
    boat = new FishingBoat();
  }

  @Override
  public void row() {
    boat.sail();
  }
}
