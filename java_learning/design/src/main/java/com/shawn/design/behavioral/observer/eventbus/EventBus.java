package com.shawn.design.behavioral.observer.eventbus;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author shawn
 * @since 2020/7/31
 */
public class EventBus {

  private Executor executor;

  private ObserverRegistry registry = new ObserverRegistry();

  public EventBus() {
    this(Executors.newSingleThreadExecutor());
  }

  public EventBus(Executor executor) {
    this.executor = executor;
  }

  public void register(Object observer) {
    registry.register(observer);
  }

  public void post(Object event) {
    List<ObserverAction> matchedObserverActions = registry.getMatchedObserverActions(event);
    for (ObserverAction observerAction : matchedObserverActions) {
      executor.execute(() -> observerAction.execute(event));
    }
  }
}
