package com.shawn.study.kafka;

import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.ISOLATION_LEVEL_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.requests.IsolationLevel;
import org.apache.kafka.common.serialization.StringSerializer;

public class ExactlyOnceDemo {

  public static void main(String[] args) {}

  private static void idempotence() {
    Map<String, Object> map = new HashMap<>();
    map.put(BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
    map.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    map.put(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    map.put(ENABLE_IDEMPOTENCE_CONFIG, true);
    KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(map);
    for (int i = 0; i < 5; i++) {
      ProducerRecord<String, String> record = new ProducerRecord<>("simple_demo", "shawn" + i);
      kafkaProducer.send(
          record,
          (metadata, e) -> {
            if (e == null) {
              System.out.println(
                  "topic: " + metadata.topic() + " partition: " + metadata.partition());
            }
          });
    }
    kafkaProducer.close();
  }

  private static void transactionProducer() {
    Map<String, Object> map = new HashMap<>();
    map.put(BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
    map.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    map.put(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    map.put(ENABLE_IDEMPOTENCE_CONFIG, true);
    KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(map);
    kafkaProducer.initTransactions();
    try {
      kafkaProducer.beginTransaction();
      for (int i = 0; i < 5; i++) {
        ProducerRecord<String, String> record = new ProducerRecord<>("simple_demo", "shawn" + i);
        kafkaProducer.send(
            record,
            (metadata, e) -> {
              if (e == null) {
                System.out.println(
                    "topic: " + metadata.topic() + " partition: " + metadata.partition());
              }
            });
      }
      kafkaProducer.commitTransaction();
    } catch (Exception e) {
      kafkaProducer.abortTransaction();
      e.printStackTrace();
    } finally {
      kafkaProducer.close();
    }
  }

  private static void transactionConsumer() {
    Map<String, Object> map = new HashMap<>();
    map.put(BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
    map.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    map.put(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    map.put(GROUP_ID_CONFIG, "consumer_simple_demo");
    map.put(ISOLATION_LEVEL_CONFIG, IsolationLevel.READ_COMMITTED.name());
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
