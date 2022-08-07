package com.shawn.study.deep.in.java.design.behavioral.command;

public interface Command {

  void execute(Target target);

  void undo();

  void redo();

  @Override
  String toString();
}
