package com.shawn.study.deep.in.java.design.behavioral.responsibility;

/**
 * @author shawn
 * @since 2020/8/8
 */
public class HandlerChain {

  private Handler head;

  private Handler tail;

  public void addHandler(Handler handler) {
    handler.setHandler(null);
    if (head == null) {
      head = handler;
      tail = handler;
      return;
    }
    tail.setHandler(handler);
    tail = handler;
  }

  public void handle() {
    if (head != null) {
      head.handle();
    }
  }

  public static void main(String[] args) {
    HandlerChain chain = new HandlerChain();
    chain.addHandler(new AHandler());
    chain.addHandler(new BHandler());
    chain.handle();
  }
}
