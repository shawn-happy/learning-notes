package com.shawn.study.deep.in.java.configuration.source;

import java.util.Collections;
import java.util.Map;

public class MapConfigSource extends BasedConfigSource {

  private final Map<String, String> config;

  public MapConfigSource(String name, Map<String, String> config, Integer ordinal) {
    super(name, ordinal);
    this.config = config;
  }

  @Override
  public Map<String, String> getProperties() {
    return Collections.unmodifiableMap(config);
  }

  @Override
  public String getValue(String propertyName) {
    return config.get(propertyName);
  }
}
