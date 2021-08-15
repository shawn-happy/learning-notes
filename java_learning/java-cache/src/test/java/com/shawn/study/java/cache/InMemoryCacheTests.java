package com.shawn.study.java.cache;

import static com.shawn.study.java.cache.util.ConfigurationUtils.cacheEntryListenerConfiguration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.shawn.study.java.cache.event.TestCacheEntryListener;
import java.io.IOException;
import java.net.URI;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class InMemoryCacheTests {

  private static final String CACHE_NAME = "in-memory-tests";

  private Cache<String, Integer> cache;
  private CachingProvider cachingProvider;
  private CacheManager cacheManager;

  @Before
  public void setup() {
    cachingProvider = Caching.getCachingProvider();
    cacheManager = cachingProvider.getCacheManager();
    // configure the cache
    MutableConfiguration<String, Integer> config =
        new MutableConfiguration<String, Integer>()
            .setManagementEnabled(true)
            .setStatisticsEnabled(true)
            .setTypes(String.class, Integer.class)
            .setWriteThrough(true);
    cache = cacheManager.createCache(CACHE_NAME, config);
  }

  @After
  public void destroy() {
    cache.removeAll();
    assertFalse(cache.isClosed());
    cacheManager.destroyCache(CACHE_NAME);
    assertTrue(cache.isClosed());
    // DO NOTHING after close() method being invoked.
    cache.close();
    assertTrue(cache.isClosed());
    // Test operation after cache closed.
    assertThrows(IllegalStateException.class, () -> cache.put("test-key", 2));
  }

  @Test
  public void testSampleInMemory() throws IOException {
    CachingProvider cachingProvider = Caching.getCachingProvider();
    CacheManager cacheManager =
        cachingProvider.getCacheManager(URI.create("in-memory://localhost/"), null);
    // configure the cache
    MutableConfiguration<String, Integer> config =
        new MutableConfiguration<String, Integer>()
            .setManagementEnabled(true)
            .setStatisticsEnabled(true)
            .setTypes(String.class, Integer.class)
            .setWriteThrough(true);

    // create the cache
    Cache<String, Integer> cache = cacheManager.createCache("simpleCache", config);

    // add listener
    cache.registerCacheEntryListener(
        cacheEntryListenerConfiguration(new TestCacheEntryListener<>()));

    // cache operations
    String key = "key";
    Integer value1 = 1;
    cache.put("key", value1);

    // update
    value1 = 2;
    cache.put("key", value1);

    Integer value2 = cache.get(key);
    assertEquals(value1, value2);
    cache.remove(key);
    assertNull(cache.get(key));
  }
}
