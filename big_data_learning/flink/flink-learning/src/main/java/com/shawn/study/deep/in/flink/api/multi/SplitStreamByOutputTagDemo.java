package com.shawn.study.deep.in.flink.api.multi;

import com.shawn.study.deep.in.flink.api.Event;
import com.shawn.study.deep.in.flink.api.source.CustomSourceFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

public class SplitStreamByOutputTagDemo {

  private static final OutputTag<Tuple3<String, String, Long>> MaryTag =
      new OutputTag<>("Mary-pv") {};
  private static final OutputTag<Tuple3<String, String, Long>> BobTag =
      new OutputTag<>("Bob-pv") {};

  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setParallelism(1);
    SingleOutputStreamOperator<Event> stream = env.addSource(new CustomSourceFunction());

    SingleOutputStreamOperator<Event> processedStream =
        stream.process(
            new ProcessFunction<Event, Event>() {
              @Override
              public void processElement(Event value, Context ctx, Collector<Event> out)
                  throws Exception {
                if (value.getUser().equals("Mary")) {
                  ctx.output(
                      MaryTag, new Tuple3<>(value.getUser(), value.getUrl(), value.getTimestamp()));
                } else if (value.getUser().equals("Bob")) {
                  ctx.output(
                      BobTag, new Tuple3<>(value.getUser(), value.getUrl(), value.getTimestamp()));
                } else {
                  out.collect(value);
                }
              }
            });

    processedStream.getSideOutput(MaryTag).print("Mary pv");
    processedStream.getSideOutput(BobTag).print("Bob pv");
    processedStream.print("else");

    env.execute();
  }
}
