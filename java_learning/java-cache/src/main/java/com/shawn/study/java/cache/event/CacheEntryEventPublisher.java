package com.shawn.study.java.cache.event;

import java.util.LinkedList;
import java.util.List;
import javax.cache.configuration.CacheEntryListenerConfiguration;
import javax.cache.event.CacheEntryEvent;

public class CacheEntryEventPublisher {

  private List<ConditionalCacheEntryEventListener> listeners = new LinkedList<>();

  public void registerCacheEntryListener(CacheEntryListenerConfiguration configuration) {
    CacheEntryEventListenerAdapter listenerAdapter =
        new CacheEntryEventListenerAdapter(configuration);
    listeners.add(listenerAdapter);
  }

  public void deregisterCacheEntryListener(CacheEntryListenerConfiguration configuration) {
    CacheEntryEventListenerAdapter listenerAdapter =
        new CacheEntryEventListenerAdapter(configuration);
    listeners.remove(listenerAdapter);
  }

  public <K, V> void publish(CacheEntryEvent<? extends K, ? extends V> event) {
    listeners.forEach(listener -> listener.onEvent(event));
  }
}
