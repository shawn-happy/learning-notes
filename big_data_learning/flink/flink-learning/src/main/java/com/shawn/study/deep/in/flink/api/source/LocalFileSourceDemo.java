package com.shawn.study.deep.in.flink.api.source;

import com.google.common.io.Resources;
import com.shawn.study.deep.in.flink.api.strategy.CustomWatermarkStrategy;
import java.time.Duration;
import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.connector.file.src.reader.TextLineInputFormat;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class LocalFileSourceDemo {

  public static void main(String[] args) throws Exception {
    String wc = Resources.getResource("wc").getPath();
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    DataStreamSource<String> dataStreamSource = fromFileSource(env, wc);
    dataStreamSource.print();
    env.execute();
  }

  private static DataStreamSource<String> readTextFile(StreamExecutionEnvironment env, String wc) {
    return env.readTextFile(wc, "UTF-8");
  }

  private static DataStreamSource<String> fromFileSource(
      StreamExecutionEnvironment env, String wc) {
    FileSource<String> source =
        FileSource.forRecordStreamFormat(new TextLineInputFormat("UTF-8"), new Path(wc)).build();
    return env.fromSource(source, new CustomWatermarkStrategy(Duration.ZERO), "wc-source");
  }
}
