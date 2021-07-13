package com.shawn.study.java.web.context;

/**
 * application context lifecycle
 *
 * @author Shawn
 * @since 1.0.0
 */
public interface ApplicationContext {

  void init();

  <C> C createComponent(String name);

  <C> C getComponent(String name);

  void destroy();
}
