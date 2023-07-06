---
typora-root-url: image
---

本章节主要讲解另外两个线程同步器：`CountDownLatch`和`CyclicBarrier`的用法，使用场景以及实现原理。

<!--more-->

# CountDownLatch的用法

` CountDownLatch`主要有两个方法：

* `countDown()`：用于使计数器减一，一般是执行任务的线程调用。
* `await()`：调用该方法的线程处于等待状态，一般是主线程调用。

这里需要注意的是

`countDown()`方法并没有规定一个线程只能调用一次，当同一个线程调用多次`countDown()`方法时，每次都会使计数器减一；

`await()`方法也并没有规定只能有一个线程执行该方法，如果多个线程同时执行`await()`方法，那么这几个线程都将处于等待状态，并且以共享模式享有同一个锁。

如下是其使用示例：

```java
private static void countDownLatch() throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(5);
    ExecutorService service = Executors.newFixedThreadPool(5);

    for (int i = 0; i < 4; i++) {
      service.submit(
          () -> {
            action();
            latch.countDown();
          });
    }

    service.awaitTermination(50, TimeUnit.MILLISECONDS);
    latch.await();
    System.out.println("Done");
    service.shutdownNow();
  }

  private static void action() {
    System.out.printf("当前线程[%s], 正在执行。。。\n", Thread.currentThread().getName());
  }

```

# CountDownLatch实现原理

```java
// java.util.concurrent.CountDownLatch
public class CountDownLatch {
    /**
     * Synchronization control For CountDownLatch.
     * Uses AQS state to represent count.
     */
    private static final class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = 4982264981922014374L;

        Sync(int count) {
            setState(count);
        }

        int getCount() {
            return getState();
        }

        protected int tryAcquireShared(int acquires) {
            return (getState() == 0) ? 1 : -1;
        }

        protected boolean tryReleaseShared(int releases) {
            // Decrement count; signal when transition to zero
            for (;;) {
                int c = getState();
                if (c == 0)
                    return false;
                int nextc = c - 1;
                if (compareAndSetState(c, nextc))
                    return nextc == 0;
            }
        }
    }

    private final Sync sync;
    public CountDownLatch(int count) {
        if (count < 0) throw new IllegalArgumentException("count < 0");
        this.sync = new Sync(count);
    }

    public void await() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }
    public boolean await(long timeout, TimeUnit unit)
        throws InterruptedException {
        return sync.tryAcquireSharedNanos(1, unit.toNanos(timeout));
    }
    public void countDown() {
        sync.releaseShared(1);
    }

}
```

从上述摘录的源代码来看，`CountDownLatch`的实现还是依赖于`AQS`，这跟之前讨论过的`ReentrantLock`的实现原理基本一致。

接下来，我们主要分析一下`await()`和`countDown()`的实现原理。

**`await()`**步骤如下：

```java
public final void acquireSharedInterruptibly(int arg)
  throws InterruptedException {
  if (Thread.interrupted())
    throw new InterruptedException();
  if (tryAcquireShared(arg) < 0) // 查看state是否为0，如果为0，直接返回，如果不为0，则调用doAcquireSharedInterruptibly()阻塞等待state变为0.
    doAcquireSharedInterruptibly(arg);
}
```

**`countDown()`**步骤如下：

```java
public final boolean releaseShared(int arg) {
  if (tryReleaseShared(arg)) { // state--，如果state变为0，则执行doReleaseShared()，唤醒等待队列中的线程
    doReleaseShared();
    return true;
  }
  return false;
}
```

# CyclicBarrier的用法

`CyclicBarrier`主要是有两个方法：

`await()`：计数器减1，当计数器的值为0，唤醒等待的线程。

`reset()`：重置计数器

```java
public class CyclicBarrierDemo {

  public static void main(String args[]) throws InterruptedException, BrokenBarrierException {

    CyclicBarrier barrier = new CyclicBarrier(4);
    Party first = new Party(1000, barrier, "PARTY-1");
    Party second = new Party(2000, barrier, "PARTY-2");
    Party third = new Party(3000, barrier, "PARTY-3");
    Party fourth = new Party(4000, barrier, "PARTY-4");

    first.start();
    second.start();
    third.start();
    fourth.start();

    System.out.println(Thread.currentThread().getName() + " has finished");
  }
}

class Party extends Thread {
  private int duration;
  private CyclicBarrier barrier;

  public Party(int duration, CyclicBarrier barrier, String name) {
    super(name);
    this.duration = duration;
    this.barrier = barrier;
  }

  @Override
  public void run() {
    try {
      Thread.sleep(duration);
      System.out.println(Thread.currentThread().getName() + " is calling await()");
      barrier.await();
      System.out.println(Thread.currentThread().getName() + " has started running again");
    } catch (InterruptedException | BrokenBarrierException e) {
      e.printStackTrace();
    }
  }
}
```

# CyclicBarrier实现原理

`CyclicBarrier`与`CountDownLatch`不同，`CountDownLatch`的同步是基于`AQS`同步器，而`CyclicBarrier`的同步是基于条件变量的，部分源代码如下：

```java
public class CyclicBarrier {
    private static class Generation {
        Generation() {}                 // prevent access constructor creation
        boolean broken;                 // initially false
    }

    /** The lock for guarding barrier entry */
    private final ReentrantLock lock = new ReentrantLock();
    /** Condition to wait on until tripped */
    private final Condition trip = lock.newCondition();
    /** The number of parties */
    private final int parties;
    /** The command to run when tripped */
    private final Runnable barrierCommand;
    /** The current generation */
    private Generation generation = new Generation();

    /**
     * Number of parties still waiting. Counts down from parties to 0
     * on each generation.  It is reset to parties on each new
     * generation or when broken.
     */
    private int count;

    private void nextGeneration() {
        // signal completion of last generation
        trip.signalAll();
        // set up next generation
        count = parties;
        generation = new Generation();
    }

    private void breakBarrier() {
        generation.broken = true;
        count = parties;
        trip.signalAll();
    }

    /**
     * Main barrier code, covering the various policies.
     */
    private int dowait(boolean timed, long nanos)
        throws InterruptedException, BrokenBarrierException,
               TimeoutException {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            final Generation g = generation;
						// 如果generation.broken为true的话，说明这个屏障已经损坏，当某个线程await的时候，直接抛出异常
            if (g.broken)
                throw new BrokenBarrierException();

            if (Thread.interrupted()) {
                breakBarrier();
                throw new InterruptedException();
            }

            int index = --count;
            // 当count == 0时，说明所有线程都已经到屏障处，
            if (index == 0) {  // tripped
                boolean ranAction = false;
                try {
                    final Runnable command = barrierCommand;
                    if (command != null)
                        command.run();
                    ranAction = true;
                    // 执行条件变量的signalAll方法唤醒等待的线程。
                    // 实现屏障的循环使用，重置
                    nextGeneration();
                    return 0;
                } finally {
                    if (!ranAction)
                        breakBarrier();
                }
            }

            // loop until tripped, broken, interrupted, or timed out
            // 如果count！= 0，说明有线程还未到屏障处，则在锁条件变量trip上等待。
            for (;;) {
                try {
                    if (!timed)
                        trip.await();
                    else if (nanos > 0L)
                        nanos = trip.awaitNanos(nanos);
                } catch (InterruptedException ie) {
                    if (g == generation && ! g.broken) {
                        breakBarrier();
                        throw ie;
                    } else {
                        // We're about to finish waiting even if we had not
                        // been interrupted, so this interrupt is deemed to
                        // "belong" to subsequent execution.
                        Thread.currentThread().interrupt();
                    }
                }

                if (g.broken)
                    throw new BrokenBarrierException();

                if (g != generation)
                    return index;

                if (timed && nanos <= 0L) {
                    breakBarrier();
                    throw new TimeoutException();
                }
            }
        } finally {
            lock.unlock();
        }
    }

  //....
    public int await() throws InterruptedException, BrokenBarrierException {
        try {
            return dowait(false, 0L);
        } catch (TimeoutException toe) {
            throw new Error(toe); // cannot happen
        }
    }
    public int await(long timeout, TimeUnit unit)
        throws InterruptedException,
               BrokenBarrierException,
               TimeoutException {
        return dowait(true, unit.toNanos(timeout));
    }
 
    public void reset() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            breakBarrier();   // break the current generation
            nextGeneration(); // start a new generation
        } finally {
            lock.unlock();
        }
    }

  
}
```

# 极客时间专栏的案例：

## 案例

用户通过在线商城下单，会生成电子订单，保存在订单库；之后物流就会生成派送单给用户发货，派送单存在派送单库。为了防止漏派送或者重复派送，对账系统每天都会校验是否存在异常订单，将差异写在订单库。

![派送单-订单-差异](/派送单-订单-差异.png)

代码示例：

```java
while(存在未对账订单){
  // 查询未对账订单
  pos = getPOrders();
  // 查询派送单
  dos = getDOrders();
  // 执行对账操作
  diff = check(pos, dos);
  // 差异写入差异库
  save(diff);
} 

```

## 优化

单线程的执行示意图：

![单线程-派送单-订单-差异](/单线程-派送单-订单-差异.png)

目前的对账系统，由于订单量和派送单量巨大，所以getPOrders()和getDOrders()相对较慢，另外这两个操作没有先后顺序的依赖。这两个操作并行以后，你会发现，吞吐量近乎单线程的2倍，示意图如下：

![多线程-派送单-订单-差异](/多线程-派送单-订单-差异.png)

代码示例：

```java
while(存在未对账订单){
  // 查询未对账订单
  Thread T1 = new Thread(()->{
    pos = getPOrders();
  });
  T1.start();
  // 查询派送单
  Thread T2 = new Thread(()->{
    dos = getDOrders();
  });
  T2.start();
  // 等待 T1、T2 结束
  T1.join();
  T2.join();
  // 执行对账操作
  diff = check(pos, dos);
  // 差异写入差异库
  save(diff);
} 

```

我们创建两个线程T1,T2，并行执行getPOrders()和getDOrders()。需要等t1,t2都执行完毕后，才能执行对账操作，差异写入差异库的操作。



## 用CountDownLatch实现线程等待

上述代码的缺点：while循环，每次都会创建新的线程，创建线程是个重量级操作，所以最好能把创建出来的线程重复利用--线程池

我们创建一个固定大小为2的线程池，之后在while循环里重复利用。但是在线程池方案里，线程根本就不会退出，执行join失效，导致check和save不知道啥时执行。

Join方案无效，那么我们可以采用计数器的方案来实现，比如计数器的初始值是2，当执行完getPOrders(),计数器减一，变为1，当执行完getDOrders()计数器再减一，变为0，当计数器变为0的时候，开始执行check,save操作。

利用CountDownLatch就可以实现

代码示例：

```java
// 创建 2 个线程的线程池
Executor executor = 
  Executors.newFixedThreadPool(2);
while(存在未对账订单){
  // 计数器初始化为 2
  CountDownLatch latch = 
    new CountDownLatch(2);
  // 查询未对账订单
  executor.execute(()-> {
    pos = getPOrders();
     // 计数器-1
    latch.countDown();
  });
  // 查询派送单
  executor.execute(()-> {
    dos = getDOrders();
      // 计数器-1
    latch.countDown();
  });
  
  // 等待两个查询操作结束 计数器变为0
  latch.await();
  
  // 执行对账操作
  diff = check(pos, dos);
  // 差异写入差异库
  save(diff);
}

```

## 进一步优化性能

前面我们将getPOrders()和getDOrders()并行了，事实上查询操作和对账操作也是可以并行的。在执行对账操作的时候，可以同时去执行下一轮的查询操作。过程示意图：

![多线程-派送单-订单-差异2](/多线程-派送单-订单-差异2.png)



两次查询操作能够和对账操作并行，对账操作还依赖查询操作的结果。这是典型的生产者消费者模型。

两次查询是生产者，对账操作是消费者。

需要队列来保存生产者生产的数据，消费者从队列中消费数据。

本案例需要两个队列

订单队列和派送单队列，这两个队列的元素之间是一一对应的。对账操作可以从订单队列里取出一个元素，从派送单队列里取出一个元素，然后对这两个元素进行对账操作，这样数据一定不会乱掉

线程T1执行订单的查询工作，线程T2执行派送单的查询工作，当T1 T2都各自生产完1条数据后，通知T3执行对账操作。要求T1和T2的步调一致，不能一个太快一个太慢。只有这样才能做到各自生产完1条数据的时候，是一一对应的，并且能通知T3。

## 用CyclicBarrier实现同步

计数器初始化为2，当T1 T2生产完一条数据都将计数器-1，如果计数器大于0，则线程T1或者T2等待。如果计数器=0，则通知线程T3，并唤醒等待线程T1或者T2，与此同时，计数器重置为2.

```java
// 订单队列
Vector<P> pos;
// 派送单队列
Vector<D> dos;
// 执行回调的线程池 
Executor executor = 
  Executors.newFixedThreadPool(1);
final CyclicBarrier barrier =
  new CyclicBarrier(2, ()->{
    executor.execute(()->check());
  });
  
void check(){
  P p = pos.remove(0);
  D d = dos.remove(0);
  // 执行对账操作
  diff = check(p, d);
  // 差异写入差异库
  save(diff);
}
  
void checkAll(){
  // 循环查询订单库
  Thread T1 = new Thread(()->{
    while(存在未对账订单){
      // 查询订单库
      pos.add(getPOrders());
      // 等待
      barrier.await();
    }
  });
  T1.start();  
  // 循环查询运单库
  Thread T2 = new Thread(()->{
    while(存在未对账订单){
      // 查询运单库
      dos.add(getDOrders());
      // 等待
      barrier.await();
    }
  });
  T2.start();
}

```

# 区别

CountDownLatch用来解决一个线程等待多个线程的场景

CyclicBarrier是一组线程之间互相等待，计数器可以循环利用。

