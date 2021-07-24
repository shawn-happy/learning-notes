package com.shawn.study.java.configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigValue;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.Converter;

/**
 * config facade
 *
 * @author Shawn
 * @since 1.0.0
 */
public class DefaultConfig implements Config {

  private List<ConfigSource> configSourceList = new ArrayList<>();

  public DefaultConfig() {
    ClassLoader classLoader = getClass().getClassLoader();
    ServiceLoader<ConfigSource> serviceLoader = ServiceLoader.load(ConfigSource.class, classLoader);
    serviceLoader.forEach(configSourceList::add);
    configSourceList.sort(DefaultConfigSourceComparator.INSTANCE);
  }

  @Override
  public <T> T getValue(String s, Class<T> aClass) {
    return (T) getPropertyValue(s);
  }

  @Override
  public ConfigValue getConfigValue(String s) {
    return null;
  }

  @Override
  public <T> Optional<T> getOptionalValue(String s, Class<T> aClass) {
    return Optional.ofNullable(getValue(s, aClass));
  }

  @Override
  public Iterable<String> getPropertyNames() {
    return null;
  }

  @Override
  public Iterable<ConfigSource> getConfigSources() {
    return Collections.unmodifiableList(configSourceList);
  }

  @Override
  public <T> Optional<Converter<T>> getConverter(Class<T> aClass) {
    return Optional.empty();
  }

  @Override
  public <T> T unwrap(Class<T> aClass) {
    return null;
  }

  protected String getPropertyValue(String propertyName) {
    String propertyValue = null;
    for (ConfigSource configSource : configSourceList) {
      propertyValue = configSource.getValue(propertyName);
      if (propertyValue != null) {
        break;
      }
    }
    return propertyValue;
  }
}
