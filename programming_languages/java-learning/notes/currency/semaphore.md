信号量（Semaphore）是 Java 多线程并发中的一种 JDK 内置同步器，通过它可以实现多线程对公共资源的并发访问控制。

<!--more-->

# 信号量由来

## 限流器

信号量的主要应用场景是控制最多 N 个线程同时地访问资源，其中计数器的最大值即是许可的最大值 N。

现在我们需要开发一个限流器，同一时刻最多有10个请求可以执行。对于这样的需求，我们实现的方案有：

1. 使用Atomic类
2. 使用Lock
3. 使用条件变量
4. 使用信号量

## 使用Atomic类实现

```java
public class LimitByAtomic {

  private static final AtomicInteger COUNTER = new AtomicInteger(10);

  public void f() {
    int count = COUNTER.decrementAndGet();
    if (count < 0) {
      COUNTER.incrementAndGet();
      System.out.println("拒绝执行业务逻辑");
      return; // 拒绝执行业务逻辑
    }

    try {
      // 执行业务逻辑
      System.out.println("执行业务逻辑");
    } finally {
      COUNTER.incrementAndGet();
    }
  }
}
```

## 使用Lock实现

```java
public class LimitByLock {

  private int count = 10;

  public void f() {
    if (count <= 0) {
      System.out.println("拒绝执行业务逻辑");
      return;
    }

    synchronized (this) {
      if (count <= 0) {
        System.out.println("拒绝执行业务逻辑");
        return;
      }
      count--;
    }

    try {
      // 执行业务逻辑
      System.out.println("执行业务逻辑");
    } finally {
      synchronized (this) {
        count++;
      }
    }
  }
}
```



## 使用条件变量实现

对于使用Atomic类还是Lock这两种实现方式，都有一个缺点，如果10个线程同时执行，当第11个线程来执行的时候，会被拒绝掉，这样就没有执行业务逻辑的机会，造成请求丢失。

所以我们可以通过线程等待-通知机制来解决上面的问题。如果10个线程同时执行，当第11个线程来执行的时候，先阻塞这第11个线程，等待前面的10个线程只要执行完一个，就通知第11个线程来执行。

```java
public class LimitByCondition {

  private int count = 10;

  public void f() throws Exception {
    synchronized (this) {
      while (count <= 0) {
        System.out.println("等待执行业务逻辑");
        this.wait();
      }
      count--;
    }

    try {
      System.out.println("执行业务逻辑");
    } finally {
      synchronized (this) {
        count++;
        this.notifyAll();
      }
    }
  }
}
```

## 使用Semaphore实现

除了使用条件变量，java sdk中还可以使用`Semaphore`来实现。

```java
public class LimitBySemaphore {

  private final Semaphore semaphore = new Semaphore(10);

  public void f() throws Exception {
    semaphore.acquire();
    try {
      System.out.println("执行业务逻辑");
    } finally {
      semaphore.release();
    }
  }
}
```

接下来我们就来探讨一下`Semaphore`的实现原理。

# Semaphore实现原理

## 信号量模型

实际上Semaphore的实现原理非常简单，总结下来就是：**一个计数器，一个等待队列，三个方法。**

在信号量模型里，计数器和等待队列对外是透明，所以只能通过信号量模型提供的三个方法访问，`init(),down(),up()`--这些方法都是原子性的。

`init()`:**设置计数器的初始值。**

`down()`:**计数器的值减1；如果此时计数器的值小于0，则当前线程将被阻塞，否则当前线程可以继续执行。**

`up()`:**计数器的值加1；如果此时计数器的值小于等于0，则唤醒等待队列中的一个线程，并将其从等待队列中移除。**

```java
class MySemaphore{
  // 计数器
  int count;
  // 等待队列
  Queue queue;
  // 初始化操作
  MySemaphore(int c){
    this.count=c;
  }
  // 
  void down(){
    this.count--;
    if(this.count<0){
      // 将当前线程插入等待队列
      // 阻塞当前线程
    }
  }
  void up(){
    this.count++;
    if(this.count<=0) {
      // 移除等待队列中的某个线程 T
      // 唤醒线程 T
    }
  }
}

```

使用方法如下：

```java
static int count;
// 初始化信号量
static final MySemaphore s 
    = new MySemaphore(1);
// 用信号量保证互斥    
static void addOne() {
  s.down();
  try {
    count+=1;
  } finally {
    s.up();
  }
}

```

实际上信号量模型，down()、up() 这两个操作历史上最早称为 P 操作和 V 操作，所以信号量模型也被称为 PV 原语。

## Java Semaphore的实现

```java
public class Semaphore implements java.io.Serializable {
	public void acquire() throws InterruptedException;
  public void acquireUninterruptibly();
  public boolean tryAcquire();
  public boolean tryAcquire(long timeout, TimeUnit unit);
  public void release();
  
  public void acquire(int permits) throws InterruptedException;
  public void acquireUninterruptibly(int permits) ;
  public boolean tryAcquire(int permits);
  public boolean tryAcquire(int permits, long timeout, TimeUnit unit)
    throws InterruptedException;
  public void release(int permits);
}
```

Java Semaphore的实现，`acquire()`对应信号量模型里的`down()`方法，`release()`对应信号量模型里的`up()`方法。

Semaphore类提供的常用方法有以下几个。我们可以粗略地将以下方法分为两组。前五个为一组，默认一次获取或释放的许可（permit）个数为1。后五个为一组，可以指定一次获取或释放的许可个数。对于每组方法来说，都有4个不同的获取许可的方法：可中断获取、不可中断获取、非阻塞获取、可超时获取，这跟Lock提供的各种加锁方法非常相似。

Java Semaphore的实现也是基于AQS来实现的，跟ReentrantLock一样，Semaphore中的AQS也有公平锁与非公平锁这两种实现。

```java
public class Semaphore implements java.io.Serializable {
 		abstract static class Sync extends AbstractQueuedSynchronizer {
    }

    // 非公平锁
    static final class NonfairSync extends Sync {
    }
    
    // 公平锁
    static final class FairSync extends Sync {
    }
  
    // 默认使用非公平锁
    public Semaphore(int permits) {
        sync = new NonfairSync(permits);
    }

    public Semaphore(int permits, boolean fair) {
        sync = fair ? new FairSync(permits) : new NonfairSync(permits);
    }
}
```

Semaphore可以看做是一种共享锁，因此，FairSync类和NofairSync类实现了AQS的tryAcquireShared()抽象方法，不过，实现逻辑并不相同。对于tryReleaseShared()抽象方法，因为在FairSync和NofairSync中的实现逻辑相同，因此，它被放置于FairSync和NofairSync的公共父类Sync中。

`acquire()`实现如下：

```java
// java.util.concurrent.Semaphore#acquire()
public void acquire() throws InterruptedException {
  sync.acquireSharedInterruptibly(1);
}

// java.util.concurrent.locks.AbstractQueuedSynchronizer#acquireSharedInterruptibly
public final void acquireSharedInterruptibly(int arg)
            throws InterruptedException {
  // 先判断线程有没有被中断
  if (Thread.interrupted())
    throw new InterruptedException();
  // 尝试获取共享锁，如果获取许可失败，返回值<0, 需要进入等待队列
  if (tryAcquireShared(arg) < 0)
    doAcquireSharedInterruptibly(arg); // 排队等待队列
}

```

`tryAcquireShared()`实现

```java
// java.util.concurrent.Semaphore.FairSync#tryAcquireShared
protected int tryAcquireShared(int acquires) {
  for (;;) {
    if (hasQueuedPredecessors()) // 比非公平锁多了这一行
      return -1;
    int available = getState();
    int remaining = available - acquires;
    if (remaining < 0 ||
        compareAndSetState(available, remaining))
      return remaining;
  }
}

// java.util.concurrent.Semaphore.Sync#nonfairTryAcquireShared
final int nonfairTryAcquireShared(int acquires) {
  for (;;) {
    int available = getState();
    int remaining = available - acquires;
    if (remaining < 0 ||
        compareAndSetState(available, remaining))
      return remaining;
  }
}
```

以上两个`tryAcquireShared()`函数的代码实现基本相同。许可个数存放在AQS的state变量中，两个函数都是通过自旋+CAS的方式来获取许可。两个函数唯一的区别在于，对于公平模式下的Semaphore，当线程调用tryAcquireShared()函数时，如果等待队列中有等待许可的线程，那么，线程将直接去排队等待许可，而不是像非公平模式下的Semaphore那样，线程可以插队直接竞争许可。

`release()`实现

```java
// java.util.concurrent.Semaphore#release()
public void release() {
  sync.releaseShared(1);
}

// java.util.concurrent.locks.AbstractQueuedSynchronizer#releaseShared
public final boolean releaseShared(int arg) {
  // 尝试释放许可
  if (tryReleaseShared(arg)) {
    // 唤醒等待队列其中一个线程
    doReleaseShared();
    return true;
  }
  return false;
}

// java.util.concurrent.Semaphore.Sync#tryReleaseShared
protected final boolean tryReleaseShared(int releases) {
  // 采用自旋 + CAS来更新state
  for (;;) {
    int current = getState();
    int next = current + releases;
    if (next < current) // overflow
      throw new Error("Maximum permit count exceeded");
    if (compareAndSetState(current, next))
      return true;
  }
}
```

# 总结

semaphore其中一个功能是lock不容易实现的，那就是：**semaphore可以允许多个线程访问同一个临界区。**

比较常见的需求就是我们工作中遇到各种池化资源，例如连接池，对象池，线程池等等。其中，最熟悉的可能是数据库连接池，在同一时刻，一定是允许多个线程同时使用连接池的，当然，每个链接在被释放前，是不允许其他线程使用的。

对象池：一次性创建出N个对象，之后所有的线程重复利用这N个对象，对象在被释放前，也是不允许其他线程使用的。对象池，可以用List保存实例对象。

```java
class ObjPool<T, R> {
  final List<T> pool;
  // 用信号量实现限流器
  final Semaphore sem;
  // 构造函数
  ObjPool(int size, T t){
    pool = new Vector<T>(){};
    for(int i=0; i<size; i++){
      pool.add(t);
    }
    sem = new Semaphore(size);
  }
  // 利用对象池的对象，调用 func 限流
  R exec(Function<T,R> func) {
    T t = null;
    sem.acquire();
    try {
      t = pool.remove(0);
      return func.apply(t);
    } finally {
      pool.add(t);
      sem.release();
    }
  }
}
// 创建对象池
ObjPool<Long, String> pool = 
  new ObjPool<Long, String>(10, 2);
// 通过对象池获取 t，之后执行  
pool.exec(t -> {
    System.out.println(t);
    return t.toString();
});

```

