package com.shawn.study.kafka.produce;

import java.util.List;
import java.util.Map;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.record.InvalidRecordException;
import org.apache.kafka.common.utils.Utils;

public class KeyOrderPartitioner implements Partitioner {

  @Override
  public int partition(
      String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
    final List<PartitionInfo> partitionInfos = cluster.partitionsForTopic(topic);
    final int size = partitionInfos.size();
    if (keyBytes == null || !(key instanceof String)) {
      throw new InvalidRecordException("we expect all messages to have customer name as key");
    }
    if (((String) key).equals("Shawn2")) {
      return size;
    }
    return Math.abs(Utils.murmur2(keyBytes)) % (size - 1);
  }

  @Override
  public void close() {}

  @Override
  public void configure(Map<String, ?> configs) {}
}
