package com.shawn.study.deep.in.distributed.zk;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DistributedLock implements Lock {

  private static final Logger LOGGER = LoggerFactory.getLogger(DistributeServer.class);
  private static final int SESSION_TIMEOUT = 3000;
  private static final String CONNECT_STRING =
      "172.27.69.71:2181,172.27.69.59:2181,172.27.69.44:2181";
  private static ZooKeeper zk;
  private static final String ROOT_LOCK_PATH = "/locks_test";
  private String waitPath;
  private String currentPath;
  private final CountDownLatch latch = new CountDownLatch(1);

  private ThreadLocal<String> resourceNameHolder =
      new ThreadLocal<String>() {

        @Override
        protected String initialValue() {
          Thread currentThread = Thread.currentThread();
          StackTraceElement[] stackTraceElements = currentThread.getStackTrace();
          StackTraceElement sourceElement = stackTraceElements[stackTraceElements.length - 1];
          return sourceElement.getClassName() + "-" + sourceElement.getMethodName();
        }
      };

  public DistributedLock() throws Exception {
    zk =
        new ZooKeeper(
            CONNECT_STRING,
            SESSION_TIMEOUT,
            new Watcher() {
              @Override
              public void process(WatchedEvent event) {
                KeeperState state = event.getState();
                if (state == KeeperState.SyncConnected) {
                  latch.countDown();
                }
                if (event.getType() == EventType.NodeDeleted && event.getPath().equals(waitPath)) {
                  latch.countDown();
                }
              }
            });
    latch.await();
    Stat exists = zk.exists(ROOT_LOCK_PATH, false);
    if (exists == null) {
      LOGGER.info("root lock node not exists");
      zk.create(ROOT_LOCK_PATH, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }
  }

  @Override
  public void lock() {
    try {
      String resourceName = getResourceName();
      Stat exists = zk.exists(ROOT_LOCK_PATH + "/" + getResourceName(), true);
      if (exists != null) {
        return;
      }
      Thread.sleep(10);
      currentPath =
          zk.create(
              ROOT_LOCK_PATH + "/" + getResourceName(),
              null,
              Ids.OPEN_ACL_UNSAFE,
              CreateMode.EPHEMERAL);

      List<String> childrenLocks = zk.getChildren(ROOT_LOCK_PATH, false);
      if (childrenLocks.size() == 1) {
        return;
      }
      int index = childrenLocks.indexOf(resourceName);
      if (index == -1) {
        LOGGER.error("lock error");
        throw new RuntimeException("lock error");
      }
      if (index > 0) {
        // 获得排名比 currentNode 前 1 位的节点
        this.waitPath = ROOT_LOCK_PATH + "/" + childrenLocks.get(index - 1);
        // 在 waitPath 上注册监听器, 当 waitPath 被删除时, zookeeper 会回调监听器的 process 方法
        zk.getData(waitPath, true, new Stat()); // 进入等待锁状态
        latch.await();
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void lockInterruptibly() throws InterruptedException {}

  @Override
  public boolean tryLock() {
    return false;
  }

  @Override
  public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
    return false;
  }

  @Override
  public void unlock() {
    try {
      zk.delete(this.currentPath, -1);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Condition newCondition() {
    return null;
  }

  private String getResourceName() {
    return resourceNameHolder.get();
  }
}
