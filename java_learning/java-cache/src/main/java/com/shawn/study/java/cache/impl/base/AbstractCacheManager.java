package com.shawn.study.java.cache.impl.base;

import static java.lang.String.format;
import static java.util.Collections.emptyMap;
import static java.util.Objects.requireNonNull;

import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.logging.Logger;
import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;

public abstract class AbstractCacheManager implements CacheManager {

  private static final Consumer<Cache<?, ?>> CLEAR_CACHE_OPERATION = Cache::clear;
  private static final Consumer<Cache<?, ?>> CLOSE_CACHE_OPERATION = Cache::close;

  private final URI uri;
  private final Properties properties;
  private final ClassLoader classLoader;
  private final CachingProvider cachingProvider;
  private final Logger logger = Logger.getLogger(this.getClass().getName());

  private final ConcurrentMap<String, Map<KeyValueTypePair, Cache<?, ?>>> cacheRepository =
      new ConcurrentHashMap<>();
  private volatile boolean closed;

  public AbstractCacheManager(
      CachingProvider cachingProvider, URI uri, ClassLoader classLoader, Properties properties) {
    this.cachingProvider = cachingProvider;
    this.uri = Optional.of(uri).orElse(cachingProvider.getDefaultURI());
    this.properties = Optional.of(properties).orElse(cachingProvider.getDefaultProperties());
    this.classLoader = Optional.of(classLoader).orElse(cachingProvider.getDefaultClassLoader());
  }

  @Override
  public CachingProvider getCachingProvider() {
    return cachingProvider;
  }

  @Override
  public URI getURI() {
    return uri;
  }

  @Override
  public ClassLoader getClassLoader() {
    return classLoader;
  }

  @Override
  public Properties getProperties() {
    return properties;
  }

  @Override
  public <K, V, C extends Configuration<K, V>> Cache<K, V> createCache(
      String cacheName, C configuration) throws IllegalArgumentException {
    // If a Cache with the specified name is known to the CacheManager, a CacheException is thrown.
    if (!cacheRepository.getOrDefault(cacheName, emptyMap()).isEmpty()) {
      throw new CacheException(
          format(
              "The Cache whose name is '%s' is already existed, "
                  + "please try another name to create a new Cache.",
              cacheName));
    }
    // If a Cache with the specified name is unknown the CacheManager, one is created according to
    // the provided Configuration after which it becomes managed by the CacheManager.
    return getOrCreateCache(cacheName, configuration, true);
  }

  @Override
  public <K, V> Cache<K, V> getCache(String cacheName, Class<K> keyType, Class<V> valueType) {
    MutableConfiguration<K, V> configuration =
        new MutableConfiguration<K, V>().setTypes(keyType, valueType);
    return getOrCreateCache(cacheName, configuration, false);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <K, V> Cache<K, V> getCache(String cacheName) {
    return getCache(cacheName, (Class<K>) Object.class, (Class<V>) Object.class);
  }

  @Override
  public Iterable<String> getCacheNames() {
    assertNotClosed();
    return cacheRepository.keySet();
  }

  @Override
  public void destroyCache(String cacheName) {
    requireNonNull(cacheName, "The 'cacheName' argument must not be null.");
    assertNotClosed();
    Map<KeyValueTypePair, Cache<?, ?>> cacheMap = cacheRepository.remove(cacheName);
    if (cacheMap != null) {
      iterateCaches(cacheMap.values(), CLEAR_CACHE_OPERATION, CLOSE_CACHE_OPERATION);
    }
  }

  @Override
  public void enableManagement(String cacheName, boolean enabled) {
    assertNotClosed();
    // TODO
    throw new UnsupportedOperationException("TO support in the future!");
  }

  @Override
  public void enableStatistics(String cacheName, boolean enabled) {
    assertNotClosed();
    // TODO
    throw new UnsupportedOperationException("TO support in the future!");
  }

  @Override
  public void close() {
    if (isClosed()) {
      logger.warning("The CacheManager has been closed, current close operation will be ignored!");
      return;
    }
    for (Map<KeyValueTypePair, Cache<?, ?>> cacheMap : cacheRepository.values()) {
      iterateCaches(cacheMap.values(), CLOSE_CACHE_OPERATION);
    }
    doClose();
    this.closed = true;
  }

  @Override
  public boolean isClosed() {
    return closed;
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

  protected abstract <K, V, C extends Configuration<K, V>> Cache<K, V> doCreateCache(
      String cacheName, C configuration);

  protected void doClose() {}

  @SuppressWarnings("unchecked")
  private <K, V, C extends Configuration<K, V>> Cache<K, V> getOrCreateCache(
      String cacheName, C configuration, boolean created)
      throws IllegalArgumentException, IllegalStateException {
    assertNotClosed();

    Map<KeyValueTypePair, Cache<?, ?>> cacheMap =
        cacheRepository.computeIfAbsent(cacheName, n -> new ConcurrentHashMap<>());

    return (Cache<K, V>)
        cacheMap.computeIfAbsent(
            new KeyValueTypePair(configuration.getKeyType(), configuration.getValueType()),
            key -> created ? doCreateCache(cacheName, configuration) : null);
  }

  @SafeVarargs
  private final void iterateCaches(
      Iterable<Cache<?, ?>> caches, Consumer<Cache<?, ?>>... cacheOperations) {
    for (Cache<?, ?> cache : caches) {
      for (Consumer<Cache<?, ?>> cacheOperation : cacheOperations) {
        try {
          cacheOperation.accept(cache);
        } catch (Throwable e) {
          logger.finest(e.getMessage());
        }
      }
    }
  }

  private void assertNotClosed() throws IllegalStateException {
    if (isClosed()) {
      throw new IllegalStateException(
          "The CacheManager has been closed, current operation should not be invoked!");
    }
  }
}
