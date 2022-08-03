package com.shawn.study.deep.in.java.configuration;

import java.util.Optional;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigSource;

public class DefaultConfig implements Config {

  @Override
  public <T> T getValue(String s, Class<T> aClass) {
    return null;
  }

  @Override
  public <T> Optional<T> getOptionalValue(String s, Class<T> aClass) {
    return Optional.empty();
  }

  @Override
  public Iterable<String> getPropertyNames() {
    return null;
  }

  @Override
  public Iterable<ConfigSource> getConfigSources() {
    return null;
  }
}
