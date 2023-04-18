package com.shawn.study.deep.in.flink.window;

import com.shawn.study.deep.in.flink.api.Event;
import com.shawn.study.deep.in.flink.api.source.CustomSourceFunction;
import java.time.Duration;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class WatermarkDemo {

  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setParallelism(1);

    // 将数据源改为socket文本流，并转换成Event类型
    env.addSource(new CustomSourceFunction())
        // 插入水位线的逻辑
        .assignTimestampsAndWatermarks(
            // 针对乱序流插入水位线，延迟时间设置为5s
            WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ofSeconds(5))
                .withTimestampAssigner(
                    new SerializableTimestampAssigner<Event>() {
                      // 抽取时间戳的逻辑
                      @Override
                      public long extractTimestamp(Event element, long recordTimestamp) {
                        return element.getTimestamp();
                      }
                    }))
        // 根据user分组，开窗统计
        .keyBy(Event::getUser)
        .window(TumblingEventTimeWindows.of(Time.seconds(10)))
        .process(new WatermarkTestResult())
        .print();

    env.execute();
  }

  // 自定义处理窗口函数，输出当前的水位线和窗口信息
  public static class WatermarkTestResult
      extends ProcessWindowFunction<Event, String, String, TimeWindow> {
    @Override
    public void process(String s, Context context, Iterable<Event> elements, Collector<String> out)
        throws Exception {
      Long start = context.window().getStart();
      Long end = context.window().getEnd();
      Long currentWatermark = context.currentWatermark();
      Long count = elements.spliterator().getExactSizeIfKnown();
      out.collect(
          "窗口" + start + " ~ " + end + "中共有" + count + "个元素，窗口闭合计算时，水位线处于：" + currentWatermark);
    }
  }
}
