package com.shawn.study.java.cache.event;

import static java.util.Objects.requireNonNull;

import javax.cache.Cache;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.EventType;

public class GenericCacheEntryEvent<K, V> extends CacheEntryEvent<K, V> {

  private final K key;

  private final V oldValue;

  private final V value;

  /**
   * Constructs a cache entry event from a given cache as source
   *
   * @param source the cache that originated the event
   * @param eventType the event type for this event
   * @param key the key of {@link javax.cache.Cache.Entry}
   * @param oldValue the old value of {@link javax.cache.Cache.Entry}
   * @param value the current value of {@link javax.cache.Cache.Entry}
   */
  public GenericCacheEntryEvent(Cache source, EventType eventType, K key, V oldValue, V value) {
    super(source, eventType);
    requireNonNull(key, "The key must not be null!");
    requireNonNull(value, "The value must not be null!");
    this.key = key;
    this.oldValue = oldValue;
    this.value = value;
  }

  @Override
  public K getKey() {
    return key;
  }

  @Override
  public V getOldValue() {
    return oldValue;
  }

  @Override
  public V getValue() {
    return value;
  }

  @Override
  public boolean isOldValueAvailable() {
    return oldValue != null;
  }

  @Override
  public <T> T unwrap(Class<T> clazz) {
    return getSource().getCacheManager().unwrap(clazz);
  }

  @Override
  public String toString() {
    return "GenericCacheEntryEvent{"
        + "key="
        + getKey()
        + ", oldValue="
        + getOldValue()
        + ", value="
        + getValue()
        + ", evenType="
        + getEventType()
        + ", source="
        + getSource().getName()
        + '}';
  }

  public static <K, V> CacheEntryEvent<K, V> createdEvent(Cache<K, V> source, K key, V value) {
    return of(source, EventType.CREATED, key, null, value);
  }

  public static <K, V> CacheEntryEvent<K, V> updatedEvent(
      Cache<K, V> source, K key, V oldValue, V value) {
    return of(source, EventType.UPDATED, key, oldValue, value);
  }

  public static <K, V> CacheEntryEvent<K, V> expiredEvent(Cache<K, V> source, K key, V oldValue) {
    return of(source, EventType.EXPIRED, key, oldValue, oldValue);
  }

  public static <K, V> CacheEntryEvent<K, V> removedEvent(Cache<K, V> source, K key, V oldValue) {
    return of(source, EventType.REMOVED, key, oldValue, oldValue);
  }

  public static <K, V> CacheEntryEvent<K, V> of(
      Cache<K, V> source, EventType eventType, K key, V oldValue, V value) {
    return new GenericCacheEntryEvent<>(source, eventType, key, oldValue, value);
  }
}
