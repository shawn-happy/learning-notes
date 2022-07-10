package com.shawn.study.java.cache.integration;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;

public class CompositeFallbackStorage extends AbstractFallbackStorage<Object, Object> {

  private static final ConcurrentMap<ClassLoader, List<FallbackStorage>> fallbackStoragesCache =
      new ConcurrentHashMap<>();

  private final List<FallbackStorage> fallbackStorages;

  public CompositeFallbackStorage(ClassLoader classLoader) {
    super(Integer.MIN_VALUE);
    this.fallbackStorages =
        fallbackStoragesCache.computeIfAbsent(classLoader, this::loadFallbackStorages);
  }

  private List<FallbackStorage> loadFallbackStorages(ClassLoader classLoader) {
    return stream(ServiceLoader.load(FallbackStorage.class, classLoader).spliterator(), false)
        .sorted(PRIORITY_COMPARATOR)
        .collect(toList());
  }

  public CompositeFallbackStorage() {
    this(Thread.currentThread().getContextClassLoader());
  }

  @Override
  public Object load(Object key) throws CacheLoaderException {
    Object value = null;
    for (FallbackStorage fallbackStorage : fallbackStorages) {
      value = fallbackStorage.load(key);
      if (value != null) {
        break;
      }
    }
    return value;
  }

  @Override
  public void write(Cache.Entry entry) throws CacheWriterException {
    fallbackStorages.forEach(fallbackStorage -> fallbackStorage.write(entry));
  }

  @Override
  public void delete(Object key) throws CacheWriterException {
    fallbackStorages.forEach(fallbackStorage -> fallbackStorage.delete(key));
  }

  @Override
  public void destroy() {
    fallbackStorages.forEach(FallbackStorage::destroy);
  }
}
