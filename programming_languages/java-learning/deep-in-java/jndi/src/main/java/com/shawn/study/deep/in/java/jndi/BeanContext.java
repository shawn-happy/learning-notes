package com.shawn.study.deep.in.java.jndi;

import java.util.List;

public interface BeanContext {

  <C> C getBean(String name);

  <C> C getBean(Class<?> requiredType);

  List<String> getBeanNames();
}
