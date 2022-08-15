package com.shawn.study.deep.in.java.rest.jax.rs.client;

import jakarta.annotation.Priority;
import jakarta.ws.rs.ConstrainedTo;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.RuntimeType;

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
    final Priority priorityAnnotation = componentClass.getAnnotation(Priority.class);
    if (priorityAnnotation != null) {
      return priorityAnnotation.value();
    } else {
      return Priorities.USER;
    }
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
