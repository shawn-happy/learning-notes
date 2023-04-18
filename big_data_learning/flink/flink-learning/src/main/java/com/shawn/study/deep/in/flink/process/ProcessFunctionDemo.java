package com.shawn.study.deep.in.flink.process;

import com.shawn.study.deep.in.flink.api.Event;
import com.shawn.study.deep.in.flink.api.source.CustomSourceFunction;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

public class ProcessFunctionDemo {

  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setParallelism(1);

    env.addSource(new CustomSourceFunction())
        .assignTimestampsAndWatermarks(
            WatermarkStrategy.<Event>forMonotonousTimestamps()
                .withTimestampAssigner(
                    new SerializableTimestampAssigner<Event>() {
                      @Override
                      public long extractTimestamp(Event event, long l) {
                        return event.getTimestamp();
                      }
                    }))
        .process(
            new ProcessFunction<Event, String>() {
              @Override
              public void processElement(Event value, Context ctx, Collector<String> out)
                  throws Exception {
                if (value.getUser().equals("Mary")) {
                  out.collect(value.getUser());
                } else if (value.getUser().equals("Bob")) {
                  out.collect(value.getUser());
                  out.collect(value.getUser());
                }
                System.out.println(ctx.timerService().currentWatermark());
              }
            })
        .print();

    env.execute();
  }
}
