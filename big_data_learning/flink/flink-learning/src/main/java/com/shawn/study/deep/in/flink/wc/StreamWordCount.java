package com.shawn.study.deep.in.flink.wc;

import java.util.List;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class StreamWordCount {

  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
    streamEnv
        .fromCollection(List.of("hello java", "hello flink", "flink learning", "deep in flink"))
        .flatMap(
            (String value, Collector<Tuple2<String, Integer>> collector) -> {
              String[] words = value.split(" ");
              for (String word : words) {
                collector.collect(new Tuple2<>(word, 1));
              }
            })
        .returns(Types.TUPLE(Types.STRING, Types.INT))
        .keyBy(0)
        .sum(1)
        .print();
    streamEnv.execute("stream demo");
  }
}
