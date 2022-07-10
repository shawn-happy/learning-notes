package com.shawn.study.java.configuration.provider;

import com.shawn.study.java.configuration.DefaultConfig;
import com.shawn.study.java.configuration.converter.Converters;
import com.shawn.study.java.configuration.source.ConfigSources;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.Converter;

public class DefaultConfigBuilder implements ConfigBuilder {

  private final ConfigSources configSources;

  private final Converters converters;

  public DefaultConfigBuilder(ClassLoader classLoader) {
    this.configSources = new ConfigSources(classLoader);
    converters = new Converters(classLoader);
  }

  @Override
  public ConfigBuilder addDefaultSources() {
    configSources.addDefaultSources();
    return this;
  }

  @Override
  public ConfigBuilder addDiscoveredSources() {
    configSources.addDiscoveredSources();
    return this;
  }

  @Override
  public ConfigBuilder addDiscoveredConverters() {
    converters.addDiscoveredConverters();
    return this;
  }

  @Override
  public ConfigBuilder forClassLoader(ClassLoader loader) {
    configSources.setClassLoader(loader);
    converters.setClassLoader(loader);
    return this;
  }

  @Override
  public ConfigBuilder withSources(ConfigSource... sources) {
    configSources.addConfigSources(sources);
    return this;
  }

  @Override
  public ConfigBuilder withConverters(Converter<?>... converters) {
    this.converters.addConverters(converters);
    return this;
  }

  @Override
  public <T> ConfigBuilder withConverter(Class<T> type, int priority, Converter<T> converter) {
    this.converters.addConverter(converter, priority, type);
    return this;
  }

  @Override
  public Config build() {
    return new DefaultConfig(configSources, converters);
  }
}
