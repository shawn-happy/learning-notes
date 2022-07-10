package com.shawn.study.java.configuration.source;

import static com.shawn.study.java.configuration.constant.ConfigSourceOrdinal.CLASSPATH_PROPERTIES_ORDINAL;

import com.shawn.study.java.configuration.exception.LoadConfigException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class ClasspathPropertiesConfigSource extends ClasspathConfigSource {

  private static final String NAME = "Classpath Properties Config Source";
  private static final String RESOURCE_NAME = "application.properties";

  private static final Map<String, String> CONFIG_MAP = new HashMap<>();

  public ClasspathPropertiesConfigSource() {
    super(RESOURCE_NAME, NAME, CLASSPATH_PROPERTIES_ORDINAL.getOrdinal());
  }

  protected void load(InputStream classpathFile) {
    Properties properties = new Properties();
    try {
      properties.load(classpathFile);
    } catch (IOException e) {
      throw new LoadConfigException("failed to load properties config", e);
    }
    Set<String> propertyNames = properties.stringPropertyNames();
    propertyNames.forEach(name -> CONFIG_MAP.put(name, properties.getProperty(name)));
  }

  @Override
  public Set<String> getPropertyNames() {
    return CONFIG_MAP.keySet();
  }

  @Override
  public String getValue(String propertyName) {
    return CONFIG_MAP.get(propertyName);
  }
}
