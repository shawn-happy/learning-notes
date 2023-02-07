package com.shawn.study.deep.in.flink.api.transform;

import com.shawn.study.deep.in.flink.api.Event;
import com.shawn.study.deep.in.flink.api.source.CustomSourceFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class FlatMapFunctionDemo {

  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    DataStreamSource<Event> eventDataStreamSource = env.addSource(new CustomSourceFunction());
    eventDataStreamSource
        .flatMap(
            new FlatMapFunction<Event, String>() {
              @Override
              public void flatMap(Event value, Collector<String> out) throws Exception {

                if (value.getUser().equals("Mary")) {
                  out.collect(value.getUser());
                } else if (value.getUser().equals("Bob")) {
                  out.collect(value.getUser());
                  out.collect(value.getUrl());
                }
              }
            })
        .print();
    env.execute();
  }
}
