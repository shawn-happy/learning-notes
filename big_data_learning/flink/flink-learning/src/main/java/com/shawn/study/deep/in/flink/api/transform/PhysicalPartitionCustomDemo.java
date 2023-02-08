package com.shawn.study.deep.in.flink.api.transform;

import com.shawn.study.deep.in.flink.api.Event;
import com.shawn.study.deep.in.flink.api.source.CustomSourceFunction;
import org.apache.flink.api.common.functions.Partitioner;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class PhysicalPartitionCustomDemo {

  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setParallelism(1);
    DataStreamSource<Event> eventDataStreamSource = env.addSource(new CustomSourceFunction());
    eventDataStreamSource
        .partitionCustom(
            new Partitioner<String>() {
              @Override
              public int partition(String key, int numPartitions) {
                return key.hashCode() % 2;
              }
            },
            new KeySelector<Event, String>() {
              @Override
              public String getKey(Event event) throws Exception {
                return event.getUser();
              }
            })
        .print("custom")
        .setParallelism(2);
    env.execute();
  }
}
