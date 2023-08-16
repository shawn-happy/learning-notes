package com.shawn.study.big.data.doris;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.apache.kafka.common.serialization.Serializer;

public class JsonSerializer<T> implements Serializer<T> {

  private static final JsonMapper mapper = JsonMapper.builder().findAndAddModules().build();

  @Override
  public byte[] serialize(String topic, T data) {
    try {
      return mapper.writeValueAsBytes(data);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return new byte[] {};
    }
  }
}
