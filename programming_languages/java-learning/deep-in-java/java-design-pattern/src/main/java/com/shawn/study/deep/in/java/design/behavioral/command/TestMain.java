package com.shawn.study.deep.in.java.design.behavioral.command;

/**
 * @author shawn
 * @since 2020/8/12
 */
public class TestMain {

  public static void main(String[] args) {
    Wizard wizard = new Wizard();
    Goblin goblin = new Goblin();

    goblin.printStatus();

    wizard.castSpell(new ShrinkSpell(), goblin);
    goblin.printStatus();

    wizard.castSpell(new InvisibilitySpell(), goblin);
    goblin.printStatus();

    wizard.undoLastSpell();
    goblin.printStatus();

    wizard.undoLastSpell();
    goblin.printStatus();

    wizard.redoLastSpell();
    goblin.printStatus();

    wizard.redoLastSpell();
    goblin.printStatus();
  }
}
