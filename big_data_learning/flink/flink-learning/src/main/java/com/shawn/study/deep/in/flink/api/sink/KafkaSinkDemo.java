package com.shawn.study.deep.in.flink.api.sink;

import com.shawn.study.deep.in.flink.api.Event;
import com.shawn.study.deep.in.flink.api.source.CustomSourceFunction;
import java.util.Properties;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

public class KafkaSinkDemo {

  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setParallelism(1);

    Properties properties = new Properties();
    properties.put("bootstrap.servers", "localhost:9092");
    DataStreamSource<Event> eventDataStreamSource = env.addSource(new CustomSourceFunction());
    eventDataStreamSource
        .map(Event::toString)
        .addSink(new FlinkKafkaProducer<String>("event", new SimpleStringSchema(), properties));

    env.execute();
  }
}
