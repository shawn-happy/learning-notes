package com.shawn.study.deep.in.java.configuration.provider;

import com.shawn.study.deep.in.java.configuration.config.DefaultConfigBuilder;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;

public class DefaultConfigProviderResolver extends ConfigProviderResolver {

  private final ConfigRegistry configRegistry = new DefaultConfigRegistry();

  @Override
  public Config getConfig() {
    return getConfig(resolveClassLoader(null));
  }

  @Override
  public Config getConfig(ClassLoader classLoader) {
    return newConfigBuilder(classLoader).build();
  }

  @Override
  public ConfigBuilder getBuilder() {
    return newConfigBuilder(resolveClassLoader(null));
  }

  @Override
  public void registerConfig(Config config, ClassLoader classLoader) {
    configRegistry.addConfig(config, classLoader);
  }

  @Override
  public void releaseConfig(Config config) {
    configRegistry.removeIfExists(config);
  }

  private ClassLoader resolveClassLoader(ClassLoader classLoader) {
    return classLoader == null ? this.getClass().getClassLoader() : classLoader;
  }

  private ConfigBuilder newConfigBuilder(ClassLoader classLoader) {
    return new DefaultConfigBuilder(classLoader)
        .addDefaultSources()
        .addDiscoveredSources()
        .addDiscoveredConverters();
  }
}
