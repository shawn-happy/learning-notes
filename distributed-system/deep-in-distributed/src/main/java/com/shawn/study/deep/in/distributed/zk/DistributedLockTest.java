package com.shawn.study.deep.in.distributed.zk;

public class DistributedLockTest {
  public static void main(String[] args) throws Exception {
    // 创建分布式锁 1
    final DistributedLock lock1 = new DistributedLock(); // 创建分布式锁 2
    final DistributedLock lock2 = new DistributedLock();
    new Thread(
        () -> { // 获取锁对象
          try {
            lock1.lock();
            System.out.println("线程 1 获取锁");
            Thread.sleep(5 * 1000);
            lock1.unlock();
            System.out.println("线程 1 释放锁");
          } catch (Exception e) {
            e.printStackTrace();
          }
        })
        .start();
    new Thread(
        () -> {
          // 获取锁对象
          try {
            lock2.lock();
            System.out.println("线程 2 获取锁");
            Thread.sleep(5 * 1000);
            lock2.unlock();
            System.out.println("线程 2 释放锁");
          } catch (Exception e) {
            e.printStackTrace();
          }
        })
        .start();
  }
}
