package com.shawn.design.create.prototype;

/**
 * @author shawn
 * @description:
 * @since 2020/7/19
 */
public abstract class Color implements Cloneable {

  protected String colorName;

  abstract void addColor();

  public Object clone() {
    Object clone = null;
    try {
      clone = super.clone();
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    return clone;
  }
}
