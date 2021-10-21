package com.shawn.study.java.cache.impl.memory;

import com.shawn.study.java.cache.impl.base.AbstractCache;
import com.shawn.study.java.cache.impl.base.ExpireEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;

public class InMemoryCache<K, V> extends AbstractCache<K, V> {

  private final Map<K, ExpireEntry<K, V>> cache;

  public InMemoryCache(
      CacheManager cacheManager, String cacheName, Configuration<K, V> configuration) {
    super(cacheManager, cacheName, configuration);
    this.cache = new HashMap<>();
  }

  @Override
  protected boolean containsEntry(K key) throws CacheException, ClassCastException {
    return cache.containsKey(key);
  }

  @Override
  protected ExpireEntry<K, V> getEntry(K key) throws CacheException, ClassCastException {
    return cache.get(key);
  }

  @Override
  protected Set<K> keySet() {
    return cache.keySet();
  }

  @Override
  protected void putEntry(ExpireEntry<K, V> entry) throws CacheException, ClassCastException {
    K key = entry.getKey();
    cache.put(key, entry);
  }

  @Override
  protected ExpireEntry<K, V> removeEntry(K key) throws CacheException, ClassCastException {
    return cache.remove(key);
  }

  @Override
  protected void clearEntries() throws CacheException {
    cache.clear();
  }
}
