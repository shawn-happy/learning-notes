package com.shawn.study.kafka.deserializer;

import com.shawn.study.kafka.domain.Customer;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

public class CustomerDeserializer implements Deserializer<Customer> {
  @Override
  public void configure(Map<String, ?> configs, boolean isKey) {
    //
  }

  @Override
  public Customer deserialize(String topic, byte[] data) {
    int id;
    int nameSize;
    String name;
    if (data == null) {
      return null;
    }
    if (data.length < 8) {
      throw new SerializationException("");
    }
    ByteBuffer buffer = ByteBuffer.wrap(data);
    id = buffer.getInt();
    nameSize = buffer.getInt();
    byte[] nameBytes = new byte[nameSize];
    buffer.get(nameBytes);
    name = new String(nameBytes, StandardCharsets.UTF_8);
    return new Customer(id, name);
  }

  @Override
  public void close() {}
}
