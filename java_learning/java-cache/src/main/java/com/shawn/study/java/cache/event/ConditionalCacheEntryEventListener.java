package com.shawn.study.java.cache.event;

import java.util.EventListener;
import java.util.Set;
import java.util.concurrent.Executor;
import javax.cache.configuration.CacheEntryListenerConfiguration;
import javax.cache.event.CacheEntryCreatedListener;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryEventFilter;
import javax.cache.event.CacheEntryExpiredListener;
import javax.cache.event.CacheEntryListenerException;
import javax.cache.event.CacheEntryRemovedListener;
import javax.cache.event.CacheEntryUpdatedListener;
import javax.cache.event.EventType;

public interface ConditionalCacheEntryEventListener<K, V> extends EventListener {

  /**
   * Determines current listener supports the given {@link CacheEntryEvent} or not.
   *
   * @param event
   * @return The effect of returning true is that listener will be invoked
   * @throws CacheEntryListenerException
   * @see CacheEntryEventFilter#evaluate(CacheEntryEvent)
   */
  boolean supports(CacheEntryEvent<? extends K, ? extends V> event)
      throws CacheEntryListenerException;

  /**
   * Called after one entry was raised by some event.
   *
   * @param event some event
   * @see CacheEntryCreatedListener
   * @see CacheEntryUpdatedListener
   * @see CacheEntryRemovedListener
   * @see CacheEntryExpiredListener
   */
  void onEvent(CacheEntryEvent<? extends K, ? extends V> event);

  /**
   * Called after one or more entries have been created.
   *
   * @param events one or more events
   * @see CacheEntryCreatedListener
   * @see CacheEntryUpdatedListener
   * @see CacheEntryRemovedListener
   * @see CacheEntryExpiredListener
   */
  default void onEvents(Iterable<CacheEntryEvent<? extends K, ? extends V>> events) {
    events.forEach(this::onEvent);
  }

  /**
   * Get the supported {@link EventType event types}
   *
   * @return non-null
   */
  Set<EventType> getSupportedEventTypes();

  /**
   * The {@link Executor} is used to dispatch the {@link CacheEntryEvent}
   *
   * @return non-null
   * @see CacheEntryListenerConfiguration#isSynchronous()
   */
  Executor getExecutor();

  @Override
  int hashCode();

  @Override
  boolean equals(Object object);
}
