package com.shawn.study.deep.in.flink.api.sink;

import com.shawn.study.deep.in.flink.api.Event;
import com.shawn.study.deep.in.flink.api.source.CustomSourceFunction;
import java.util.concurrent.TimeUnit;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.configuration.MemorySize;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy;

public class FileSinkDemo {

  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setParallelism(1);
    DataStreamSource<Event> eventDataStreamSource = env.addSource(new CustomSourceFunction());
    StreamingFileSink<String> fileSink =
        StreamingFileSink.<String>forRowFormat(
                new Path("./output"), new SimpleStringEncoder<>("UTF-8"))
            .withRollingPolicy(
                DefaultRollingPolicy.builder()
                    .withRolloverInterval(TimeUnit.MINUTES.toMillis(15))
                    .withInactivityInterval(TimeUnit.MINUTES.toMillis(5))
                    .withMaxPartSize(new MemorySize(1024 * 1024 * 1024))
                    .build())
            .build();
    eventDataStreamSource.map(Event::toString).addSink(fileSink);
    env.execute();
  }
}
