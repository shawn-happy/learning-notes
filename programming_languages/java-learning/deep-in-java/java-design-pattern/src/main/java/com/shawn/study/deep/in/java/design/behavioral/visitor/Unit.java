package com.shawn.study.deep.in.java.design.behavioral.visitor;

/**
 * @author shawn
 * @since 2020/8/12
 */
public abstract class Unit {

  private Unit[] children;

  public Unit(Unit... children) {
    this.children = children;
  }

  /** Accept visitor */
  public void accept(UnitVisitor visitor) {
    for (Unit child : children) {
      child.accept(visitor);
    }
  }
}
