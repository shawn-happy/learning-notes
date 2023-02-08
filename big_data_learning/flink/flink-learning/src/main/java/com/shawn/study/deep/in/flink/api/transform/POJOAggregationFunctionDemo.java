package com.shawn.study.deep.in.flink.api.transform;

import com.shawn.study.deep.in.flink.api.Event;
import com.shawn.study.deep.in.flink.api.source.CustomSourceFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class POJOAggregationFunctionDemo {

  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    DataStreamSource<Event> eventDataStreamSource = env.addSource(new CustomSourceFunction());
    // 如果POJO只能指定字段名
    eventDataStreamSource.keyBy(Event::getUser).max("timestamp").print();
    env.execute();
  }
}
