package com.shawn.study.deep.in.java.concurrency;

public class AccountRelative {

  // private Object lock = new Object(); 作为共享锁
  // 构造函数的时候传进来
  // AccountRelative.class作为共享锁

  double balance;

  synchronized void transfer(AccountRelative target, double amt) {

    if (this.balance > amt) {
      this.balance -= amt;
      target.balance += amt;
    }
  }

  public static void main(String[] args) throws Exception {
    AccountRelative a = new AccountRelative();
    a.balance = 200.0;

    AccountRelative b = new AccountRelative();
    b.balance = 200.0;

    AccountRelative c = new AccountRelative();
    c.balance = 200.0;

    Thread t1 =
        new Thread(
            () -> {
              a.transfer(b, 100.0);
            });

    Thread t2 =
        new Thread(
            () -> {
              b.transfer(c, 100.0);
            });

    t2.start();
    t1.start();
    t1.join();
    t2.join();

    System.out.println("a.balance=" + a.balance);
    System.out.println("b.balance=" + b.balance);
    System.out.println("c.balance=" + c.balance);
  }
}
