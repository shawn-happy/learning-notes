package com.shawn.study.kafka.consumer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Deserializer;

public class KafkaConsumerFactory {

  private KafkaConsumerFactory() {}

  public static <K, V> KafkaConsumerBuilder<K, V> builder() {
    return new KafkaConsumerBuilder<>();
  }

  public static class KafkaConsumerBuilder<K, V> {
    private String groupId;
    private String clientId;
    private String bootstrapServers;
    private Deserializer<K> keyDeserializer;
    private Deserializer<V> valueDeserializer;
    private final Map<String, Object> advancedConfig = new HashMap<>();

    public KafkaConsumerBuilder() {}

    public KafkaConsumerBuilder<K, V> groupId(String groupId) {
      this.groupId = groupId;
      return this;
    }

    public KafkaConsumerBuilder<K, V> clientId(String clientId) {
      this.clientId = clientId;
      return this;
    }

    public KafkaConsumerBuilder<K, V> bootstrapServers(String bootstrapServers) {
      this.bootstrapServers = bootstrapServers;
      return this;
    }

    public KafkaConsumerBuilder<K, V> keyDeserializer(Deserializer<K> keyDeserializer) {
      this.keyDeserializer = keyDeserializer;
      return this;
    }

    public KafkaConsumerBuilder<K, V> valueDeserializer(Deserializer<V> valueDeserializer) {
      this.valueDeserializer = valueDeserializer;
      return this;
    }

    public KafkaConsumerBuilder<K, V> advancedConfig(Map<String, Object> advancedConfig) {
      if (advancedConfig != null) {
        this.advancedConfig.putAll(advancedConfig);
      }
      return this;
    }

    public KafkaConsumer<K, V> build() {
      assertNotEmpty(bootstrapServers, "bootstrap-servers should not be null");
      assertNotEmpty(groupId, "group.id should not be null");
      assertNotNull(keyDeserializer, "key deserializer should not be null");
      assertNotNull(valueDeserializer, "value deserializer should not be null");

      advancedConfig.putIfAbsent(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
      advancedConfig.putIfAbsent(ConsumerConfig.GROUP_ID_CONFIG, groupId);
      advancedConfig.putIfAbsent(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
      advancedConfig.putIfAbsent(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
      advancedConfig.putIfAbsent(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
      advancedConfig.remove(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG);
      advancedConfig.remove(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG);

      return new KafkaConsumer<>(advancedConfig, keyDeserializer, valueDeserializer);
    }

    private void assertNotEmpty(String str, String message) {
      if (StringUtils.isBlank(str)) {
        throw new IllegalArgumentException(message);
      }
    }

    private void assertNotNull(Object obj, String message) {
      if (Objects.isNull(obj)) {
        throw new IllegalArgumentException(message);
      }
    }
  }
}
