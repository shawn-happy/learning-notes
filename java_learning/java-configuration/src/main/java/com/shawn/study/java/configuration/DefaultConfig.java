package com.shawn.study.java.configuration;

import static java.util.stream.StreamSupport.stream;

import com.shawn.study.java.configuration.converter.Converters;
import com.shawn.study.java.configuration.source.ConfigSources;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

  private final ConfigSources configSources;
  private final Converters converters;

  public DefaultConfig(ConfigSources configSources, Converters converters) {
    this.configSources = configSources;
    this.converters = converters;
  }

  @Override
  public <T> T getValue(String propertyName, Class<T> propertyType) {
    ConfigValue configValue = getConfigValue(propertyName);
    if (configValue == null) {
      return null;
    }
    String propertyValue = configValue.getValue();
    // String 转换成目标类型
    Converter<T> converter = doGetConverter(propertyType);
    return converter == null ? null : converter.convert(propertyValue);
  }

  @Override
  public ConfigValue getConfigValue(String propertyName) {

    String propertyValue = null;

    ConfigSource configSource = null;

    for (ConfigSource source : configSources) {
      configSource = source;
      propertyValue = configSource.getValue(propertyName);
      if (propertyValue != null) {
        break;
      }
    }
    if (propertyValue == null) { // Not found
      return null;
    }

    return newConfigValue(
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
    return stream(configSources.spliterator(), false)
        .map(ConfigSource::getPropertyNames)
        .collect(LinkedHashSet::new, Set::addAll, Set::addAll);
  }

  @Override
  public Iterable<ConfigSource> getConfigSources() {
    return configSources;
  }

  @Override
  public <T> Optional<Converter<T>> getConverter(Class<T> propertyType) {
    Converter<T> converter = doGetConverter(propertyType);
    return converter == null ? Optional.empty() : Optional.of(converter);
  }

  @Override
  public <T> T unwrap(Class<T> aClass) {
    return null;
  }

  private <T> Converter<T> doGetConverter(Class<T> propertyType) {
    List<Converter> converters = this.converters.getConverters(propertyType);
    return converters.isEmpty() ? null : converters.get(0);
  }

  private ConfigValue newConfigValue(
      String name, String value, String rawValue, String sourceName, int sourceOrdinal) {
    return new DefaultConfigValue(name, value, rawValue, sourceName, sourceOrdinal);
  }

  private static class DefaultConfigValue implements ConfigValue {

    private final String name;

    private final String value;

    private final String rawValue;

    private final String sourceName;

    private final int sourceOrdinal;

    DefaultConfigValue(
        String name, String value, String rawValue, String sourceName, int sourceOrdinal) {
      this.name = name;
      this.value = value;
      this.rawValue = rawValue;
      this.sourceName = sourceName;
      this.sourceOrdinal = sourceOrdinal;
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public String getValue() {
      return value;
    }

    @Override
    public String getRawValue() {
      return rawValue;
    }

    @Override
    public String getSourceName() {
      return sourceName;
    }

    @Override
    public int getSourceOrdinal() {
      return sourceOrdinal;
    }
  }
}
