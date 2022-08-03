package com.shawn.study.deep.in.java.baens.core;

import java.util.List;

public interface ComponentContext {

  <C> C getComponent(String name);

  <C> C getComponent(String name, Class<C> type);

  <C> C getComponent(Class<C> type);

  List<String> getComponentNames();
}
