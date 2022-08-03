package com.shawn.study.deep.in.java.configuration.source;

import java.util.Objects;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.microprofile.config.spi.ConfigSource;

public abstract class BasedConfigSource implements ConfigSource {

  private final String name;
  private final int ordinal;

  public BasedConfigSource(String name, Integer ordinal) {
    this.name = name;
    this.ordinal =
        Objects.isNull(ordinal) ? ConfigSourceOrdinal.DEFAULT_ORDINAL.getOrdinal() : ordinal;
  }

  @Override
  public String getName() {
    return name;
  }

  public boolean containsConfig(String name) {
    Set<String> propertyNames = getPropertyNames();
    if (CollectionUtils.isEmpty(propertyNames)) {
      return false;
    }
    for (String propertyName : propertyNames) {
      if (StringUtils.equals(propertyName, name)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public int getOrdinal() {
    return ordinal;
  }
}
