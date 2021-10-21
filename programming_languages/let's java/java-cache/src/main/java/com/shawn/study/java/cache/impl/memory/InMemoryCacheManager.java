package com.shawn.study.java.cache.impl.memory;

import com.shawn.study.java.cache.impl.base.AbstractCacheManager;
import java.net.URI;
import java.util.Properties;
import javax.cache.Cache;
import javax.cache.configuration.Configuration;
import javax.cache.spi.CachingProvider;

public class InMemoryCacheManager extends AbstractCacheManager {

  public InMemoryCacheManager(
      CachingProvider cachingProvider, URI uri, ClassLoader classLoader, Properties properties) {
    super(cachingProvider, uri, classLoader, properties);
  }

  @Override
  protected <K, V, C extends Configuration<K, V>> Cache doCreateCache(
      String cacheName, C configuration) {
    return new InMemoryCache<K, V>(this, cacheName, configuration);
  }
}
