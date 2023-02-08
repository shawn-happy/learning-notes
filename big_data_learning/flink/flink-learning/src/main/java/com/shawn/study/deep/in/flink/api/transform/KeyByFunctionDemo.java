package com.shawn.study.deep.in.flink.api.transform;

import com.shawn.study.deep.in.flink.api.Event;
import com.shawn.study.deep.in.flink.api.source.CustomSourceFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class KeyByFunctionDemo {

  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    DataStreamSource<Event> eventDataStreamSource = env.addSource(new CustomSourceFunction());
    // KeyedStream<当前流中的元素类型, key类型>
    KeyedStream<Event, String> eventStringKeyedStream = eventDataStreamSource.keyBy(Event::getUser);
    eventStringKeyedStream.print();
    env.execute();
  }
}
