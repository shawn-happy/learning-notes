package com.shawn.study.kafka.consumer;

import java.util.Collections;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KafkaConsumerWeakUpDemo {

  public static void main(String[] args) throws Exception {
    KafkaConsumerConfig<String, String> config = new KafkaConsumerConfig<>();
    config.setBootstrapServers("127.0.0.1:9092");
    config.setGroupId("consumer_simple_demo");
    config.setKeyDeserializer(new StringDeserializer());
    config.setValueDeserializer(new StringDeserializer());
    config.setTopics(Collections.singletonList("simple_demo"));
    KafkaConsumerRunner<String, String> runner = new KafkaConsumerRunner<>(config);
    new Thread(runner).start();
    TimeUnit.SECONDS.sleep(10);
    Thread.currentThread().join();
    runner.shutdown();
  }
}
