---
typora-root-url: image
---

## 前言

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

## 区别

CountDownLatch用来解决一个线程等待多个线程的场景

CyclicBarrier是一组线程之间互相等待，计数器可以循环利用。

