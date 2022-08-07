package com.shawn.study.deep.in.java.configuration.source;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public abstract class ResourceConfigSource extends MapConfigSource {

  private final Logger logger = Logger.getLogger(this.getClass().getName());

  private final ClassLoader classLoader;
  private final Map<String, String> config;

  public ResourceConfigSource(String name, ClassLoader classLoader) {
    super(name, ConfigSourceOrdinal.RESOURCE_ORDINAL.getOrdinal());
    this.classLoader = classLoader;
    this.config = new HashMap<>();
    fillConfig(config);
    prepareConfig(config);
  }

  protected abstract void fillConfig(Map<String, String> config);

  public abstract String getPath();

  public ClassLoader getClassLoader() {
    return classLoader;
  }
}
