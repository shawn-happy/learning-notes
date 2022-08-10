package com.shawn.study.deep.in.java.design.behavioral.state.pattern;

import com.shawn.study.deep.in.java.design.behavioral.state.StateEnum;

/**
 * @author shawn
 * @since 2020/8/8
 */
public class SuperMario implements IMario {

  private MarioStateMachine marioStateMachine;

  public SuperMario(MarioStateMachine marioStateMachine) {
    this.marioStateMachine = marioStateMachine;
  }

  @Override
  public StateEnum getName() {
    return StateEnum.SUPER;
  }

  @Override
  public void obtainMushRoom() {}

  @Override
  public void obtainCape() {
    marioStateMachine.setCurrentState(new CapeMario(marioStateMachine));
    marioStateMachine.setScore(marioStateMachine.getScore() + 200);
  }

  @Override
  public void obtainFireFlower() {
    marioStateMachine.setCurrentState(new FireMario(marioStateMachine));
    marioStateMachine.setScore(marioStateMachine.getScore() + 300);
  }

  @Override
  public void meetMonster() {
    marioStateMachine.setCurrentState(new SmallMario(marioStateMachine));
    marioStateMachine.setScore(marioStateMachine.getScore() - 100);
  }
}
