package com.shawn.study.kafka.consumer;

import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KafkaConsumerSimpleDemo {

  public static void main(String[] args) {
    Map<String, Object> map = new HashMap<>();
    map.put(BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
    map.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    map.put(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    map.put(GROUP_ID_CONFIG, "consumer_simple_demo");
    KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(map);
    kafkaConsumer.subscribe(Collections.singletonList("simple_demo"));
    long now = System.currentTimeMillis();
    long i = 0;
    try {
      while (i < now) {
        ConsumerRecords<String, String> consumerRecords =
            kafkaConsumer.poll(Duration.ofMillis(100));
        for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
          System.out.printf(
              "topic: %s, partition: %d, offset: %d, message: %s%n",
              consumerRecord.topic(),
              consumerRecord.partition(),
              consumerRecord.offset(),
              consumerRecord.value());
        }
        i++;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      kafkaConsumer.close();
    }
  }
}
