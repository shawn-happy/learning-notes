package com.shawn.study.deep.in.java.design.behavioral.observer.eventbus;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author shawn
 * @since 2020/7/31
 */
public class ObserverRegistry {

  private ConcurrentMap<Class<?>, CopyOnWriteArrayList<ObserverAction>> registry =
      new ConcurrentHashMap<>();

  public void register(Object observer) {
    Map<Class<?>, Collection<ObserverAction>> observerActions = findAllObserverActions(observer);
    Set<Entry<Class<?>, Collection<ObserverAction>>> entries = observerActions.entrySet();
    for (Map.Entry<Class<?>, Collection<ObserverAction>> entry : entries) {
      Class<?> eventType = entry.getKey();
      Collection<ObserverAction> eventActions = entry.getValue();
      CopyOnWriteArrayList<ObserverAction> registeredEventActions = registry.get(eventType);
      if (registeredEventActions == null) {
        registry.putIfAbsent(eventType, new CopyOnWriteArrayList<>());
        registeredEventActions = registry.get(eventType);
      }
      registeredEventActions.addAll(eventActions);
    }
  }

  public List<ObserverAction> getMatchedObserverActions(Object event) {
    List<ObserverAction> observerActions = new ArrayList<>();
    Class<?> eventType = event.getClass();
    for (Entry<Class<?>, CopyOnWriteArrayList<ObserverAction>> entry : registry.entrySet()) {
      Class<?> key = entry.getKey();
      CopyOnWriteArrayList<ObserverAction> value = entry.getValue();
      if (eventType.isAssignableFrom(key)) {
        observerActions.addAll(value);
      }
    }
    return observerActions;
  }

  private Map<Class<?>, Collection<ObserverAction>> findAllObserverActions(Object observer) {
    Map<Class<?>, Collection<ObserverAction>> observerActions = new HashMap<>();
    Class<?> clazz = observer.getClass();
    for (Method method : getAnnotationMethods(clazz)) {
      Class<?>[] parameterTypes = method.getParameterTypes();
      Class<?> eventType = parameterTypes[0];
      if (!observerActions.containsKey(eventType)) {
        observerActions.put(eventType, new ArrayList<>());
      }
      observerActions.get(eventType).add(new ObserverAction(observer, method));
    }

    return observerActions;
  }

  private List<Method> getAnnotationMethods(Class<?> clazz) {
    List<Method> methods = new ArrayList<>();
    for (Method method : clazz.getDeclaredMethods()) {
      Class<?>[] parameterTypes = method.getParameterTypes();
      if (parameterTypes == null || parameterTypes.length < 1) {
        continue;
      }
      methods.add(method);
    }
    return methods;
  }
}
