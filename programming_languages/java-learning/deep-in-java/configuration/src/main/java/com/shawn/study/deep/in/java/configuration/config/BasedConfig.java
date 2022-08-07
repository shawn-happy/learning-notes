package com.shawn.study.deep.in.java.configuration.config;

import com.shawn.study.deep.in.java.configuration.converter.Converters;
import com.shawn.study.deep.in.java.configuration.source.ConfigSources;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.StreamSupport;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigValue;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.Converter;

class BasedConfig implements Config {

  private final ConfigSources configSources;
  private final Converters converters;

  BasedConfig(ConfigSources configSources, Converters converters) {
    this.configSources = configSources;
    this.converters = converters;
  }

  @Override
  public <T> T getValue(String propertyName, Class<T> propertyType) {
    ConfigValue configValue = getConfigValue(propertyName);
    if (configValue == null) {
      return null;
    }
    String value = configValue.getValue();
    Converter<T> converter = getConverter(propertyType).orElse(null);
    return converter == null ? null : converter.convert(value);
  }

  @Override
  public ConfigValue getConfigValue(String propertyName) {
    String propertyValue = null;
    ConfigSource configSource = null;
    for (ConfigSource source : configSources) {
      propertyValue = source.getValue(propertyName);
      if (!Objects.isNull(propertyValue)) {
        configSource = source;
        break;
      }
    }

    if (Objects.isNull(propertyValue)) { // Not found
      return null;
    }
    return new BasedConfigValue(
        propertyName,
        propertyValue,
        propertyValue,
        configSource.getName(),
        configSource.getOrdinal());
  }

  @Override
  public <T> Optional<T> getOptionalValue(String propertyName, Class<T> propertyType) {
    return Optional.ofNullable(getValue(propertyName, propertyType));
  }

  @Override
  public Iterable<String> getPropertyNames() {
    return StreamSupport.stream(configSources.spliterator(), false)
        .map(ConfigSource::getPropertyNames)
        .collect(LinkedHashSet::new, Set::addAll, Set::addAll);
  }

  @Override
  public Iterable<ConfigSource> getConfigSources() {
    return configSources;
  }

  @Override
  public <T> Optional<Converter<T>> getConverter(Class<T> forType) {
    Converter<T> converter = doGetConverter(forType);
    return converter == null ? Optional.empty() : Optional.of(converter);
  }

  private <T> Converter<T> doGetConverter(Class<T> forType) {
    return (Converter<T>) converters.getConverter(forType);
  }

  @Override
  public <T> T unwrap(Class<T> type) {
    return null;
  }
}
