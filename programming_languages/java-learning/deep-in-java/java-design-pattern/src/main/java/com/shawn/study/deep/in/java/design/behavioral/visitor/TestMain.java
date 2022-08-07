package com.shawn.study.deep.in.java.design.behavioral.visitor;

/**
 * @author shawn
 * @since 2020/8/12
 */
public class TestMain {

  public static void main(String[] args) {
    Commander commander =
        new Commander(
            new Sergeant(new Soldier(), new Soldier(), new Soldier()),
            new Sergeant(new Soldier(), new Soldier(), new Soldier()));
    commander.accept(new SoldierVisitor());
    commander.accept(new SergeantVisitor());
    commander.accept(new CommanderVisitor());
  }
}
