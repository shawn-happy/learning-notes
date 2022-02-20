package com.shawn.study.kafka.consumer;

import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

public class IndependentConsumerDemo {

  public static void main(String[] args) {
    Map<String, Object> map = new HashMap<>();
    map.put(BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
    map.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    map.put(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    map.put(GROUP_ID_CONFIG, "consumer_simple_demo");
    KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(map);
    final List<PartitionInfo> partitionInfos = kafkaConsumer.partitionsFor("simple_demo");
    List<TopicPartition> partitions =
        partitionInfos.stream()
            .map(
                partitionInfo ->
                    new TopicPartition(partitionInfo.topic(), partitionInfo.partition()))
            .collect(Collectors.toList());
    kafkaConsumer.assign(partitions);
    // kafkaConsumer.subscribe(Collections.singletonList("simple_demo"));
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
