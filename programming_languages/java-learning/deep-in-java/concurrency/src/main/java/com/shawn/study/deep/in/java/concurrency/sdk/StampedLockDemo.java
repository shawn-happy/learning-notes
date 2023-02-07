package com.shawn.study.deep.in.java.concurrency.sdk;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.StampedLock;

public class StampedLockDemo {

  private final StampedLock stampedLock = new StampedLock();
  private final List<String> list = new LinkedList<>();

  public void add(int idx, String str) {
    long stamped = stampedLock.writeLock();
    try {
      list.add(idx, str);
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      stampedLock.unlockWrite(stamped);
    }
  }

  public String get(int idx) {
    long stamped = stampedLock.tryOptimisticRead(); // 乐观读
    String res = list.get(idx);
    if (stampedLock.validate(stamped)) { // 如果没有写操作干扰
      return res;
    }
    // 有写操作干扰，重新获取读锁
    stamped = stampedLock.readLock();
    try {
      return list.get(idx);
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      stampedLock.unlockRead(stamped);
    }
  }
}
