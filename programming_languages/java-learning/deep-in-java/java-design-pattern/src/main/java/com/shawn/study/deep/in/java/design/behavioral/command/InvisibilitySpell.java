package com.shawn.study.deep.in.java.design.behavioral.command;

/**
 * @author shawn
 * @since 2020/8/12
 */
public class InvisibilitySpell implements Command {

  private Target target;

  @Override
  public void execute(Target target) {
    target.setVisibility(Visibility.INVISIBLE);
    this.target = target;
  }

  @Override
  public void undo() {
    if (target != null) {
      target.setVisibility(Visibility.VISIBLE);
    }
  }

  @Override
  public void redo() {
    if (target != null) {
      target.setVisibility(Visibility.INVISIBLE);
    }
  }

  @Override
  public String toString() {
    return "Invisibility spell";
  }
}
