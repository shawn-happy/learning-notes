---
typora-root-url: image
---

Thread-Per-Message模式，对应到现实世界，其实就是委托代办。

## Worker Thread模式及其实现

类比现实世界里车间的工作模式：车间里的工人，有活儿了，大家一起干，没活儿了就聊聊天等着。

**Worker Thread对应到现实世界里，其实指的就是车间里的工人。**

![img](/Worker Thread模式.png)

在编程领域里该如何模拟车间的这种工作模式呢？或者说如何去实现Worker Thread模式呢？可以用线程池。

线程池有很多优点，例如能够避免重复创建，销毁线程，同时能够限制创建线程的上限等等。

用java的Thread实现Thread-Per-Message模式难以应付高并发场景，原因就在于频繁创建，销毁Java线程的成本优点高，而且无限制地创建线程还可能导致应用OOM。

```java
ExecutorService es = Executors
  .newFixedThreadPool(500);
final ServerSocketChannel ssc = 
  ServerSocketChannel.open().bind(
    new InetSocketAddress(8080));
// 处理请求    
try {
  while (true) {
    // 接收请求
    SocketChannel sc = ssc.accept();
    // 将请求处理任务提交给线程池
    es.execute(()->{
      try {
        // 读 Socket
        ByteBuffer rb = ByteBuffer
          .allocateDirect(1024);
        sc.read(rb);
        // 模拟处理请求
        Thread.sleep(2000);
        // 写 Socket
        ByteBuffer wb = 
          (ByteBuffer)rb.flip();
        sc.write(wb);
        // 关闭 Socket
        sc.close();
      }catch(Exception e){
        throw new UncheckedIOException(e);
      }
    });
  }
} finally {
  ssc.close();
  es.shutdown();
}   

```

## 正确地创建线程池

java的线程池既能够避免无限制地**创建线程**导致OOM，也能避免无限制地接收任务导致OOM。只不过后者经常容易被我们忽略，例如上面的实现中，就被我们忽略了。所以强烈建议你用**创建有界的队列来接收任务**。

当请求量大于有界队列的容量时，就需要合理地拒绝请求。如何合理地拒绝呢？这需要你结合具体的业务场景来判定，即便线程池默认的拒绝策略能够满足你的需求，也同样建议你在**创建线程池时，清晰地指明拒绝策略**。

```java
ExecutorService es = new ThreadPoolExecutor(
  50, 500,
  60L, TimeUnit.SECONDS,
  // 注意要创建有界队列
  new LinkedBlockingQueue<Runnable>(2000),
  // 建议根据业务需求实现 ThreadFactory
  r->{
    return new Thread(r, "echo-"+ r.hashCode());
  },
  // 建议根据业务需求实现 RejectedExecutionHandler
  new ThreadPoolExecutor.CallerRunsPolicy());

```

## 避免死锁

使用线程池，需要注意死锁的问题。

如果提交到相同线程池的任务不是相互独立的，而是有依赖关系的，那么就有可能导致死锁问题。

现象：**应用每运行一段时间偶尔会处于无响应的状态，监控数据看上去一切都正常，但实际上已经不能正常工作了**

该应用将一个大型的计算任务分成两个阶段，第一个阶段的任务会等待第二阶段的子任务完成。在这个应用里，每一个阶段都使用了同一个线程池。

![img](/Worker Thread模式-避免死锁.png)

```java
//L1、L2 阶段共用的线程池
ExecutorService es = Executors.
  newFixedThreadPool(2);
//L1 阶段的闭锁    
CountDownLatch l1=new CountDownLatch(2);
for (int i=0; i<2; i++){
  System.out.println("L1");
  // 执行 L1 阶段任务
  es.execute(()->{
    //L2 阶段的闭锁 
    CountDownLatch l2=new CountDownLatch(2);
    // 执行 L2 阶段子任务
    for (int j=0; j<2; j++){
      es.execute(()->{
        System.out.println("L2");
        l2.countDown();
      });
    }
    // 等待 L2 阶段任务执行完
    l2.await();
    l1.countDown();
  });
}
// 等着 L1 阶段任务执行完
l1.await();
// 执行不到这一行。
System.out.println("end");

```

下面是上述代码停止响应后的线程栈，你会发现线程池中的两个线程全部阻塞在l2.await()上，也就是说，线程池里所有的线程都在等待L2阶段的任务执行完，那么L2阶段的子任务什么时候能够执行完呢？

由于线程池里的线程都阻塞了，没有空闲的线程执行L2阶段的任务了。

![img](/Worker Thread模式-异常.png)

解决：

1. 将线程池的最大线程数调大，如果能够确定任务的数量不是非常多，这个方法是可行的。
2. 为不同的任务创建不同的线程池。
3. 提交到相同线程池中的任务一定是相互独立的，否则就一定要谨慎。

