package com.shawn.study.kafka.produce;

import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.PARTITIONER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class KeyOrderingPartitionerDemo {
  public static void main(String[] args) {
    Map<String, Object> map = new HashMap<>();
    map.put(BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9097");
    map.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    map.put(VALUE_SERIALIZER_CLASS_CONFIG, CustomerSerializer.class.getName());
    map.put(PARTITIONER_CLASS_CONFIG, KeyOrderPartitioner.class.getName());
    KafkaProducer<String, Customer> kafkaProducer = new KafkaProducer<>(map);
    for (int i = 0; i < 5; i++) {
      final Customer customer = new Customer();
      customer.setId(i + 1);
      customer.setName("shawn_" + (i + 1));
      ProducerRecord<String, Customer> record =
          new ProducerRecord<>("user", customer.getName(), customer);
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
}
