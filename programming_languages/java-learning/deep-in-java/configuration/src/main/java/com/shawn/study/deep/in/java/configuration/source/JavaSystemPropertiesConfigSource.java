package com.shawn.study.deep.in.java.configuration.source;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class JavaSystemPropertiesConfigSource extends MapConfigSource {

  private static final Map<String, String> JAVA_SYSTEM_PROPERTIES = new HashMap<>();
  private static final String CONFIG_SOURCE_NAME = "java.system.properties";

  static {
    Properties properties = System.getProperties();
    Set<String> propertyNames = properties.stringPropertyNames();
    propertyNames.forEach(name -> JAVA_SYSTEM_PROPERTIES.put(name, properties.getProperty(name)));
  }

  public JavaSystemPropertiesConfigSource() {
    super(CONFIG_SOURCE_NAME, ConfigSourceOrdinal.JAVA_SYSTEM_PROPERTIES_ORDINAL.getOrdinal());
    super.prepareConfig(JAVA_SYSTEM_PROPERTIES);
  }
}
