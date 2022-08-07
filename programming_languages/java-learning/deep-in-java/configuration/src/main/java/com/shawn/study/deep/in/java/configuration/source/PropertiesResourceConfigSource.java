package com.shawn.study.deep.in.java.configuration.source;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;

public class PropertiesResourceConfigSource extends ResourceConfigSource {

  private static final String CONFIG_SOURCE_NAME = "application.properties";
  private static final String configFileLocation = "META-INF/microprofile-config.properties";
  private static final Logger logger =
      Logger.getLogger(PropertiesResourceConfigSource.class.getName());

  private final String path;

  public PropertiesResourceConfigSource() {
    this(configFileLocation);
  }

  public PropertiesResourceConfigSource(String path) {
    this(path, Thread.currentThread().getContextClassLoader());
  }

  public PropertiesResourceConfigSource(String path, ClassLoader classLoader) {
    super(CONFIG_SOURCE_NAME, classLoader);
    this.path = path;
  }

  @Override
  protected void fillConfig(Map<String, String> config) {
    String path = getPath();
    URL resource = getClassLoader().getResource(path);
    if (resource == null) {
      logger.info("The default config file can't be found in the classpath : " + path);
      return;
    }
    try (InputStream inputStream = resource.openStream()) {
      Properties properties = new Properties();
      properties.load(inputStream);
      Set<Object> keySet = properties.keySet();
      for (Object key : keySet) {
        config.put(key.toString(), properties.getProperty(key.toString()));
      }
    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public String getPath() {
    if (StringUtils.isBlank(path)) {
      return configFileLocation;
    }
    return path;
  }
}
