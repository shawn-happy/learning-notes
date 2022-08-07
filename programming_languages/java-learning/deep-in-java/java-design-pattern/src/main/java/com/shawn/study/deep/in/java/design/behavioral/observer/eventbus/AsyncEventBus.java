package com.shawn.study.deep.in.java.design.behavioral.observer.eventbus;

import java.util.concurrent.Executor;

/**
 * @author shawn
 * @since 2020/7/31
 */
public class AsyncEventBus extends EventBus {

  public AsyncEventBus(Executor executor) {
    super(executor);
  }
}
