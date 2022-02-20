package com.shawn.study.kafka.consumer;

import static org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KafkaConsumerCommitDemo {

  public static void main(String[] args) {
    Map<String, Object> map = new HashMap<>();
    map.put(BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
    map.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    map.put(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    map.put(GROUP_ID_CONFIG, "consumer_simple_demo");
    map.put(ENABLE_AUTO_COMMIT_CONFIG, false);
    KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(map);
    kafkaConsumer.subscribe(Collections.singletonList("simple_demo"));
    try {
      commitSync(kafkaConsumer);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      kafkaConsumer.close();
    }
  }

  private static void commitSync(KafkaConsumer<String, String> kafkaConsumer) {
    long now = System.currentTimeMillis();
    long i = 0;
    while (i < now) {
      ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofMillis(100));
      for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
        System.out.printf(
            "topic: %s, partition: %d, offset: %d, message: %s%n",
            consumerRecord.topic(),
            consumerRecord.partition(),
            consumerRecord.offset(),
            consumerRecord.value());
      }
      try {
        kafkaConsumer.commitSync(Duration.ofMinutes(1));
      } catch (CommitFailedException e) {
        e.printStackTrace();
      }
      i++;
    }
  }

  private static void commitASync(KafkaConsumer<String, String> kafkaConsumer) {
    long now = System.currentTimeMillis();
    long i = 0;
    while (i < now) {
      ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofMillis(100));
      for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
        System.out.printf(
            "topic: %s, partition: %d, offset: %d, message: %s%n",
            consumerRecord.topic(),
            consumerRecord.partition(),
            consumerRecord.offset(),
            consumerRecord.value());
      }
      kafkaConsumer.commitAsync();
      i++;
    }
  }

  private static void commitASyncWithCallback(KafkaConsumer<String, String> kafkaConsumer) {
    long now = System.currentTimeMillis();
    long i = 0;
    while (i < now) {
      ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofMillis(100));
      for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
        System.out.printf(
            "topic: %s, partition: %d, offset: %d, message: %s%n",
            consumerRecord.topic(),
            consumerRecord.partition(),
            consumerRecord.offset(),
            consumerRecord.value());
      }
      kafkaConsumer.commitAsync(
          (offsets, e) -> {
            if (e != null) {
              Set<TopicPartition> topicPartitions = offsets.keySet();
              topicPartitions.forEach(
                  (topic) -> {
                    OffsetAndMetadata offsetAndMetadata = offsets.get(topic);
                    System.out.printf(
                        "topic: %s, partition: %d, offset: %s\n",
                        topic.topic(), topic.partition(), offsetAndMetadata.offset());
                  });
            }
          });
      i++;
    }
  }

  private static void commitSyncAndAsync(KafkaConsumer<String, String> kafkaConsumer) {
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
        kafkaConsumer.commitAsync();
        i++;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        kafkaConsumer.commitSync();
      } finally {
        kafkaConsumer.close();
      }
    }
  }

  private static void commitByOffset(KafkaConsumer<String, String> kafkaConsumer) {
    long now = System.currentTimeMillis();
    long i = 0;
    long count = 0;
    Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();
    while (i < now) {

      ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofMillis(100));
      for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
        System.out.printf(
            "topic: %s, partition: %d, offset: %d, message: %s%n",
            consumerRecord.topic(),
            consumerRecord.partition(),
            consumerRecord.offset(),
            consumerRecord.value());
        kafkaConsumer.commitAsync();
        final TopicPartition topicPartition =
            new TopicPartition(consumerRecord.topic(), consumerRecord.partition());
        final OffsetAndMetadata offsetAndMetadata =
            new OffsetAndMetadata(consumerRecord.offset() + 1, "no metadata");
        offsets.put(topicPartition, offsetAndMetadata);
        if (count % 1000 == 0) {
          kafkaConsumer.commitSync(offsets);
        }
        count++;
      }

      i++;
    }
  }
}
