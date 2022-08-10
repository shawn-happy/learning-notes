package com.shawn.study.deep.in.java.design.behavioral.state.table;

import static com.shawn.study.deep.in.java.design.behavioral.state.StateEnum.*;

import com.shawn.study.deep.in.java.design.behavioral.state.StateEnum;

/**
 * @author shawn
 * @since 2020/8/8
 */
public class MarioStateMachine {

  private int score;
  private StateEnum currentState;

  private static final StateEnum[][] transitionTable = {
    {SUPER, CAPE, FIRE, SMALL},
    {SUPER, CAPE, FIRE, SMALL},
    {CAPE, CAPE, CAPE, SMALL},
    {FIRE, FIRE, FIRE, SMALL}
  };

  private static final int[][] actionTable = {
    {+100, +200, +300, +0},
    {+0, +200, +300, -100},
    {+0, +0, +0, -200},
    {+0, +0, +0, -300}
  };

  public MarioStateMachine() {
    this.score = 0;
    this.currentState = StateEnum.SMALL;
  }

  public void obtainMushRoom() {
    executeEvent(Event.GET_MUSHROOM);
  }

  public void obtainCape() {
    executeEvent(Event.GET_CAPE);
  }

  public void obtainFireFlower() {
    executeEvent(Event.GET_FIRE);
  }

  public void meetMonster() {
    executeEvent(Event.MEET_MONSTER);
  }

  private void executeEvent(Event event) {
    int stateValue = currentState.getValue();
    int eventValue = event.getValue();
    this.currentState = transitionTable[stateValue][eventValue];
    this.score = actionTable[stateValue][eventValue];
  }

  public int getScore() {
    return score;
  }

  public StateEnum getCurrentState() {
    return currentState;
  }

  public static void main(String[] args) {
    MarioStateMachine machine = new MarioStateMachine();
    System.out.printf(
        "init mario score: %d, state: %s\n", machine.getScore(), machine.getCurrentState());
    machine.obtainMushRoom();
    System.out.printf(
        "obtain mush room mario score: %d, state: %s\n",
        machine.getScore(), machine.getCurrentState());
    machine.meetMonster();
    System.out.printf(
        "meet Monster mario score: %d, state: %s\n", machine.getScore(), machine.getCurrentState());
    machine.obtainFireFlower();
    System.out.printf(
        "obtain fire flower mario score: %d, state: %s\n",
        machine.getScore(), machine.getCurrentState());
    machine.obtainFireFlower();
    System.out.printf(
        "obtain fire flower mario score: %d, state: %s\n",
        machine.getScore(), machine.getCurrentState());
    machine.meetMonster();
    System.out.printf(
        "meet Monster mario score: %d, state: %s\n", machine.getScore(), machine.getCurrentState());
    machine.obtainCape();
    System.out.printf(
        "obtain cape mario score: %d, state: %s\n", machine.getScore(), machine.getCurrentState());
  }
}
