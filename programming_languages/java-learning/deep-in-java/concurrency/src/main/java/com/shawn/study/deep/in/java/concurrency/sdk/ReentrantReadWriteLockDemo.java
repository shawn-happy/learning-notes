package com.shawn.study.deep.in.java.concurrency.sdk;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockDemo {

  private final List<String> strList = new LinkedList<>();
  private final ReadWriteLock lock = new ReentrantReadWriteLock();
  private final Lock readLock = lock.readLock();
  private final Lock writeLock = lock.writeLock();

  public void add(int idx, String str) {
    writeLock.lock();
    try {
      strList.add(idx, str);
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      writeLock.unlock();
    }
  }

  public String get(int idx) {
    readLock.lock();
    try {
      return strList.get(idx);
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      readLock.unlock();
    }
  }
}
