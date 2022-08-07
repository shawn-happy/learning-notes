package com.shawn.study.deep.in.java.design.behavioral.mediator;

public interface Party {

  void addMember(PartyMember member);

  void act(PartyMember actor, Action action);
}
