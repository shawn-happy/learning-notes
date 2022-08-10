package com.shawn.study.deep.in.java.design.behavioral.mediator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shawn
 * @since 2020/8/13
 */
public abstract class PartyMemberBase implements PartyMember {

  private static final Logger LOGGER = LoggerFactory.getLogger(PartyMemberBase.class);

  protected Party party;

  @Override
  public void joinedParty(Party party) {
    LOGGER.info("{} joins the party", this);
    this.party = party;
  }

  @Override
  public void partyAction(Action action) {
    LOGGER.info("{} {}", this, action.getDescription());
  }

  @Override
  public void act(Action action) {
    if (party != null) {
      LOGGER.info("{} {}", this, action);
      party.act(this, action);
    }
  }

  @Override
  public abstract String toString();
}
