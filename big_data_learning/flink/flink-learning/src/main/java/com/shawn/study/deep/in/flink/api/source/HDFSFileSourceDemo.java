package com.shawn.study.deep.in.flink.api.source;

import org.apache.flink.api.java.hadoop.mapred.HadoopInputFormat;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.hadoopcompatibility.HadoopInputs;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.TextInputFormat;

public class HDFSFileSourceDemo {

  public static void main(String[] args) throws Exception {
    String wc = "hdfs://localhost:9000/user/hadoop/input/JsonSeries.txt";
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    DataStreamSource<String> dataStreamSource = readTextFileFromHDFS(env, wc);
    dataStreamSource.print();
    env.execute();
  }

  private static DataStreamSource<String> readTextFileFromHDFS(
      StreamExecutionEnvironment env, String wc) {
    return env.readTextFile(wc);
  }

  private static void createInput(StreamExecutionEnvironment env, String wc) {
    HadoopInputFormat<LongWritable, Text> inputFormat =
        HadoopInputs.readHadoopFile(new TextInputFormat(), LongWritable.class, Text.class, wc);
    DataStreamSource<Tuple2<LongWritable, Text>> input = env.createInput(inputFormat);
    input.print();
  }
}
