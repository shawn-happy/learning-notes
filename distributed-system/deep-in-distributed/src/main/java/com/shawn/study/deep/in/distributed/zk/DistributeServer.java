package com.shawn.study.deep.in.distributed.zk;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DistributeServer {

  private static final Logger LOGGER = LoggerFactory.getLogger(DistributeServer.class);
  private static final int SESSION_TIMEOUT = 3000;
  private static final String CONNECT_STRING =
      "172.27.69.71:2181,172.27.69.59:2181,172.27.69.44:2181";
  private static ZooKeeper zk;
  private static final String PARENT_PATH = "/shawn_servers";

  public void getConnect() throws Exception {
    zk =
        new ZooKeeper(
            CONNECT_STRING,
            SESSION_TIMEOUT,
            new Watcher() {
              @Override
              public void process(WatchedEvent event) {}
            });
    Stat exists = zk.exists(PARENT_PATH, false);
    if (exists == null) {
      zk.create(
          PARENT_PATH,
          PARENT_PATH.getBytes(StandardCharsets.UTF_8),
          Ids.OPEN_ACL_UNSAFE,
          CreateMode.PERSISTENT);
    }
  }

  // 注册服务器
  public void registerServer(String hostname) throws Exception {
    String create =
        zk.create(
            PARENT_PATH + "/server",
            hostname.getBytes(),
            Ids.OPEN_ACL_UNSAFE,
            CreateMode.EPHEMERAL_SEQUENTIAL);
    LOGGER.info("hostname: {}, create: {}", hostname, create);
  }

  // 业务功能
  public void business(String hostname) throws Exception {
    LOGGER.info(hostname + " is working ...");
    Thread.sleep(Long.MAX_VALUE);
  }

  public static void main(String[] args) throws Exception {
    DistributeServer server = new DistributeServer();
    server.getConnect();
    server.business("server1");
  }
}
