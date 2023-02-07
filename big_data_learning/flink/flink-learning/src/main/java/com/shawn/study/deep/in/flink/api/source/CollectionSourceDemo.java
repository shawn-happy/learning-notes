package com.shawn.study.deep.in.flink.api.source;

import com.shawn.study.deep.in.flink.api.Event;
import java.util.List;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class CollectionSourceDemo {

  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    RandomDataGenerator randomDataGenerator = new RandomDataGenerator();
    DataStreamSource<Event> dataStreamSource =
        env.fromCollection(
            List.of(
                new Event(
                    "shawn",
                    "/home",
                    randomDataGenerator.nextLong(
                        System.currentTimeMillis() - 1,
                        System.currentTimeMillis() + System.currentTimeMillis())),
                new Event(
                    "jack",
                    "/mnt",
                    randomDataGenerator.nextLong(
                        System.currentTimeMillis() - 1,
                        System.currentTimeMillis() + System.currentTimeMillis()))));
    dataStreamSource.print();
    env.execute();
  }
}
