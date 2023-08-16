package com.shawn.study.big.data.doris;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.apache.kafka.clients.admin.AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

@Component
public class DataFake implements ApplicationRunner {

  @Override
  public void run(ApplicationArguments args) throws Exception {
    Map<String, Object> map = new HashMap<>();
    map.put(BOOTSTRAP_SERVERS_CONFIG, "192.168.53.135:9092");
    map.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    map.put(VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
    KafkaProducer<String, User> kafkaProducer = new KafkaProducer<>(map);
    AtomicLong atomic = new AtomicLong(1);
    for (int i = 0; i < 10000; i++) {
      long id = atomic.incrementAndGet();
      ProducerRecord<String, User> record =
          new ProducerRecord<>(
              "user_doris",
              new User(
                  id,
                  "user" + id,
                  "user" + id,
                  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now())));
      kafkaProducer.send(
          record,
          (metadata, e) -> {
            if (e == null) {
              System.out.printf(
                  "topic: %s, partition %s, offset: %s\n",
                  metadata.topic(), metadata.partition(), metadata.offset());
            } else {
              e.printStackTrace();
            }
          });
      TimeUnit.SECONDS.sleep(3);
    }
    kafkaProducer.close();
  }
}
