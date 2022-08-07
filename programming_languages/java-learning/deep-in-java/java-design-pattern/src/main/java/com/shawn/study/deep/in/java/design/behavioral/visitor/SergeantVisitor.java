package com.shawn.study.deep.in.java.design.behavioral.visitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author shawn
 * @since 2020/8/12
 */
public class SergeantVisitor implements UnitVisitor {

  private static final Logger LOGGER = LoggerFactory.getLogger(SergeantVisitor.class);

  @Override
  public void visitSoldier(Soldier soldier) {
    // Do nothing
  }

  @Override
  public void visitSergeant(Sergeant sergeant) {
    LOGGER.info("Hello {}", sergeant);
  }

  @Override
  public void visitCommander(Commander commander) {
    // Do nothing
  }
}
