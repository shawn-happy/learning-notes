package com.shawn.study.deep.in.flink.api.transform;

import com.shawn.study.deep.in.flink.api.Event;
import com.shawn.study.deep.in.flink.api.source.CustomSourceFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class RichFunctionDemo {

  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    DataStreamSource<Event> eventDataStreamSource = env.addSource(new CustomSourceFunction());
    eventDataStreamSource
        .map(
            new RichMapFunction<Event, String>() {

              @Override
              public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                System.out.println(
                    "index: " + getRuntimeContext().getIndexOfThisSubtask() + " task begin");
              }

              @Override
              public String map(Event value) throws Exception {
                return value.toString();
              }

              @Override
              public void close() throws Exception {
                super.close();
                System.out.println(
                    "index: " + getRuntimeContext().getIndexOfThisSubtask() + " task close");
              }
            })
        .print();
    env.execute();
  }
}
