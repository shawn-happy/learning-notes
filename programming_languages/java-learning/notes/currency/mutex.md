---
typora-root-url: image
---

# 互斥锁

## long类型变量

long类型变量是64位，在32位CPU上执行写操作会被拆分成两次写操作（写高32位和低32位）

![img](/long.png)

在单核CPU场景下，同一时刻只有一个线程执行，禁止CPU中断，意味着操作系统不会重新调度线程，也就是禁止了线程切换，获得CPU使用权的线程就可以不间断的执行，所以两次写操作一定是：**要么都被执行，要么都没有被执行，具有原子性。**

在多核场景下，假设有两个线程同时执行，线程A在CPU-1上执行，线程B在CPU-2上执行，此时禁止CPU中断，只能保证CPU上的线程连续执行，并不能保证同一时刻只有一个线程执行，如果两个线程同时写long类型变量高32位，就有可能出现诡异Bug.

## 互斥

**同一时刻只有一个线程执行，我们称之为互斥，保证原子性。**

### 锁

![img](/锁1.png)

我们把一段需要互斥执行的代码称为临界区，线程在进入临界区之前，首先尝试加锁lock();如果成功，则进入临界区，此时我们称这个线程持有锁；否则就等待，直到持有锁的线程解锁；持有锁的线程执行完临界区的代码后，执行解锁unlock()。

两个问题：

**我们锁的是什么？保护的又是什么**

![img](/锁2.png)

1. 我们要把临界区要保护的资源标注出来，如图中的临界区里增加一个元素：受保护的资源R；
2. 我们要保护资源R就得为它创建一把锁LR；
3. 针对这个锁LR，我们还需要在进出临界区加上加锁和解锁的操作。
4. 在锁LR和R之间的关联，非常重要，很多bug都是因为这里才出现的。



### synchronized

语法：

```java
class X {
  // 修饰非静态方法
  synchronized void foo() {
    // 临界区
  }
  // 修饰静态方法
  synchronized static void bar() {
    // 临界区
  }
  // 修饰代码块
  Object obj = new Object()；
  void baz() {
    synchronized(obj) {
      // 临界区
    }
  }
}  

```

java编译器会在synchronized修饰的方法或代码块前后自动Lock，unlock。

1. synchronized修饰代码块，锁定是个obj对象，或者是一个类，sychronized(this.class)
2. synchronized修饰静态方法，锁定是当前类的class对象
3. synchronized修饰非静态方法，锁定的是当前实例对象this。

```java

class X {
  // 修饰静态方法
  synchronized(X.class) static void bar() {
    // 临界区
  }
}

class X {
  // 修饰非静态方法
  synchronized(this) void foo() {
    // 临界区
  }
}

```

#### 累加器问题

```java
class SafeCalc {
  long value = 0L;
  long get() {
    return value;
  }
  synchronized void addOne() {
    value += 1;
  }
}

```

addone是被synchronized修饰的，对于一个锁的解锁happens-before于后续对这个锁的加锁。指的就是前一个线程的解锁对后面一个线程的加锁是可见的，根据happens-before的传递性原则，我们就能看得出，前一个线程在临界区修改的共享变量，对后续进入临界区的线程是可见的，所以addone()是能得到正确结果的。

但是get()没有用synchronized修饰，所以可见性没法保证，也只需要加上synchronized即可。

![img](/锁3.png)

### 锁和受保护资源的关系

受保护资源和锁之间的关联关系是N:1的关系。可以用一把锁来保护多个资源，但是不能用多把锁来保护一个资源。

```java
class SafeCalc {
  static long value = 0L;
  synchronized long get() {
    return value;
  }
  synchronized static void addOne() {
    value += 1;
  }
}

```

改动后的代码是用两个锁保护一个资源，这个受保护的资源就是value，两个锁分别是this,SafeCalc.class。临界区addone对value的修改对临界区get是没有可见性保障的，这两个临界区也没有互斥关系。这就会导致并发问题。

![img](/锁4.png)

#### 保护没有关联关系的多个资源

案例：银行业务中有针对账户余额（余额是一种资源）的取款操作，也有针对账户密码（密码也是一种资源）的更改操作，我们可以为账户余额和账户密码分配不同的锁来解决并发问题。

```java
class Account {
  // 锁：保护账户余额
  private final Object balLock
    = new Object();
  // 账户余额  
  private Integer balance;
  // 锁：保护账户密码
  private final Object pwLock
    = new Object();
  // 账户密码
  private String password;

  // 取款
  void withdraw(Integer amt) {
    synchronized(balLock) {
      if (this.balance > amt){
        this.balance -= amt;
      }
    }
  } 
  // 查看余额
  Integer getBalance() {
    synchronized(balLock) {
      return balance;
    }
  }

  // 更改密码
  void updatePassword(String pw){
    synchronized(pwLock) {
      this.password = pw;
    }
  } 
  // 查看密码
  String getPassword() {
    synchronized(pwLock) {
      return password;
    }
  }
}

```

当然我们也可以用一把锁保护多个资源，我们可以用this来管理账户类的所有资源，具体实现很简单，所有方法都加上synchronized关键字。

但是用一把锁会导致性能太差，会导致取款，查看余额，修改密码，查看密码这四个操作都是串行的。我们用两把锁，取款和修改密码是可以并行的。

**用不同的锁对受保护资源进行精细化管理，能够提升性能。我们称为细粒度锁。**

#### 保护有关联关系的多个资源

案例：转账，账户A减少100，账户B增加100.

```java
class Account {
  private int balance;
  // 转账
  synchronized void transfer(
      Account target, int amt){
    if (this.balance > amt) {
      this.balance -= amt;
      target.balance += amt;
    }
  } 
}

```

this这把锁可以保护自己的余额this.balance，却保护不了别人的余额target.balance。

![img](/锁5.png)

假设线程1执行账户A转账户B的操作，线程2执行账户B转账户C的操作。这两个操作分别在两颗CPU上同时执行，这两个线程之间不是互斥的，因为线程1锁定的是账户A的实例（A.this），线程2锁定的是账户B的实例。当这两个线程同时进入临界区，最后导致账户B的余额可能是100（线程1先于线程2写B.balance,线程1写的B.balance值被线程2覆盖）,300（线程1后于线程2写B.balance,线程2写的B.balance值被线程1覆盖）。

### 使用锁的正确姿势

如何让对象A和对象B共享一把锁。

可以让所有对象都持有一个唯一性的对象，把这个对象在创建Account时传入。

```java
class Account {
  private Object lock；
  private int balance;
  private Account();
  // 创建 Account 时传入同一个 lock 对象
  public Account(Object lock) {
    this.lock = lock;
  } 
  // 转账
  void transfer(Account target, int amt){
    // 此处检查所有对象共享的锁
    synchronized(lock) {
      if (this.balance > amt) {
        this.balance -= amt;
        target.balance += amt;
      }
    }
  }
}

```

注意：**如果传入的lock必须是同一个对象，如果不是，那么程序就会出bug。**

用Account.class作为共享锁。

```java
class Account {
  private int balance;
  // 转账
  void transfer(Account target, int amt){
    synchronized(Account.class) {
      if (this.balance > amt) {
        this.balance -= amt;
        target.balance += amt;
      }
    }
  } 
}

```

![img](/锁6.png)



## 原子性的本质

不可分割只是外在表现，其本质是多个资源间有一致性的要求，操作的中间状态对外不可见。

解决原子性问题，就是保证中间状态对外不可见。



<font color="red">不能用可变对象做锁</font>

