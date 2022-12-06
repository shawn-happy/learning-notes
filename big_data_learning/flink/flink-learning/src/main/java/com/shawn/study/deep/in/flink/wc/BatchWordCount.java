package com.shawn.study.deep.in.flink.wc;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

public class BatchWordCount {

  public static void main(String[] args) throws Exception {
    ExecutionEnvironment batchEnv = ExecutionEnvironment.getExecutionEnvironment();
    String filePath = "input/wc";
    DataSource<String> ds = batchEnv.readTextFile(filePath);
    ds.flatMap(
            (String value, Collector<Tuple2<String, Integer>> collector) -> {
              String[] words = value.split(" ");
              for (String word : words) {
                collector.collect(new Tuple2<>(word, 1));
              }
            }).returns(Types.TUPLE(Types.STRING, Types.INT))
        .groupBy(0)
        .sum(1)
        .print();
  }
}
