package com.shawn.study.kafka.admin;

import static org.apache.kafka.clients.admin.AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.admin.AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.TopicPartitionInfo;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class KafkaAdminClientDemo {

  private static final Map<String, Object> CONF = new HashMap<>();
  private static final String TEST_TOPIC = "kafka_demo";
  private static final int DEFAULT_PARTITION_NUM = 1;
  private static final short DEFAULT_REPLICATION_NUM = 1;

  private static AdminClient adminClient;

  @BeforeClass
  public static void createAdminClient() {
    try {
      CONF.put(BOOTSTRAP_SERVERS_CONFIG, "172.27.128.113:9097");
      CONF.put(REQUEST_TIMEOUT_MS_CONFIG, 120000);
      CONF.put("zookeeper.connect", "172.27.128.113:21891");
      adminClient = AdminClient.create(CONF);
    } catch (Exception e) {
      throw new RuntimeException("create kafka admin client error", e);
    }
  }

  @AfterClass
  public static void stop() {
    if (adminClient != null) {
      adminClient.close();
    }
  }

  @Test
  public void testCreateTopic() {
    boolean match = topicExists(adminClient, TEST_TOPIC);
    if (!match) {
      NewTopic topic = new NewTopic(TEST_TOPIC, DEFAULT_PARTITION_NUM, DEFAULT_REPLICATION_NUM);
      CreateTopicsResult result = adminClient.createTopics(Collections.singleton(topic));
      assertNotNull(result);
      assertTrue(topicExists(adminClient, TEST_TOPIC));
    }
  }

  @Test
  public void testDeleteTopic() {
    boolean match = topicExists(adminClient, TEST_TOPIC);
    if (match) {
      DeleteTopicsResult deleteTopicsResult =
          adminClient.deleteTopics(Collections.singleton(TEST_TOPIC));
      assertNotNull(deleteTopicsResult);
      assertFalse(topicExists(adminClient, TEST_TOPIC));
    }
  }

  @Test
  public void testDescribeTopic() throws ExecutionException, InterruptedException {
    boolean match = topicExists(adminClient, TEST_TOPIC);
    if (!match) {
      NewTopic topic = new NewTopic(TEST_TOPIC, DEFAULT_PARTITION_NUM, DEFAULT_REPLICATION_NUM);
      CreateTopicsResult result = adminClient.createTopics(Collections.singleton(topic));
      assertNotNull(result);
      assertTrue(topicExists(adminClient, TEST_TOPIC));
    }
    DescribeTopicsResult describeTopicsResult =
        adminClient.describeTopics(Collections.singleton(TEST_TOPIC));
    KafkaFuture<TopicDescription> kafkaFuture = describeTopicsResult.values().get(TEST_TOPIC);
    assertNotNull(kafkaFuture);
    TopicDescription topicDescription = kafkaFuture.get();
    assertEquals(1, topicDescription.partitions().size());
    TopicPartitionInfo topicPartitionInfo = topicDescription.partitions().get(0);
    assertEquals(0, topicPartitionInfo.partition());
    assertEquals(1, topicPartitionInfo.replicas().size());
    assertEquals(1, topicPartitionInfo.isr().size());
    assertFalse(topicPartitionInfo.leader().isEmpty());
  }

  @Test
  public void testMetrics() {
    Map<MetricName, ? extends Metric> metrics = adminClient.metrics();
    metrics.forEach(
        ((metricName, metric) ->
            System.out.printf(
                "指标名称：[%s]\t指标描述：[%s]\t指标值：[%s]\n",
                metricName.name(), metricName.description(), metric.metricValue())));
  }

  private static boolean topicExists(AdminClient adminClient, String topic) {
    if (null == topic || topic.length() == 0) {
      throw new RuntimeException("topic can't be null");
    }
    ListTopicsResult listTopics = adminClient.listTopics();
    try {
      return listTopics.names().get().stream().anyMatch(topic::equalsIgnoreCase);
    } catch (ExecutionException | InterruptedException e) {
      throw new RuntimeException(String.format("The Topic [%s] is Not Found", topic), e);
    }
  }
}
