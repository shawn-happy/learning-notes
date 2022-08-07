package com.shawn.study.deep.in.java.design.behavioral.command;

import java.util.Deque;
import java.util.LinkedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shawn
 * @since 2020/8/12
 */
public class Wizard {

  private static final Logger LOGGER = LoggerFactory.getLogger(Wizard.class);

  private Deque<Command> undoStack = new LinkedList<>();
  private Deque<Command> redoStack = new LinkedList<>();

  public Wizard() {
    // comment to ignore sonar issue: LEVEL critical
  }

  /** Cast spell */
  public void castSpell(Command command, Target target) {
    LOGGER.info("{} casts {} at {}", this, command, target);
    command.execute(target);
    undoStack.offerLast(command);
  }

  /** Undo last spell */
  public void undoLastSpell() {
    if (!undoStack.isEmpty()) {
      Command previousSpell = undoStack.pollLast();
      redoStack.offerLast(previousSpell);
      LOGGER.info("{} undoes {}", this, previousSpell);
      previousSpell.undo();
    }
  }

  /** Redo last spell */
  public void redoLastSpell() {
    if (!redoStack.isEmpty()) {
      Command previousSpell = redoStack.pollLast();
      undoStack.offerLast(previousSpell);
      LOGGER.info("{} redoes {}", this, previousSpell);
      previousSpell.redo();
    }
  }

  @Override
  public String toString() {
    return "Wizard";
  }
}
