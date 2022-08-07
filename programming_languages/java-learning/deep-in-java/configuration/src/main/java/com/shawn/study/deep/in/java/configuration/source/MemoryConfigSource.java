package com.shawn.study.deep.in.java.configuration.source;

import static com.shawn.study.deep.in.java.configuration.source.ConfigSourceOrdinal.MEMORY_ORDINAL;

import java.util.Map;

public class MemoryConfigSource extends MapConfigSource {

  public static final String NAME = "java.memory.config";

  public MemoryConfigSource(Map<String, String> config) {
    super(NAME, MEMORY_ORDINAL.getOrdinal());
    prepareConfig(config);
  }
}
