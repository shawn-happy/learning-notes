package com.shawn.design.behavioral.state.pattern;

import com.shawn.design.behavioral.state.StateEnum;

public interface IMario {

  StateEnum getName();

  void obtainMushRoom();

  void obtainCape();

  void obtainFireFlower();

  void meetMonster();
}
