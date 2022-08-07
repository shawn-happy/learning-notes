package com.shawn.study.deep.in.java.design.behavioral.state.pattern;

import com.shawn.study.deep.in.java.design.behavioral.state.StateEnum;

public interface IMario {

  StateEnum getName();

  void obtainMushRoom();

  void obtainCape();

  void obtainFireFlower();

  void meetMonster();
}
