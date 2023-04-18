package com.shawn.study.deep.in.distributed.zk;

import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleZookeeperDemo {

  private static final Logger LOGGER = LoggerFactory.getLogger(SimpleZookeeperDemo.class);
  private static final int SESSION_TIMEOUT = 2000;
  private static final String CONNECT_STRING =
      "172.27.69.71:2181,172.27.69.59:2181,172.27.69.44:2181";

  public static void main(String[] args) throws Exception {
    ZooKeeper zooKeeper =
        new ZooKeeper(
            CONNECT_STRING,
            SESSION_TIMEOUT,
            new Watcher() {
              @Override
              public void process(WatchedEvent event) {
                LOGGER.info("event Type: {}, path: {}", event.getType(), event.getPath());
              }
            });
    String parentPath = "/shawn_zk_test";
    Stat exists = zooKeeper.exists(parentPath, true);
    if (exists == null) {
      zooKeeper.create(
          "/shawn_zk_test",
          "shawn".getBytes(StandardCharsets.UTF_8),
          Ids.OPEN_ACL_UNSAFE,
          CreateMode.PERSISTENT);
    }
    exists = zooKeeper.exists(parentPath, true);
    List<String> childrenPaths = zooKeeper.getChildren(parentPath, true);
    childrenPaths.forEach(
        path -> LOGGER.info("parent path: {} children path: {}", parentPath, path));
    int version = exists.getVersion();
    int aversion = exists.getAversion();
    int cversion = exists.getCversion();
    LOGGER.info("version: {}", version);
    LOGGER.info("aversion: {}", aversion);
    LOGGER.info("cversion: {}", cversion);
    zooKeeper.delete(parentPath, version);
    exists = zooKeeper.exists(parentPath, true);
    LOGGER.info("exists: {}", exists != null);
  }
}
