package com.shawn.study.java.cache.impl;

import static java.lang.String.format;

import com.shawn.study.java.cache.impl.base.AbstractCacheManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URL;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;
import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.OptionalFeature;
import javax.cache.spi.CachingProvider;

/** */
public class ConfigurableCachingProvider implements CachingProvider {

  /** The resource name of {@link #getDefaultProperties() the default properties}. */
  public static final String DEFAULT_PROPERTIES_RESOURCE_NAME =
      "META-INF/default-caching-provider.properties";

  /**
   * The prefix of property name for the mappings of {@link CacheManager}, e.g:
   *
   * <p>javax.cache.CacheManager.mappings.${uri.scheme}=com.acme.SomeSchemeCacheManager
   */
  public static final String CACHE_MANAGER_MAPPINGS_PROPERTY_PREFIX =
      "javax.cache.CacheManager.mappings.";

  public static final String DEFAULT_ENCODING = System.getProperty("file.encoding", "UTF-8");

  private final URI DEFAULT_URI = URI.create("in-memory://localhost/");

  private Properties defaultProperties;

  private final ConcurrentMap<String, CacheManager> cacheManagersRepository =
      new ConcurrentHashMap<>();

  @Override
  public CacheManager getCacheManager(URI uri, ClassLoader classLoader, Properties properties) {
    URI actualURI = getOrDefault(uri, this::getDefaultURI);
    ClassLoader actualClassLoader = getOrDefault(classLoader, this::getDefaultClassLoader);
    Properties actualProperties = getDefaultProperties();
    if (properties != null && properties.isEmpty()) {
      actualProperties.putAll(properties);
    }
    String key = generateCacheManagerKey(actualURI, actualClassLoader, actualProperties);

    return cacheManagersRepository.computeIfAbsent(
        key, k -> newCacheManager(actualURI, actualClassLoader, actualProperties));
  }

  @Override
  public ClassLoader getDefaultClassLoader() {
    ClassLoader classLoader = Caching.getDefaultClassLoader();
    if (classLoader == null) {
      classLoader = this.getClass().getClassLoader();
    }
    return classLoader;
  }

  @Override
  public URI getDefaultURI() {
    return DEFAULT_URI;
  }

  @Override
  public Properties getDefaultProperties() {
    if (this.defaultProperties == null) {
      this.defaultProperties = loadDefaultProperties();
    }
    return this.defaultProperties;
  }

  @Override
  public CacheManager getCacheManager(URI uri, ClassLoader classLoader) {
    return getCacheManager(uri, classLoader, getDefaultProperties());
  }

  @Override
  public CacheManager getCacheManager() {
    return getCacheManager(getDefaultURI(), getDefaultClassLoader(), getDefaultProperties());
  }

  @Override
  public void close() {
    close(getDefaultClassLoader());
  }

  @Override
  public void close(ClassLoader classLoader) {
    close(getDefaultURI(), getDefaultClassLoader());
  }

  @Override
  public void close(URI uri, ClassLoader classLoader) {
    for (CacheManager cacheManager : cacheManagersRepository.values()) {
      if (Objects.equals(cacheManager.getURI(), uri)
          && Objects.equals(cacheManager.getClassLoader(), classLoader)) {
        cacheManager.close();
      }
    }
  }

  @Override
  public boolean isSupported(OptionalFeature optionalFeature) {
    return false;
  }

  private <T> T getOrDefault(T value, Supplier<T> defaultValue) {
    return value == null ? defaultValue.get() : value;
  }

  private Properties loadDefaultProperties() {
    ClassLoader classLoader = getDefaultClassLoader();
    Properties defaultProperties = new Properties();
    try {
      Enumeration<URL> enumeration = classLoader.getResources(DEFAULT_PROPERTIES_RESOURCE_NAME);
      while (enumeration.hasMoreElements()) {
        URL url = enumeration.nextElement();
        try (InputStream inputStream = url.openStream();
            final InputStreamReader reader = new InputStreamReader(inputStream, DEFAULT_ENCODING)) {
          defaultProperties.load(reader);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return defaultProperties;
  }

  private String generateCacheManagerKey(URI uri, ClassLoader classLoader, Properties properties) {
    return uri.toASCIIString() + "-" + classLoader + "-" + properties;
  }

  private static String getCacheManagerClassNamePropertyName(URI uri) {
    String scheme = uri.getScheme();
    return CACHE_MANAGER_MAPPINGS_PROPERTY_PREFIX + scheme;
  }

  private String getCacheManagerClassName(URI uri, Properties properties) {
    String propertyName = getCacheManagerClassNamePropertyName(uri);
    String className = properties.getProperty(propertyName);
    if (className == null) {
      throw new IllegalStateException(
          format(
              "The implementation class name of %s that is the value of property '%s' "
                  + "must be configured in the Properties[%s]",
              CacheManager.class.getName(), propertyName, properties));
    }
    return className;
  }

  private Class<? extends AbstractCacheManager> getCacheManagerClass(
      URI uri, ClassLoader classLoader, Properties properties)
      throws ClassNotFoundException, ClassCastException {
    String className = getCacheManagerClassName(uri, properties);
    Class<?> cacheManagerClass = classLoader.loadClass(className);
    // The AbstractCacheManager class must be extended by the implementation class,
    // because the constructor of the implementation class must have four arguments in order:
    // [0] - CachingProvider
    // [1] - URI
    // [2] - ClassLoader
    // [3] - Properties
    if (!AbstractCacheManager.class.isAssignableFrom(cacheManagerClass)) {
      throw new ClassCastException(
          format(
              "The implementation class of %s must extend %s",
              CacheManager.class.getName(), AbstractCacheManager.class.getName()));
    }

    return (Class<? extends AbstractCacheManager>) cacheManagerClass;
  }

  private CacheManager newCacheManager(URI uri, ClassLoader classLoader, Properties properties) {
    CacheManager cacheManager = null;
    try {
      Class<? extends AbstractCacheManager> cacheManagerClass =
          getCacheManagerClass(uri, classLoader, properties);
      Class<?>[] parameterTypes =
          new Class[] {CachingProvider.class, URI.class, ClassLoader.class, Properties.class};
      Constructor<? extends AbstractCacheManager> constructor =
          cacheManagerClass.getConstructor(parameterTypes);
      cacheManager = constructor.newInstance(this, uri, classLoader, properties);
    } catch (Throwable e) {
      throw new CacheException(e);
    }
    return cacheManager;
  }
}
