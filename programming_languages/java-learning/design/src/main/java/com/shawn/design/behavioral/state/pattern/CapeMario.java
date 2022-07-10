package com.shawn.design.behavioral.state.pattern;

import com.shawn.design.behavioral.state.StateEnum;

/**
 * @author shawn
 * @since 2020/8/8
 */
public class CapeMario implements IMario {

  private MarioStateMachine marioStateMachine;

  public CapeMario(MarioStateMachine marioStateMachine) {
    this.marioStateMachine = marioStateMachine;
  }

  @Override
  public StateEnum getName() {
    return StateEnum.CAPE;
  }

  @Override
  public void obtainMushRoom() {}

  @Override
  public void obtainCape() {}

  @Override
  public void obtainFireFlower() {
    marioStateMachine.setCurrentState(new FireMario(marioStateMachine));
    marioStateMachine.setScore(marioStateMachine.getScore() + 300);
  }

  @Override
  public void meetMonster() {
    marioStateMachine.setCurrentState(new SmallMario(marioStateMachine));
    marioStateMachine.setScore(marioStateMachine.getScore() - 200);
  }
}
