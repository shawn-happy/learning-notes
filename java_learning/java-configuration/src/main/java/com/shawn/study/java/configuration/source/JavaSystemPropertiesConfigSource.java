package com.shawn.study.java.configuration.source;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.eclipse.microprofile.config.spi.ConfigSource;

/** java system properties config source */
public class JavaSystemPropertiesConfigSource implements ConfigSource {

  private static final Map<String, String> JAVA_SYSTEM_PROPERTIES = new HashMap<>();

  private static final String CONFIG_SOURCE_NAME = "java_system_properties";

  static {
    Properties properties = System.getProperties();
    Set<String> propertyNames = properties.stringPropertyNames();
    propertyNames.forEach(name -> JAVA_SYSTEM_PROPERTIES.put(name, properties.getProperty(name)));
  }

  @Override
  public Set<String> getPropertyNames() {
    return JAVA_SYSTEM_PROPERTIES.keySet();
  }

  @Override
  public String getValue(String propertyName) {
    return JAVA_SYSTEM_PROPERTIES.get(propertyName);
  }

  @Override
  public String getName() {
    return CONFIG_SOURCE_NAME;
  }
}
