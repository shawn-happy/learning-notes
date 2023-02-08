package com.shawn.study.deep.in.flink.api.transform;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;

public class PhysicalPartitioningRescaleDemo {

  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.addSource(
            new RichParallelSourceFunction<Integer>() { // 这里使用了并行数据源的富函数版本
              @Override
              public void run(SourceContext<Integer> sourceContext) throws Exception {
                for (int i = 1; i <= 8; i++) {
                  // 将奇数发送到索引为1的并行子任务
                  // 将偶数发送到索引为0的并行子任务
                  if (i % 2 == getRuntimeContext().getIndexOfThisSubtask()) {
                    sourceContext.collect(i);
                  }
                }
              }

              @Override
              public void cancel() {}
            })
        .setParallelism(2)
        .rescale()
        .print()
        .setParallelism(4);
    env.execute();
  }
}
