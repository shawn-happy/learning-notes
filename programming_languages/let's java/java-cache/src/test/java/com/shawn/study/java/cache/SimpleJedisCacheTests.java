package com.shawn.study.java.cache;

import static com.shawn.study.java.cache.util.ConfigurationUtils.cacheEntryListenerConfiguration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.shawn.study.java.cache.event.TestCacheEntryListener;
import java.net.URI;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;
import org.junit.Test;

public class SimpleJedisCacheTests {

  @Test
  public void testSampleRedis() {
    CachingProvider cachingProvider = Caching.getCachingProvider();
    CacheManager cacheManager =
        cachingProvider.getCacheManager(URI.create("redis://127.0.0.1:6379/"), null);
    // configure the cache
    MutableConfiguration<String, Integer> config =
        new MutableConfiguration<String, Integer>().setTypes(String.class, Integer.class);

    // create the cache
    Cache<String, Integer> cache = cacheManager.createCache("redisCache", config);

    // add listener
    cache.registerCacheEntryListener(
        cacheEntryListenerConfiguration(new TestCacheEntryListener<>()));

    // cache operations
    String key = "redis-key";
    Integer value1 = 1;
    cache.put(key, value1);

    // update
    value1 = 2;
    cache.put(key, value1);

    Integer value2 = cache.get(key);
    assertEquals(value1, value2);
    cache.remove(key);
    assertNull(cache.get(key));
  }
}
