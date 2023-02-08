package com.shawn.study.deep.in.flink.api.transform;

import java.util.List;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class TupleAggregationFunctionDemo {

  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    DataStreamSource<Tuple2<String, Integer>> dataStreamSource =
        env.fromCollection(
            List.of(
                Tuple2.of("a", 1),
                Tuple2.of("a", 3),
                Tuple2.of("b", 3),
                Tuple2.of("b", 5),
                Tuple2.of("c", 8),
                Tuple2.of("d", 7)));

    dataStreamSource.keyBy(t -> t.f0).sum(1).print();
    dataStreamSource.keyBy(t -> t.f0).sum("f1").print();

    dataStreamSource.keyBy(t -> t.f0).min(1).print();
    dataStreamSource.keyBy(t -> t.f0).min("f1").print();
    dataStreamSource.keyBy(t -> t.f0).minBy(1).print();
    dataStreamSource.keyBy(t -> t.f0).minBy("f1").print();

    dataStreamSource.keyBy(t -> t.f0).max(1).print();
    dataStreamSource.keyBy(t -> t.f0).max("f1").print();
    dataStreamSource.keyBy(t -> t.f0).maxBy(1).print();
    dataStreamSource.keyBy(t -> t.f0).maxBy("f1").print();

    env.execute();
  }
}
