package com.shawn.study.kafka.consumer;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

public class KafkaConsumerRunner<K, V> implements Runnable {

  private final KafkaConsumer<K, V> consumer;
  private final ConsumerRebalanceListener rebalanceListener;
  private final List<String> topics;

  public KafkaConsumerRunner(KafkaConsumerConfig<K, V> kafkaConsumerConfig) {
    this.topics = kafkaConsumerConfig.getTopics();
    consumer =
        KafkaConsumerFactory.<K, V>builder()
            .groupId(kafkaConsumerConfig.getGroupId())
            .bootstrapServers(kafkaConsumerConfig.getBootstrapServers())
            .keyDeserializer(kafkaConsumerConfig.getKeyDeserializer())
            .valueDeserializer(kafkaConsumerConfig.getValueDeserializer())
            .build();
    rebalanceListener =
        new ConsumerRebalanceListener() {
          @Override
          public void onPartitionsRevoked(Collection<TopicPartition> partitions) {}

          @Override
          public void onPartitionsAssigned(Collection<TopicPartition> partitions) {}
        };
  }

  @Override
  public void run() {
    consumer.subscribe(topics, rebalanceListener);
    while (true) {
      ConsumerRecords<K, V> records = consumer.poll(Duration.ofMillis(100));
      int count = 0;
      for (ConsumerRecord<K, V> rec : records) {
        if (rec.value() == null) {
          continue; // when deserialization errors.
        }
        System.out.printf(
            "topic: %s, partition: %d, offset: %d, message: %s%n",
            rec.topic(), rec.partition(), rec.offset(), rec.value());
        count++;
      }
      if (count % 10 == 0) {
        commit();
      }
    }
  }

  public void shutdown() {
    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  consumer.wakeup();
                  String name = Thread.currentThread().getName();
                  System.out.println(name);
                  try {
                    Thread.currentThread().join();
                  } catch (Exception e) {
                    e.printStackTrace();
                  }
                }));
  }

  public void commit() {
    consumer.commitSync();
  }
}
