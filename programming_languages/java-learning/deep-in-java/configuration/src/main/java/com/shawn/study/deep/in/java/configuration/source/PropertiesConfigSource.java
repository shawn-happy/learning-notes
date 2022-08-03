package com.shawn.study.deep.in.java.configuration.source;

import com.shawn.study.deep.in.java.configuration.utils.ConfigSourceUtils;
import java.util.Map;
import java.util.Properties;

public class PropertiesConfigSource extends MapConfigSource {

  private static final String NAME = "PropertiesConfigSource";

  public PropertiesConfigSource(Properties properties) {
    super(
        NAME,
        ConfigSourceUtils.toMap(properties),
        ConfigSourceOrdinal.PROPERTIES_ORDINAL.getOrdinal());
  }

  public PropertiesConfigSource(Map<String, String> config) {
    super(NAME, config, ConfigSourceOrdinal.PROPERTIES_ORDINAL.getOrdinal());
  }
}
