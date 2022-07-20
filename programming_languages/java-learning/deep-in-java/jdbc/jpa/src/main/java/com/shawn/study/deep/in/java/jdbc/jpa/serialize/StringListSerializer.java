package com.shawn.study.deep.in.java.jdbc.jpa.serialize;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringListSerializer implements PersistenceConverter<List<String>> {

  @Override
  public List<String> deserialize(byte[] bytes) throws IOException {
    if (null == bytes || bytes.length == 0) {
      return Collections.emptyList();
    }
    String s = new String(bytes, StandardCharsets.UTF_8);
    String[] split = s.split(",");
    return Arrays.asList(split);
  }

  @Override
  public byte[] serialize(List<String> object) throws IOException {
    if (object == null || object.isEmpty()) {
      return null;
    }
    String collect = String.join(",", object);
    return collect.getBytes(StandardCharsets.UTF_8);
  }
}
