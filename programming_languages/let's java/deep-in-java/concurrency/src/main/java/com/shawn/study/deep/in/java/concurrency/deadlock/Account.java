package com.shawn.study.deep.in.java.concurrency.deadlock;

public class Account {

  // 单例
  private Allocator actr;
  private double balance;

  private int id;

  public Account(double balance) {
    this.balance = balance;
  }

  /** 转账（死锁） */
  void transferDeadLock(Account target, double amt) {
    // 锁定转出账户
    synchronized (this) {
      // 锁定转入账户
      synchronized (target) {
        if (this.balance > amt) {
          this.balance -= amt;
          target.balance += amt;
        }
      }
    }
  }

  /** 转账（破坏占有且等待条件） */
  void transfer(Account target, double amt) {
    // 问题：如果循环成千上万次都没有申请到资源，就会造成程序执行不下去，cpu飙升
    while (!actr.apply(this, target)) {}

    try {
      // 锁定转出账户
      synchronized (this) {
        // 锁定转入账户
        synchronized (target) {
          if (this.balance > amt) {
            this.balance -= amt;
            target.balance += amt;
          }
        }
      }
    } finally {
      actr.free(this, target);
    }
  }

  /** 转账（破坏循环等待条件） */
  void transfer2(Account target, double amt) {
    Account left = this;
    Account right = target;
    if (this.id > target.id) {
      left = target;
      right = this;
    }
    // 锁定id小的账户
    synchronized (left) {
      // 锁定id大的账户
      synchronized (right) {
        if (this.balance > amt) {
          this.balance -= amt;
          target.balance += amt;
        }
      }
    }
  }

  public static void main(String[] args) {
    Account a = new Account(200.0);
    Account b = new Account(200.0);
    for (int i = 0; i < 10011; i++) {
      Thread t1 =
          new Thread(
              () -> {
                a.transferDeadLock(b, 100);
                System.out.println(
                    Thread.currentThread().getName() + ",t1 a.balanece, " + a.balance);
                System.out.println(
                    Thread.currentThread().getName() + ",t1 b.balanece, " + b.balance);
              });

      Thread t2 =
          new Thread(
              () -> {
                b.transferDeadLock(a, 100);
                System.out.println(
                    Thread.currentThread().getName() + ",t2 a.balanece, " + a.balance);
                System.out.println(
                    Thread.currentThread().getName() + ",t2 b.balanece, " + b.balance);
              });

      t1.start();
      t2.start();
    }
  }
}
