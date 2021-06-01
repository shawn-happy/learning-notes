package com.shawn.design.behavioral.mediator;

/**
 * @author shawn
 * @since 2020/8/13
 */
public class TestMain {

  public static void main(String[] args) {

    // create party and members
    Party party = new PartyImpl();
    Hobbit hobbit = new Hobbit();
    Wizard wizard = new Wizard();
    Rogue rogue = new Rogue();
    Hunter hunter = new Hunter();

    // add party members
    party.addMember(hobbit);
    party.addMember(wizard);
    party.addMember(rogue);
    party.addMember(hunter);

    // perform actions -> the other party members
    // are notified by the party
    hobbit.act(Action.ENEMY);
    wizard.act(Action.TALE);
    rogue.act(Action.GOLD);
    hunter.act(Action.HUNT);
  }

}
