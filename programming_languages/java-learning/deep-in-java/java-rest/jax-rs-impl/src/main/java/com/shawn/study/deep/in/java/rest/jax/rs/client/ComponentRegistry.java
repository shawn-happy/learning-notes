package com.shawn.study.deep.in.java.rest.jax.rs.client;

import javax.ws.rs.ConstrainedTo;
import javax.ws.rs.Priorities;
import javax.ws.rs.RuntimeType;

class ComponentRegistry {

  private final Class<?> componentClass;
  private final Object component;
  private final RuntimeType runtimeType;
  private final int priority;

  ComponentRegistry(final Class<?> componentClass, int priority) {
    this.componentClass = componentClass;
    this.component = null;
    final ConstrainedTo runtimeTypeConstraint = componentClass.getAnnotation(ConstrainedTo.class);
    this.runtimeType = runtimeTypeConstraint == null ? null : runtimeTypeConstraint.value();
    this.priority = priority(componentClass, priority);
  }

  ComponentRegistry(final Object component, int priority) {
    this.componentClass = component.getClass();
    this.component = component;
    final ConstrainedTo runtimeTypeConstraint = componentClass.getAnnotation(ConstrainedTo.class);
    this.runtimeType = runtimeTypeConstraint == null ? null : runtimeTypeConstraint.value();
    this.priority = priority(componentClass, priority);
  }

  private int priority(Class<?> componentClass, int priority) {
    if (priority != -1) {
      return priority;
    }
    return Priorities.USER;
  }

  Class<?> getFeatureClass() {
    return componentClass;
  }

  Object getFeature() {
    return component;
  }

  RuntimeType getFeatureRuntimeType() {
    return runtimeType;
  }

  int getPriority() {
    return priority;
  }
}
