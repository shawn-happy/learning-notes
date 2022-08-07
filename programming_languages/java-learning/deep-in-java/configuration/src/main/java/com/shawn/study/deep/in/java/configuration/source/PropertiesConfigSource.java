package com.shawn.study.deep.in.java.configuration.source;

import com.shawn.study.deep.in.java.configuration.utils.ConfigSourceUtils;
import java.util.Map;
import java.util.Properties;

public class PropertiesConfigSource extends MapConfigSource {

  private static final String NAME = "properties.config";

  public PropertiesConfigSource(Properties properties) {
    this(ConfigSourceUtils.toMap(properties));
  }

  public PropertiesConfigSource(Map<String, String> config) {
    super(NAME, ConfigSourceOrdinal.PROPERTIES_ORDINAL.getOrdinal());
    prepareConfig(config);
  }
}
