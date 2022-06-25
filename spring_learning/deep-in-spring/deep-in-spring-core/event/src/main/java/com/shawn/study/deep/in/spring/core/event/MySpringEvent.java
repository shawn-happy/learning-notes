package com.shawn.study.deep.in.spring.core.event;

import org.springframework.context.ApplicationEvent;

public class MySpringEvent extends ApplicationEvent {

  /**
   * Create a new {@code ApplicationEvent}.
   *
   * @param message the object on which the event initially occurred or with which the event is
   *     associated (never {@code null})
   */
  public MySpringEvent(String message) {
    super(message);
  }

  @Override
  public String getSource() {
    return (String) super.getSource();
  }

  public String getMessage() {
    return getSource();
  }
}
