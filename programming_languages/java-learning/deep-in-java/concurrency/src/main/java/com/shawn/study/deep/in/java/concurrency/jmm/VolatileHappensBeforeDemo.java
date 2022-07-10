package com.shawn.study.deep.in.java.concurrency.jmm;

public class VolatileHappensBeforeDemo {
  int x = 0;
  volatile boolean v = false;

  public void write() {
    x = 42;
    v = true;
  }

  public void read() {
    if (v) {
      System.out.println("read: " + x);
    }
  }

  public static void main(String[] args) throws Exception {
    VolatileHappensBeforeDemo example = new VolatileHappensBeforeDemo();
    Thread t1 =
        new Thread(
            () -> {
              System.out.println(example.x);
              example.write();
            });

    Thread t2 =
        new Thread(
            () -> {
              example.read();
              example.x = 11;
            });
    example.x = 45;
    t1.start(); // 根据happens before start的规则，x的值在t1.start()之前修改了，所以t1看到的是x=45。
    t2.start();
    t1.join(); // 根据happens before join的规则，x的值在t1.join()里修改了，所以t2看到的是x=42。 && volatile变量规则 && 传递性
    t2.join();
    System.out.println(example.x); // 根据happens before join的规则，x的值在t2.join()里修改了，所以main线程看到的是x=11。
  }
}
