package com.shawn.study.deep.in.java.design.behavioral.mediator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shawn
 * @since 2020/8/13
 */
public class PartyImpl implements Party {

  private final List<PartyMember> members;

  public PartyImpl() {
    members = new ArrayList<>();
  }

  @Override
  public void act(PartyMember actor, Action action) {
    for (PartyMember member : members) {
      if (!member.equals(actor)) {
        member.partyAction(action);
      }
    }
  }

  @Override
  public void addMember(PartyMember member) {
    members.add(member);
    member.joinedParty(this);
  }
}
