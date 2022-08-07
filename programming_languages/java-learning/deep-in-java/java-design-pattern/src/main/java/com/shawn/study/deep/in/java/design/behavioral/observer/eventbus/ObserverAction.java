package com.shawn.study.deep.in.java.design.behavioral.observer.eventbus;

import java.lang.reflect.Method;

/**
 * @author shawn
 * @since 2020/7/31
 */
public class ObserverAction {

  private Object target;

  private Method method;

  public ObserverAction(Object target, Method method) {
    this.target = target;
    this.method = method;
    this.method.setAccessible(true);
  }

  public void execute(Object event) {
    try {
      method.invoke(target, event);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
