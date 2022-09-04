package com.shawn.study.kafka.consumer;

import java.util.List;
import org.apache.kafka.common.serialization.Deserializer;

public class KafkaConsumerConfig<K, V> {

  private List<String> topics;
  private String groupId;
  private String bootstrapServers;
  private Deserializer<K> keyDeserializer;
  private Deserializer<V> valueDeserializer;

  public List<String> getTopics() {
    return topics;
  }

  public void setTopics(List<String> topics) {
    this.topics = topics;
  }

  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public String getBootstrapServers() {
    return bootstrapServers;
  }

  public void setBootstrapServers(String bootstrapServers) {
    this.bootstrapServers = bootstrapServers;
  }

  public Deserializer<K> getKeyDeserializer() {
    return keyDeserializer;
  }

  public void setKeyDeserializer(Deserializer<K> keyDeserializer) {
    this.keyDeserializer = keyDeserializer;
  }

  public Deserializer<V> getValueDeserializer() {
    return valueDeserializer;
  }

  public void setValueDeserializer(Deserializer<V> valueDeserializer) {
    this.valueDeserializer = valueDeserializer;
  }
}
