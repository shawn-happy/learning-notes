package com.shawn.study.deep.in.flink.api.transform;

import com.shawn.study.deep.in.flink.api.Event;
import com.shawn.study.deep.in.flink.api.source.CustomSourceFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class ReduceFunctionDemo {

  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    DataStreamSource<Event> eventDataStreamSource = env.addSource(new CustomSourceFunction());
    eventDataStreamSource
        .map(e -> Tuple2.of(e.getUser(), 1L))
        .returns(Types.TUPLE(Types.STRING, Types.LONG))
        .keyBy(r -> r.f0) // 使用用户名来进行分流
        .reduce(
            (value1, value2) -> {
              // 每到一条数据，用户pv的统计值加1
              return Tuple2.of(value1.f0, value1.f1 + value2.f1);
            })
        .returns(Types.TUPLE(Types.STRING, Types.LONG))
        .keyBy(r -> true) // 为每一条数据分配同一个key，将聚合结果发送到一条流中去
        .reduce(
            (value1, value2) -> {
              // 将累加器更新为当前最大的pv统计值，然后向下游发送累加器的值
              return value1.f1 > value2.f1 ? value1 : value2;
            })
        .returns(Types.TUPLE(Types.STRING, Types.LONG))
        .print();

    env.execute();
  }
}
