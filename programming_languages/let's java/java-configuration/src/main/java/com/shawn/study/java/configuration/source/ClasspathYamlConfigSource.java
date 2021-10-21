package com.shawn.study.java.configuration.source;

import static com.shawn.study.java.configuration.constant.ConfigSourceOrdinal.CLASSPATH_YAML_ORDINAL;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.yaml.snakeyaml.Yaml;

public class ClasspathYamlConfigSource extends ClasspathConfigSource {

  private static final String RESOURCE_NAME = "application.yml";
  private static final String CONFIG_SOURCE_NAME = "Classpath Yaml Config Source";
  private static final Map<String, String> CONFIG_MAP = new HashMap<>();

  public ClasspathYamlConfigSource() {
    super(RESOURCE_NAME, CONFIG_SOURCE_NAME, CLASSPATH_YAML_ORDINAL.getOrdinal());
  }

  @Override
  @SuppressWarnings("unchecked")
  protected void load(InputStream classpathFile) {
    Yaml yaml = new Yaml();
    Map<String, String> map = yaml.loadAs(classpathFile, Map.class);
    CONFIG_MAP.putAll(map);
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
