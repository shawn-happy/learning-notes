package com.shawn.design.behavioral.state.logic;

import com.shawn.design.behavioral.state.StateEnum;

/**
 * @author shawn
 * @since 2020/8/8
 */
public class MarioStateMachine {

  private int score;
  private StateEnum currentState;

  public MarioStateMachine() {
    this.score = 0;
    this.currentState = StateEnum.SMALL;
  }

  public void obtainMushRoom() {
    if(currentState.equals(StateEnum.SMALL)){
      this.currentState = StateEnum.SUPER;
      this.score += 100;
    }
  }

  public void obtainCape(){
    if(currentState.equals(StateEnum.SMALL) || currentState.equals(StateEnum.SUPER)){
      this.currentState = StateEnum.CAPE;
      this.score += 200;
    }
  }

  public void obtainFireFlower(){
    if(currentState.equals(StateEnum.SMALL) || currentState.equals(StateEnum.SUPER)){
      this.currentState = StateEnum.FIRE;
      this.score += 300;
    }
  }

  public void meetMonster(){
    if(currentState.equals(StateEnum.SUPER)){
      this.currentState = StateEnum.SMALL;
      this.score -= 100;
      return;
    }
    if(currentState.equals(StateEnum.CAPE)){
      this.currentState = StateEnum.SMALL;
      this.score -= 200;
      return;
    }
    if(currentState.equals(StateEnum.FIRE)){
      this.currentState = StateEnum.SMALL;
      this.score -= 300;
      return;
    }
  }

  public int getScore() {
    return score;
  }

  public StateEnum getCurrentState() {
    return currentState;
  }

  public static void main(String[] args) {
    MarioStateMachine machine = new MarioStateMachine();
    System.out.printf("init mario score: %d, state: %s\n", machine.getScore(), machine.getCurrentState());
    machine.obtainMushRoom();
    System.out.printf("obtain mush room mario score: %d, state: %s\n", machine.getScore(), machine.getCurrentState());
    machine.meetMonster();
    System.out.printf("meet Monster mario score: %d, state: %s\n", machine.getScore(), machine.getCurrentState());
    machine.obtainFireFlower();
    System.out.printf("obtain fire flower mario score: %d, state: %s\n", machine.getScore(), machine.getCurrentState());
    machine.obtainFireFlower();
    System.out.printf("obtain fire flower mario score: %d, state: %s\n", machine.getScore(), machine.getCurrentState());
    machine.meetMonster();
    System.out.printf("meet Monster mario score: %d, state: %s\n", machine.getScore(), machine.getCurrentState());
    machine.obtainCape();
    System.out.printf("obtain cape mario score: %d, state: %s\n", machine.getScore(), machine.getCurrentState());
  }
}
