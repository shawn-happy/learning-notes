package com.shawn.study.java.cache.impl.base;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.util.Map;
import javax.cache.Cache;

public class ExpireEntry<K, V> implements Cache.Entry<K, V>, Serializable {

  private final K key;

  private V value;

  private long timestamp;

  private ExpireEntry(K key, V value) throws NullPointerException {
    requireKeyNotNull(key);
    this.key = key;
    this.setValue(value);
    this.timestamp = Long.MAX_VALUE; // default
  }

  @Override
  public K getKey() {
    return key;
  }

  @Override
  public V getValue() {
    return value;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setValue(V value) {
    requireValueNotNull(value);
    this.value = value;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  public boolean isExpired() {
    return getExpiredTime() < 1;
  }

  public boolean isEternal() {
    return Long.MAX_VALUE == getTimestamp();
  }

  public long getExpiredTime() {
    return getTimestamp() - System.currentTimeMillis();
  }

  @Override
  public <T> T unwrap(Class<T> clazz) {
    T value = null;
    try {
      value = clazz.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
    return value;
  }

  @Override
  public String toString() {
    return "ExpireEntry{" + "key=" + key + ", value=" + value + ", timestamp=" + timestamp + '}';
  }

  public static <K, V> ExpireEntry<K, V> of(Map.Entry<K, V> entry) {
    return new ExpireEntry(entry.getKey(), entry.getValue());
  }

  public static <K, V> ExpireEntry<K, V> of(K key, V value) {
    return new ExpireEntry(key, value);
  }

  public static <K> void requireKeyNotNull(K key) {
    requireNonNull(key, "The key must not be null.");
  }

  public static <V> void requireValueNotNull(V value) {
    requireNonNull(value, "The value must not be null.");
  }

  public static <V> void requireOldValueNotNull(V oldValue) {
    requireNonNull(oldValue, "The oldValue must not be null.");
  }
}
