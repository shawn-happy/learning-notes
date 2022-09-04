package com.shawn.study.kafka.deserializer;

import org.apache.kafka.common.serialization.Deserializer;

public class DeserializerDelegating<T> implements Deserializer<T> {

  private Deserializer<T> delegate;

  public DeserializerDelegating(Deserializer<T> delegate) {
    this.delegate = delegate;
  }

  @Override
  public T deserialize(String topic, byte[] data) {
    return delegate.deserialize(topic, data);
  }
}
