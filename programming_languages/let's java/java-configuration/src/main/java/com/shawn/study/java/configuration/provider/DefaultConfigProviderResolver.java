package com.shawn.study.java.configuration.provider;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;

public class DefaultConfigProviderResolver extends ConfigProviderResolver {

  private ConcurrentMap<ClassLoader, Config> configsRepository = new ConcurrentHashMap<>();

  @Override
  public Config getConfig() {
    return getConfig(getClass().getClassLoader());
  }

  @Override
  public Config getConfig(ClassLoader loader) {
    return configsRepository.computeIfAbsent(loader, this::newConfig);
  }

  @Override
  public ConfigBuilder getBuilder() {
    return newConfigBuilder(null);
  }

  @Override
  public void registerConfig(Config config, ClassLoader classLoader) {
    configsRepository.put(classLoader, config);
  }

  @Override
  public void releaseConfig(Config config) {
    List<ClassLoader> targetKeys = new LinkedList<>();
    for (Map.Entry<ClassLoader, Config> entry : configsRepository.entrySet()) {
      if (Objects.equals(config, entry.getValue())) {
        targetKeys.add(entry.getKey());
      }
    }
    targetKeys.forEach(configsRepository::remove);
  }

  private ConfigBuilder newConfigBuilder(ClassLoader classLoader) {
    return new DefaultConfigBuilder(resolveClassLoader(classLoader));
  }

  private Config newConfig(ClassLoader classLoader) {
    return newConfigBuilder(classLoader).build();
  }

  private ClassLoader resolveClassLoader(ClassLoader classLoader) {
    return classLoader == null ? this.getClass().getClassLoader() : classLoader;
  }
}
