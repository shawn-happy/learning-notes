package com.shawn.study.deep.in.java.design.behavioral.state.pattern;

import com.shawn.study.deep.in.java.design.behavioral.state.StateEnum;

/**
 * @author shawn
 * @since 2020/8/8
 */
public class FireMario implements IMario {

  private MarioStateMachine marioStateMachine;

  public FireMario(MarioStateMachine marioStateMachine) {
    this.marioStateMachine = marioStateMachine;
  }

  @Override
  public StateEnum getName() {
    return StateEnum.FIRE;
  }

  @Override
  public void obtainMushRoom() {}

  @Override
  public void obtainCape() {}

  @Override
  public void obtainFireFlower() {}

  @Override
  public void meetMonster() {
    marioStateMachine.setCurrentState(new SmallMario(marioStateMachine));
    marioStateMachine.setScore(marioStateMachine.getScore() - 300);
  }
}
