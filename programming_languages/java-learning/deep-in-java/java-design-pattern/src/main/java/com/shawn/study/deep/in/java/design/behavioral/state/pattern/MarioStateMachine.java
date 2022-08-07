package com.shawn.study.deep.in.java.design.behavioral.state.pattern;

import com.shawn.study.deep.in.java.design.behavioral.state.StateEnum;

/**
 * @author shawn
 * @since 2020/8/8
 */
public class MarioStateMachine {

  private int score;
  private IMario currentState;

  public MarioStateMachine() {
    this.score = 0;
    this.currentState = new SmallMario(this);
  }

  public void obtainMushRoom() {
    this.currentState.obtainMushRoom();
  }

  public void obtainCape() {
    this.currentState.obtainCape();
  }

  public void obtainFireFlower() {
    this.currentState.obtainFireFlower();
  }

  public void meetMonster() {
    this.currentState.meetMonster();
  }

  public int getScore() {
    return score;
  }

  public StateEnum getCurrentState() {
    return currentState.getName();
  }

  public void setScore(int score) {
    this.score = score;
  }

  public void setCurrentState(IMario currentState) {
    this.currentState = currentState;
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
