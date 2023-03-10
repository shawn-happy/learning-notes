package com.shawn.study.deep.in.distributed.zk;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DistributeClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(DistributeClient.class);
  private static final int SESSION_TIMEOUT = 2000;
  private static final String CONNECT_STRING =
      "172.27.69.71:2181,172.27.69.59:2181,172.27.69.44:2181";
  private static ZooKeeper zk;
  private static final String PARENT_PATH = "/servers";

  public void getConnect() throws Exception {
    zk =
        new ZooKeeper(
            CONNECT_STRING,
            SESSION_TIMEOUT,
            new Watcher() {
              @Override
              public void process(WatchedEvent event) {}
            });
  }

  // 获取服务器列表信息
  public void getServerList() throws Exception {
    // 1.获取服务器子节点信息，并且对父节点进行监听
    List<String> children = zk.getChildren(PARENT_PATH, true);
    // 2.存储服务器信息列表
    List<String> servers = new ArrayList<>();
    // 3.遍历所有节点，获取节点中的主机名称信息
    for (String child : children) {
      byte[] data = zk.getData(PARENT_PATH + "/" + child, false, null);
      servers.add(new String(data));
    }
    // 4.打印服务器列表信息
    LOGGER.info("{}", servers);
  }

  // 业务功能
  public void business() throws Exception {
    LOGGER.info("client is working ...");
    Thread.sleep(Long.MAX_VALUE);
  }

  public static void main(String[] args) throws Exception {
    // 1获取zk连接
    DistributeClient client = new DistributeClient();
    client.getConnect();
    // 2获取servers的子节点信息，从中获取服务器信息列表
    client.getServerList();
    // 3业务进程启动
    client.business();
  }
}
