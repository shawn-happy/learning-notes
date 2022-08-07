package com.shawn.study.deep.in.java.design.behavioral.visitor;

/**
 * @author shawn
 * @since 2020/8/12
 */
public class Soldier extends Unit {

  public Soldier(Unit... children) {
    super(children);
  }

  @Override
  public void accept(UnitVisitor visitor) {
    visitor.visitSoldier(this);
    super.accept(visitor);
  }

  @Override
  public String toString() {
    return "soldier";
  }
}
