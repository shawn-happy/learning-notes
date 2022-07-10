package com.shawn.design.behavioral.responsibility;

/**
 * @author shawn
 * @since 2020/8/8
 */
public abstract class Handler {
  protected Handler handler = null;

  public void setHandler(Handler handler) {
    this.handler = handler;
  }

  public abstract void handle();
}
