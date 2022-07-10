package com.shawn.study.deep.in.java.concurrency;

/** 用两把不同锁，保护两种不同，没有关联关系的资源，能够提升性能，细粒度锁 */
public class Account {

  // 锁：保护账户余额
  private final Object balLock = new Object();

  // 账户余额
  private double balance;

  // 锁：保护账号密码
  private final Object pwLock = new Object();

  // 账号密码
  private String password;

  /**
   * 取款
   *
   * @param amt
   */
  void withdraw(double amt) {
    synchronized (balLock) {
      if (this.balance > amt) {
        this.balance -= amt;
      }
    }
  }

  /**
   * 查看余额
   *
   * @return
   */
  double getBalance() {
    synchronized (balLock) {
      return balance;
    }
  }

  /**
   * 修改密码
   *
   * @param newPassword
   */
  void updatePassword(String newPassword) {
    synchronized (balLock) {
      this.password = newPassword;
    }
  }

  /**
   * 查看密码
   *
   * @return
   */
  String getPassword() {
    synchronized (balLock) {
      return password;
    }
  }
}
