package com.shawn.study.deep.in.flink.process;

import com.shawn.study.deep.in.flink.api.Event;
import com.shawn.study.deep.in.flink.api.source.CustomSourceFunction;
import java.sql.Timestamp;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

public class ProcessingTimeTimerDemo {

  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setParallelism(1);

    // 处理时间语义，不需要分配时间戳和watermark
    SingleOutputStreamOperator<Event> stream = env.addSource(new CustomSourceFunction());

    // 要用定时器，必须基于KeyedStream
    stream
        .keyBy(data -> true)
        .process(
            new KeyedProcessFunction<Boolean, Event, String>() {
              @Override
              public void processElement(Event value, Context ctx, Collector<String> out)
                  throws Exception {
                Long currTs = ctx.timerService().currentProcessingTime();
                out.collect("数据到达，到达时间：" + new Timestamp(currTs));
                // 注册一个10秒后的定时器
                ctx.timerService().registerProcessingTimeTimer(currTs + 10 * 1000L);
              }

              @Override
              public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out)
                  throws Exception {
                out.collect("定时器触发，触发时间：" + new Timestamp(timestamp));
              }
            })
        .print();

    env.execute();
  }
}
