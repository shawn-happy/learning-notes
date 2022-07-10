---
typora-root-url: image
---

## 前言

```java
// 一次性申请转出账户和转入账户，直到成功
while(!actr.apply(this, target))
  ;

```

如果apply操作耗时非常短，而且并发冲突量也不大时，这个方案可以。但是如果操作耗时非常长，或者并发冲突量大的时候，循环等待这种方案就不适用了，因为这种场景下，可能要循环上万次才能获取到锁，太消耗cpu了。

解决方案：

如果线程要求的条件不满足，则线程阻塞自己，进入**等待状态**。当线程要求的条件满足后，通知等待的线程重新执行。

**一个完整的等待-通知机制：线程首先获取互斥锁，当线程要求的条件不满足时，释放互斥锁，进入等待状态；当要求的条件满足时，通知等待的线程，重新获取互斥锁。**

## synchronized+wait+notify/notifyAll

![img](/线程等待通知原理1.png)

左边有一个等待队列，同一时刻，只允许一个线程进入synchronized保护的临界区，当有一个线程进入临界区后，其他线程就只能进入图中左边的等待队列里等待。

**这个等待队列和互斥锁是一对一的关系，每个互斥锁都有自己独立的等待队列。**

在并发程序中，当一个线程进入临界区后，由于某些条件不满足，需要进入等待状态，调用wait方法后，当前线程就会被阻塞，并且进入到右边的等待队列中，这个等待队列也是互斥锁的等待队列。线程在进入等待队列的同时，会释放持有的互斥锁，其他线程就有机会获得锁，并进入临界区。

调用notify/notifyAll，可以通知等待线程重新执行。当条件满足时调用notify，会通知等待队列中的线程，告诉它**条件曾经满足过。**

![img](/线程等待通知原理2.png)

为什么说是曾经满足过呢？

**因为notify()只能保证在通知时间点，条件是满足的。而被通知的线程执行时间点和通知的时间点基本上不会重合。所以当线程执行的时候，很可能条件已经不满足了（保不齐会有其他线程插队）。**

被通知的线程要想重新执行，仍然需要获取到互斥锁（因为曾经获取的锁在调用wait时已经释放了）。

**注意：**

1. wait,notify方法操作的等待队列是互斥锁的等待队列。
2. synchronized锁定的是this,那么对应的一定是this.wait(),this.notify().
3. synchronized锁定的是target，那么对应的一定是target.wait(),target.notify().
4. wait+notify/notifyAll能够被调用的前提是已经获取到相应的互斥锁，所以wait+notify/notifyAll都是在synchronized{}内部调用的。
5. 如果在synchronized{}外部调用，或者锁定的this,而用target.wait()，jvm会抛出java.lang.IllegalMonitorStateException。

## 解决转账问题

考虑四个因素：

1. 互斥锁：Allocator需要是单例的，所以我们可以用this作为互斥锁。
2. 线程要求的条件：转出账户和转入账户都没有被分配过。
3. 何时等待：线程要求的条件不满足
4. 何时通知：当有线程释放账户时就通知。

```java
  while(条件不满足) {
    wait();
  }

```

```java
class Allocator {
  private List<Object> als;
  // 一次性申请所有资源
  synchronized void apply(
    Object from, Object to){
    // 经典写法
    while(als.contains(from) ||
         als.contains(to)){
      try{
        wait();
      }catch(Exception e){
      }   
    } 
    als.add(from);
    als.add(to);  
  }
  // 归还资源
  synchronized void free(
    Object from, Object to){
    als.remove(from);
    als.remove(to);
    notifyAll();
  }
}

```

## 尽量使用notifyAll

**notify是会随机地通知等待队列中的一个线程，而notifyAll会通知等待队列中的所有线程。**

