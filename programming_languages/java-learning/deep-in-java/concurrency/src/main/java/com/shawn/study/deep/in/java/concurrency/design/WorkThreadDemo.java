package com.shawn.study.deep.in.java.concurrency.design;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class WorkThreadDemo {

  public static final ExecutorService service =
      new ThreadPoolExecutor(
          50,
          500,
          60L,
          TimeUnit.SECONDS,
          // 注意要创建有界队列
          new LinkedBlockingQueue<Runnable>(2000),
          // 建议根据业务需求实现ThreadFactory
          r -> {
            return new Thread(r, "echo-" + r.hashCode());
          },
          // 建议根据业务需求实现RejectedExecutionHandler
          new ThreadPoolExecutor.CallerRunsPolicy());

  public static void main(String[] args) throws IOException {

    final ServerSocketChannel ssc = ServerSocketChannel.open().bind(new InetSocketAddress(8080));

    // 处理请求
    try {
      while (true) {
        SocketChannel accept = ssc.accept();
        // 每一个请求都创建一个线程
        service.execute(
            () -> {
              try {
                // 读socket
                ByteBuffer rb = ByteBuffer.allocateDirect(1024);
                accept.read(rb);
                // 模拟处理请求
                TimeUnit.SECONDS.sleep(2);
                // 写socket
                ByteBuffer wb = (ByteBuffer) rb.flip();
                accept.write(wb);
                accept.close();
              } catch (Exception ex) {
                ex.printStackTrace();
              }
            });
      }
    } finally {
      ssc.close();
    }
  }
}

// 死锁
class WorkThreadDemo2 {

  public static void main(String[] args) throws Exception {
    // L1、L2阶段共用的线程池
    ExecutorService es = Executors.newFixedThreadPool(2);
    // L1阶段的闭锁
    CountDownLatch l1 = new CountDownLatch(2);
    for (int i = 0; i < 2; i++) {
      System.out.println("L1");
      // 执行L1阶段任务
      es.execute(
          () -> {
            // L2阶段的闭锁
            CountDownLatch l2 = new CountDownLatch(2);
            // 执行L2阶段子任务
            for (int j = 0; j < 2; j++) {
              es.execute(
                  () -> {
                    System.out.println("L2");
                    l2.countDown();
                  });
            }
            // 等待L2阶段任务执行完
            try {
              l2.await();
              l1.countDown();
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          });
    }
    // 等着L1阶段任务执行完
    l1.await();
    System.out.println("end");
  }
}
