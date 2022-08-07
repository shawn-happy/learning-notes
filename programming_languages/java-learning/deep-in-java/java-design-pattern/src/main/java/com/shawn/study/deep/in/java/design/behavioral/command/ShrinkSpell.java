package com.shawn.study.deep.in.java.design.behavioral.command;

/**
 * @author shawn
 * @since 2020/8/12
 */
public class ShrinkSpell implements Command {

  private Size oldSize;
  private Target target;

  @Override
  public void execute(Target target) {
    oldSize = target.getSize();
    target.setSize(Size.SMALL);
    this.target = target;
  }

  @Override
  public void undo() {
    if (oldSize != null && target != null) {
      Size temp = target.getSize();
      target.setSize(oldSize);
      oldSize = temp;
    }
  }

  @Override
  public void redo() {
    undo();
  }

  @Override
  public String toString() {
    return "Shrink spell";
  }
}
