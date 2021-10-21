package com.shawn.design.structural.adapter;

/**
 * @author shawn
 * @description:
 * @since 2020/7/29
 */
public class Captain {

  private RowingBoat rowingBoat;

  public Captain() {}

  public Captain(RowingBoat rowingBoat) {
    this.rowingBoat = rowingBoat;
  }

  public void setRowingBoat(RowingBoat rowingBoat) {
    this.rowingBoat = rowingBoat;
  }

  public void row() {
    rowingBoat.row();
  }
}
