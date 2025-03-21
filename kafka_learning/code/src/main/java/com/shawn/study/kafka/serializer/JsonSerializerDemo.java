package com.shawn.study.kafka.serializer;

import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

import com.shawn.study.kafka.domain.Customer;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class JsonSerializerDemo {

  public static void main(String[] args) {
    Map<String, Object> map = new HashMap<>();
    map.put(BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
    map.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    map.put(VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
    KafkaProducer<String, Customer> kafkaProducer = new KafkaProducer<>(map);
    for (int i = 0; i < 5; i++) {
      final Customer customer = new Customer();
      customer.setId(i + 1);
      customer.setName("shawn_" + (i + 1));
      ProducerRecord<String, Customer> record = new ProducerRecord<>("user", customer);
      kafkaProducer.send(
          record,
          (metadata, e) -> {
            if (e == null) {
              System.out.printf(
                  "topic: %s, partition %s, offset: %s\n",
                  metadata.topic(), metadata.partition(), metadata.topic());
            } else {
              e.printStackTrace();
            }
          });
    }
    kafkaProducer.close();
  }
}
