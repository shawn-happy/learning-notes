package com.shawn.design.behavioral.responsibility;

/**
 * @author shawn
 * @since 2020/8/8
 */
public class BHandler extends Handler {

  @Override
  public void handle() {
    if (handler != null) {
      System.out.println("BHandler.handle");
      handler.handle();
    }
  }
}
