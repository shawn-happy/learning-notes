package com.shawn.design.behavioral.mediator;

public interface PartyMember {

  void joinedParty(Party party);

  void partyAction(Action action);

  void act(Action action);
}
