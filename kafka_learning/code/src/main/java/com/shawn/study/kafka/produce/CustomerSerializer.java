package com.shawn.study.kafka.produce;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import org.apache.kafka.common.serialization.Serializer;

public class CustomerSerializer implements Serializer<Customer> {

  @Override
  public byte[] serialize(String topic, Customer data) {
    if (data == null) {
      return null;
    }
    byte[] nameBytes;
    int nameBytesSize;
    if (data.getName() != null && data.getName().length() != 0) {
      nameBytes = data.getName().getBytes(StandardCharsets.UTF_8);
      nameBytesSize = nameBytes.length;
    } else {
      nameBytes = new byte[0];
      nameBytesSize = 0;
    }
    ByteBuffer byteBuffer = ByteBuffer.allocate(4 + 4 + nameBytesSize);
    byteBuffer.putInt(data.getId());
    byteBuffer.putInt(nameBytesSize);
    byteBuffer.put(nameBytes);
    return byteBuffer.array();
  }
}
