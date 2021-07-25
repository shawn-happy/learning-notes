package com.shawn.study.java.configuration.source;

import com.shawn.study.java.configuration.config.InMemoryConfig;
import com.shawn.study.java.configuration.constant.ConfigSourceOrdinal;
import java.util.Map;
import java.util.Set;

/**
 * the configuration in jvm memory
 *
 * @author Shawn
 * @since 1.0.1
 */
public class InMemoryConfigSource extends BaseConfigSource {

  private static final String NAME = "In JVM Memory";

  private Map<String, String> config;

  public InMemoryConfigSource() {
    super(NAME, ConfigSourceOrdinal.IN_MEMORY_ORDINAL.getOrdinal());
    this.config = InMemoryConfig.getConfigMap();
  }

  @Override
  public Set<String> getPropertyNames() {
    return config.keySet();
  }

  @Override
  public String getValue(String propertyName) {
    return config.get(propertyName);
  }
}
