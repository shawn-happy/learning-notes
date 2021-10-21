package com.shawn.study.java.configuration.source;

import static com.shawn.study.java.configuration.constant.ConfigSourceOrdinal.JAVA_SYSTEM_PROPERTIES_ORDINAL;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/** java system properties config source */
public class JavaSystemPropertiesConfigSource extends BaseConfigSource {

  private static final Map<String, String> JAVA_SYSTEM_PROPERTIES = new HashMap<>();

  private static final String CONFIG_SOURCE_NAME = "java_system_properties";

  static {
    Properties properties = System.getProperties();
    Set<String> propertyNames = properties.stringPropertyNames();
    propertyNames.forEach(name -> JAVA_SYSTEM_PROPERTIES.put(name, properties.getProperty(name)));
  }

  public JavaSystemPropertiesConfigSource() {
    super(CONFIG_SOURCE_NAME, JAVA_SYSTEM_PROPERTIES_ORDINAL.getOrdinal());
  }

  @Override
  public Set<String> getPropertyNames() {
    return JAVA_SYSTEM_PROPERTIES.keySet();
  }

  @Override
  public String getValue(String propertyName) {
    return JAVA_SYSTEM_PROPERTIES.get(propertyName);
  }
}
