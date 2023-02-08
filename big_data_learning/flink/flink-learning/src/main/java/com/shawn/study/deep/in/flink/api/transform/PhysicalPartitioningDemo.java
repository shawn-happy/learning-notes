package com.shawn.study.deep.in.flink.api.transform;

import com.shawn.study.deep.in.flink.api.Event;
import com.shawn.study.deep.in.flink.api.source.CustomSourceFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class PhysicalPartitioningDemo {

  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setParallelism(1);
    DataStreamSource<Event> eventDataStreamSource = env.addSource(new CustomSourceFunction());
    eventDataStreamSource.shuffle().print("shuffle").setParallelism(4);
    eventDataStreamSource.rebalance().print("round-robin").setParallelism(4);
    eventDataStreamSource.broadcast().print("broadcast").setParallelism(4);
    eventDataStreamSource.global().print("global").setParallelism(4);
    env.execute();
  }
}
