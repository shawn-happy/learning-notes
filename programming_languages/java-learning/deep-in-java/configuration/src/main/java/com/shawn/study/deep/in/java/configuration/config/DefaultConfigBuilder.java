package com.shawn.study.deep.in.java.configuration.config;

import com.shawn.study.deep.in.java.configuration.converter.Converters;
import com.shawn.study.deep.in.java.configuration.converter.MutableConverters;
import com.shawn.study.deep.in.java.configuration.source.ConfigSources;
import com.shawn.study.deep.in.java.configuration.source.JavaSystemEnvironmentConfigSource;
import com.shawn.study.deep.in.java.configuration.source.JavaSystemPropertiesConfigSource;
import com.shawn.study.deep.in.java.configuration.source.MutableConfigSources;
import java.util.Arrays;
import java.util.ServiceLoader;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.Converter;

public class DefaultConfigBuilder implements ConfigBuilder {

  private final ConfigSources configSources;
  private final Converters converters;
  private final ClassLoader classLoader;

  public DefaultConfigBuilder(ClassLoader classLoader) {
    this.configSources = new MutableConfigSources();
    this.converters = new MutableConverters();
    if (classLoader == null) {
      this.classLoader = Thread.currentThread().getContextClassLoader();
    } else {
      this.classLoader = classLoader;
    }
  }

  @Override
  public ConfigBuilder addDefaultSources() {
    MutableConfigSources configSources = getConfigSources();
    configSources.addLast(new JavaSystemPropertiesConfigSource());
    configSources.addLast(new JavaSystemEnvironmentConfigSource());
    return this;
  }

  @Override
  public ConfigBuilder addDiscoveredSources() {
    MutableConfigSources configSources = getConfigSources();
    ServiceLoader<ConfigSource> serviceLoader =
        ServiceLoader.load(ConfigSource.class, getClassLoader());
    for (ConfigSource configSource : serviceLoader) {
      configSources.addLast(configSource);
    }
    return this;
  }

  @Override
  public ConfigBuilder addDiscoveredConverters() {
    MutableConverters converters = getConverters();
    for (Converter<?> converter : ServiceLoader.load(Converter.class, getClassLoader())) {
      converters.addConverter(converter);
    }
    return this;
  }

  @Override
  public ConfigBuilder forClassLoader(ClassLoader loader) {
    return this;
  }

  @Override
  public ConfigBuilder withSources(ConfigSource... sources) {
    MutableConfigSources configSources = getConfigSources();
    Arrays.asList(sources).forEach(configSources::addLast);
    return this;
  }

  @Override
  public ConfigBuilder withConverters(Converter<?>... converters) {
    getConverters().addConverters(converters);
    return this;
  }

  @Override
  public <T> ConfigBuilder withConverter(Class<T> type, int priority, Converter<T> converter) {
    getConverters().addConverter(converter, priority, type);
    return this;
  }

  @Override
  public Config build() {
    return new BasedConfig(getConfigSources(), converters);
  }

  protected MutableConfigSources getConfigSources() {
    configSources.sorted();
    return (MutableConfigSources) configSources;
  }

  protected MutableConverters getConverters() {
    return (MutableConverters) converters;
  }

  public ClassLoader getClassLoader() {
    return this.classLoader;
  }
}
