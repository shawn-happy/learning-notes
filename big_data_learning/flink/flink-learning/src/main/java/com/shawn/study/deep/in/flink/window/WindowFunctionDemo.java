package com.shawn.study.deep.in.flink.window;

import com.shawn.study.deep.in.flink.api.Event;
import com.shawn.study.deep.in.flink.api.source.CustomSourceFunction;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

public class WindowFunctionDemo {

  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    OutputTag<Event> outputTag = new OutputTag<>("late") {};
    SingleOutputStreamOperator<Map<String, Object>> result =
        env.addSource(new CustomSourceFunction())
            .assignTimestampsAndWatermarks(
                WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ofSeconds(2))
                    .withTimestampAssigner(
                        new SerializableTimestampAssigner<Event>() {
                          @Override
                          public long extractTimestamp(Event element, long recordTimestamp) {
                            return element.getTimestamp();
                          }
                        }))
            .keyBy(Event::getUser)
            .window(TumblingEventTimeWindows.of(Time.seconds(10)))
            .allowedLateness(Time.minutes(1))
            .sideOutputLateData(outputTag)
            .aggregate(new CustomAggregationFunction(), new CustomProcessWindowFunction());
    result.print();
    result.getSideOutput(outputTag).print();
    env.execute();
  }

  // AggregateFunction<In, Acc, Out>
  public static class CustomAggregationFunction implements AggregateFunction<Event, Long, Long> {

    @Override
    public Long createAccumulator() {
      return 0L;
    }

    @Override
    public Long add(Event value, Long accumulator) {
      return accumulator + 1;
    }

    @Override
    public Long getResult(Long accumulator) {
      return accumulator;
    }

    @Override
    public Long merge(Long a, Long b) {
      return null;
    }
  }

  public static class CustomProcessWindowFunction
      extends ProcessWindowFunction<Long, Map<String, Object>, String, TimeWindow> {

    @Override
    public void process(
        String s, Context context, Iterable<Long> elements, Collector<Map<String, Object>> out)
        throws Exception {
      TimeWindow window = context.window();
      long start = window.getStart();
      long end = window.getEnd();
      Map<String, Object> result = new HashMap<>();
      result.put("window_start", start);
      result.put("window_end", end);
      Long next = elements.iterator().next();
      result.put("count", next);
      result.put("user", s);
      out.collect(result);
    }
  }
}
