package com.shawn.study.java.cache.processor;

import javax.cache.Cache;
import javax.cache.configuration.CompleteConfiguration;
import javax.cache.processor.MutableEntry;

public class MutableEntryAdapter<K, V> implements MutableEntry<K, V> {

  private final K key;

  private final Cache<K, V> cache;

  private MutableEntryAdapter(K key, Cache<K, V> cache) {
    this.key = key;
    this.cache = cache;
  }

  @Override
  public boolean exists() {
    return cache.containsKey(getKey());
  }

  @Override
  public void remove() {
    cache.remove(getKey());
  }

  @Override
  public K getKey() {
    return key;
  }

  @Override
  public V getValue() {
    CompleteConfiguration configuration = cache.getConfiguration(CompleteConfiguration.class);
    // If the cache is configured to use read-through, and this method
    // would return null because the entry is missing from the cache,
    // the Cache's {@link CacheLoader} is called in an attempt to load the entry.
    return configuration.isReadThrough() ? null : cache.get(key);
  }

  @Override
  public <T> T unwrap(Class<T> clazz) {
    return cache.unwrap(clazz);
  }

  @Override
  public void setValue(V value) {
    // If exists is false and setValue is called then a mapping is added to the cache visible
    // once the EntryProcessor completes. Moreover a second invocation of exists() will return true.
    cache.put(key, value);
  }

  public static <K, V> MutableEntry<K, V> of(K key, Cache<K, V> cache) {
    return new MutableEntryAdapter<>(key, cache);
  }
}
