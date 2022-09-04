package com.shawn.study.kafka.produce;

import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class KafkaProduceSimpleDemo {

  public static void main(String[] args) throws Exception {
    Map<String, Object> map = new HashMap<>();
    map.put(BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
    map.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    map.put(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(map);
    for (int i = 0; i < 100; i++) {
      ProducerRecord<String, String> record =
          new ProducerRecord<>("simple_demo", RandomStringUtils.randomAlphabetic(6));
      // 异步发送消息
      // kafkaProducer.send(record);
      // 同步发送消息
      // kafkaProducer.send(record).get();
      // 异步发送消息，并执行回掉函数
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
      TimeUnit.SECONDS.sleep(1);
    }
    kafkaProducer.close();
  }
}
