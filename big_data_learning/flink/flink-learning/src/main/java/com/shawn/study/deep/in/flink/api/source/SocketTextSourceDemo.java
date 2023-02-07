package com.shawn.study.deep.in.flink.api.source;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class SocketTextSourceDemo {

  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    // macOS or linux exec `nc -lk 7777`
    DataStreamSource<String> dataStreamSource = env.socketTextStream("localhost", 7777);
    dataStreamSource.print();
    env.execute();
  }
}
