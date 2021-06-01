package com.shawn.design.behavioral.command;

public interface Command {

  public abstract void execute(Target target);

  public abstract void undo();

  public abstract void redo();

  @Override
  public abstract String toString();
}
