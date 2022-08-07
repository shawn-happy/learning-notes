package com.shawn.study.deep.in.java.configuration.source;

import com.fasterxml.jackson.core.type.TypeReference;
import com.shawn.study.deep.in.java.common.JsonUtil;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

public class JsonResourceConfigResource extends ResourceConfigSource {

  private static final String CONFIG_SOURCE_NAME = "application.json";
  private static final String DEFAULT_PATH = "META-INF/microprofile-config.json";
  private final String path;

  public JsonResourceConfigResource(String path, ClassLoader classLoader) {
    super(CONFIG_SOURCE_NAME, classLoader);
    this.path = path;
  }

  @Override
  protected void fillConfig(Map<String, String> config) {
    String path = getPath();
    try (InputStream inputStream = getClassLoader().getResourceAsStream(path)) {
      if (inputStream == null) {
        return;
      }
      String json = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
      Map<String, String> stringMap =
          JsonUtil.fromJson(json, new TypeReference<Map<String, String>>() {}).orElse(null);
      if (MapUtils.isEmpty(stringMap)) {
        return;
      }
      Set<String> keySet = stringMap.keySet();
      for (String key : keySet) {
        String value = stringMap.get(key);
        config.put(key, value);
      }
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public String getPath() {
    if (StringUtils.isBlank(path)) {
      return CONFIG_SOURCE_NAME;
    }
    return path;
  }
}
