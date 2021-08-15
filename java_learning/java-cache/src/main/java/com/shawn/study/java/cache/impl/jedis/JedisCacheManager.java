package com.shawn.study.java.cache.impl.jedis;

import com.shawn.study.java.cache.impl.ConfigurableCachingProvider;
import com.shawn.study.java.cache.impl.base.AbstractCacheManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import javax.cache.Cache;
import javax.cache.configuration.Configuration;
import javax.cache.spi.CachingProvider;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisCacheManager extends AbstractCacheManager {

  public static final String JEDIS_PROPERTIES_RESOURCE_NAME =
      "META-INF/jedis-caching-provider.properties";
  private final JedisPool jedisPool;
  private final Properties jedisProperties;

  public JedisCacheManager(
      CachingProvider cachingProvider, URI uri, ClassLoader classLoader, Properties properties) {
    super(cachingProvider, uri, classLoader, properties);
    this.jedisPool = new JedisPool(uri);
    this.jedisProperties = loadJedisProperties();
  }

  @Override
  protected <K, V, C extends Configuration<K, V>> Cache<K, V> doCreateCache(
      String cacheName, C configuration) {
    final Jedis jedis = jedisPool.getResource();
    if (jedisProperties != null && !jedisProperties.isEmpty()) {
      String password = jedisProperties.getProperty("jedis.auth.password");
      jedis.auth(password);
    }
    return new JedisCache<>(this, cacheName, configuration, jedis);
  }

  @Override
  protected void doClose() {
    jedisPool.close();
  }

  private Properties loadJedisProperties() {
    ClassLoader classLoader = this.getClassLoader();
    Properties properties = new Properties();
    try {
      Enumeration<URL> enumeration = classLoader.getResources(JEDIS_PROPERTIES_RESOURCE_NAME);
      while (enumeration.hasMoreElements()) {
        URL url = enumeration.nextElement();
        try (InputStream inputStream = url.openStream();
            final InputStreamReader reader =
                new InputStreamReader(inputStream, ConfigurableCachingProvider.DEFAULT_ENCODING)) {
          properties.load(reader);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return properties;
  }
}
