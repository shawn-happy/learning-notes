package com.shawn.study.java.configuration.provider;

import java.util.Iterator;
import java.util.ServiceLoader;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;

public class DefaultConfigProviderResolver extends ConfigProviderResolver {

  @Override
  public Config getConfig() {
    return getConfig(null);
  }

  @Override
  public Config getConfig(ClassLoader loader) {
    if (loader == null) {
      loader = Thread.currentThread().getContextClassLoader();
    }
    ServiceLoader<Config> configServiceLoader = ServiceLoader.load(Config.class, loader);
    Iterator<Config> iterator = configServiceLoader.iterator();
    return iterator.hasNext() ? iterator.next() : null;
  }

  @Override
  public ConfigBuilder getBuilder() {
    return null;
  }

  @Override
  public void registerConfig(Config config, ClassLoader classLoader) {}

  @Override
  public void releaseConfig(Config config) {}
}
