package com.shawn.study.deep.in.java.configuration.source;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.MapUtils;

public class MapConfigSource extends BasedConfigSource {

  private Map<String, String> config;

  public MapConfigSource(String name, Integer ordinal) {
    super(name, ordinal);
  }

  @Override
  public Map<String, String> getProperties() {
    return Collections.unmodifiableMap(config);
  }

  @Override
  public Set<String> getPropertyNames() {
    if (MapUtils.isEmpty(config)) {
      return null;
    }
    return config.keySet();
  }

  @Override
  public String getValue(String propertyName) {
    return config.get(propertyName);
  }

  void prepareConfig(Map<String, String> config) {
    this.config = config;
  }
}
