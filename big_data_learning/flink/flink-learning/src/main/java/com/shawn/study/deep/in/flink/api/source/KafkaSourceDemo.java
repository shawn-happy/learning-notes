package com.shawn.study.deep.in.flink.api.source;

import java.util.Properties;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

public class KafkaSourceDemo {

  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    DataStreamSource<String> dataStreamSource = fromSource(env);
    dataStreamSource.print();
    env.execute();
  }

  private static DataStreamSource<String> readFromSourceFunction(StreamExecutionEnvironment env) {
    Properties properties = new Properties();
    properties.setProperty("bootstrap.servers", "localhost:9092");
    properties.setProperty("group.id", "consumer-group");
    properties.setProperty(
        "key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    properties.setProperty(
        "value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    FlinkKafkaConsumer<String> flinkKafkaConsumer =
        new FlinkKafkaConsumer<>("event", new SimpleStringSchema(), properties);
    return env.addSource(flinkKafkaConsumer);
  }

  private static DataStreamSource<String> fromSource(StreamExecutionEnvironment env) {
    String bootstrapServers = "localhost:9092";
    KafkaSource<String> kafkaSource =
        KafkaSource.<String>builder()
            .setBootstrapServers(bootstrapServers)
            .setTopics("event")
            .setGroupId("consumer-group")
            .setValueOnlyDeserializer(new SimpleStringSchema())
            .setStartingOffsets(OffsetsInitializer.earliest())
            .build();
    return env.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "kafka-source");
  }
}
