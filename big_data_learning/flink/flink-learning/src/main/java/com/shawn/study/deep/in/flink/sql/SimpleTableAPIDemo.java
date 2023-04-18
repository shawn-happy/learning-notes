package com.shawn.study.deep.in.flink.sql;

import static org.apache.flink.table.api.Expressions.$;

import com.shawn.study.deep.in.flink.api.Event;
import com.shawn.study.deep.in.flink.api.source.CustomSourceFunction;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

public class SimpleTableAPIDemo {

  public static void main(String[] args) throws Exception {
    // 获取流执行环境
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setParallelism(1);

    // 1. 读取数据源
    SingleOutputStreamOperator<Event> eventStream = env.addSource(new CustomSourceFunction());

    // 2. 获取表环境
    StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

    // 3. 将数据流转换成表
    Table eventTable = tableEnv.fromDataStream(eventStream);

    // 4. 用执行SQL 的方式提取数据
    Table resultTable1 = tableEnv.sqlQuery("select url, user from " + eventTable);

    // 5. 基于Table直接转换
    Table resultTable2 = eventTable.select($("user"), $("url")).where($("user").isEqual("Alice"));

    // 6. 将表转换成数据流，打印输出
    tableEnv.toDataStream(resultTable1).print("result1");
    tableEnv.toDataStream(resultTable2).print("result2");

    // 执行程序
    env.execute();
  }
}
