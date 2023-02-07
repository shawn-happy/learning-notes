package com.shawn.study.deep.in.flink.api.source;

import com.shawn.study.deep.in.flink.api.Event;
import com.shawn.study.deep.in.flink.api.source.CustomSource.CustomCheckPoint;
import com.shawn.study.deep.in.flink.api.source.CustomSource.CustomSourceSplit;
import org.apache.flink.api.connector.source.Boundedness;
import org.apache.flink.api.connector.source.Source;
import org.apache.flink.api.connector.source.SourceReader;
import org.apache.flink.api.connector.source.SourceReaderContext;
import org.apache.flink.api.connector.source.SourceSplit;
import org.apache.flink.api.connector.source.SplitEnumerator;
import org.apache.flink.api.connector.source.SplitEnumeratorContext;
import org.apache.flink.core.io.SimpleVersionedSerializer;

public class CustomSource implements Source<Event, CustomSourceSplit, CustomCheckPoint> {

  @Override
  public Boundedness getBoundedness() {
    return Boundedness.CONTINUOUS_UNBOUNDED;
  }

  @Override
  public SourceReader<Event, CustomSourceSplit> createReader(SourceReaderContext readerContext)
      throws Exception {
    return null;
  }

  @Override
  public SplitEnumerator<CustomSourceSplit, CustomCheckPoint> createEnumerator(
      SplitEnumeratorContext<CustomSourceSplit> enumContext) throws Exception {
    return null;
  }

  @Override
  public SimpleVersionedSerializer<CustomSourceSplit> getSplitSerializer() {
    return null;
  }

  @Override
  public SimpleVersionedSerializer<CustomCheckPoint> getEnumeratorCheckpointSerializer() {
    return null;
  }

  @Override
  public SplitEnumerator<CustomSourceSplit, CustomCheckPoint> restoreEnumerator(
      SplitEnumeratorContext<CustomSourceSplit> enumContext, CustomCheckPoint checkpoint)
      throws Exception {
    return null;
  }

  protected static class CustomSourceSplit implements SourceSplit {

    private final Event event;

    private CustomSourceSplit(Event event) {
      this.event = event;
    }

    @Override
    public String splitId() {
      return event.toString();
    }
  }

  protected static class CustomCheckPoint {

    private Event event;

    private CustomCheckPoint(Event event) {
      this.event = event;
    }

    public String getCheckpoint() {
      return event.getUser() + ", " + event.getUrl() + ", " + event.getTimestamp();
    }
  }
}
