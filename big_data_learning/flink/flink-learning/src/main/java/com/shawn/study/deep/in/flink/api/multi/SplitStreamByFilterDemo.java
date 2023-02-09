package com.shawn.study.deep.in.flink.api.multi;

import com.shawn.study.deep.in.flink.api.Event;
import com.shawn.study.deep.in.flink.api.source.CustomSourceFunction;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class SplitStreamByFilterDemo {

  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setParallelism(1);
    SingleOutputStreamOperator<Event> stream = env.addSource(new CustomSourceFunction());
    // 筛选Mary的浏览行为放入MaryStream流中
    DataStream<Event> MaryStream =
        stream.filter(
            new FilterFunction<Event>() {
              @Override
              public boolean filter(Event value) throws Exception {
                return value.getUser().equals("Mary");
              }
            });
    // 筛选Bob的购买行为放入BobStream流中
    DataStream<Event> BobStream =
        stream.filter(
            new FilterFunction<Event>() {
              @Override
              public boolean filter(Event value) throws Exception {
                return value.getUser().equals("Bob");
              }
            });
    // 筛选其他人的浏览行为放入elseStream流中
    DataStream<Event> elseStream =
        stream.filter(
            new FilterFunction<Event>() {
              @Override
              public boolean filter(Event value) throws Exception {
                return !value.getUser().equals("Mary") && !value.getUser().equals("Bob");
              }
            });

    MaryStream.print("Mary pv");
    BobStream.print("Bob pv");
    elseStream.print("else pv");

    env.execute();
  }
}
