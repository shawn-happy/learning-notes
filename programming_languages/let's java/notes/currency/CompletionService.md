```java
// 创建线程池
ExecutorService executor =
  Executors.newFixedThreadPool(3);
// 异步向电商 S1 询价
Future<Integer> f1 = 
  executor.submit(
    ()->getPriceByS1());
// 异步向电商 S2 询价
Future<Integer> f2 = 
  executor.submit(
    ()->getPriceByS2());
// 异步向电商 S3 询价
Future<Integer> f3 = 
  executor.submit(
    ()->getPriceByS3());
    
// 获取电商 S1 报价并保存
r=f1.get();
executor.execute(()->save(r));
  
// 获取电商 S2 报价并保存
r=f2.get();
executor.execute(()->save(r));
  
// 获取电商 S3 报价并保存  
r=f3.get();
executor.execute(()->save(r));


```

上面代码有个需要注意的，如果获取电商S1的报价耗时很长，那么即便获取电商S2报价的耗时很短，也无法让保存S2的操作先执行，因为主线程阻塞在了f1.get()操作上。

那么如何解决呢？我们可以增加一个阻塞队列，获取到S1,S2,S3的报价都进入阻塞队列，然后再主线程中消费阻塞队列，这样就能保证先获取到的报价先保存到数据库了。代码如下：

```java
// 创建阻塞队列
BlockingQueue<Integer> bq =
  new LinkedBlockingQueue<>();
// 电商 S1 报价异步进入阻塞队列  
executor.execute(()->
  bq.put(f1.get()));
// 电商 S2 报价异步进入阻塞队列  
executor.execute(()->
  bq.put(f2.get()));
// 电商 S3 报价异步进入阻塞队列  
executor.execute(()->
  bq.put(f3.get()));
// 异步保存所有报价  
for (int i=0; i<3; i++) {
  Integer r = bq.take();
  executor.execute(()->save(r));
}  

```

利用CompletionService实现询价系统

CompletionService的实现原理也是内部维护了一个阻塞队列，当任务执行结束就把任务的执行结果加入到阻塞队列中，不同的是CompletionService是把任务执行结果的Future对象加入到阻塞队列中。

ExecutorComletionService是CompletionService的实现类

两个构造方法：

1. ExecutorComletionService(Executor executor);
2. ExecutorComletionService(Executor executor,BlockingQueue< Future< V > > completionService );

如果我们使用的ExecutorComletionService(Executor executor),那么默认使用无界的LinkedBlockingQueue。

```java
// 创建线程池
ExecutorService executor = 
  Executors.newFixedThreadPool(3);
// 创建 CompletionService
CompletionService<Integer> cs = new 
  ExecutorCompletionService<>(executor);
// 异步向电商 S1 询价
cs.submit(()->getPriceByS1());
// 异步向电商 S2 询价
cs.submit(()->getPriceByS2());
// 异步向电商 S3 询价
cs.submit(()->getPriceByS3());
// 将询价结果异步保存到数据库
for (int i=0; i<3; i++) {
  Integer r = cs.take().get();
  executor.execute(()->save(r));
}

```



1. submit(Callable< V > task);
2. submit(Runnable task,V result);
3. Future< V > take() throws InterruptException;
4. Future< V > poll();
5. Future< V > poll(long timeout,TimeUnit unit) throws InterruptException;

这三个方法，区别在于如果阻塞队列是空的，那么调用take()方法的线程就会被阻塞，而poll()方法会返回null值。

poll(long timeout,TimeUnit unit)方法支持以超时的方式获取并移除阻塞队列头部的一个元素，如果等待timeout unit时间，阻塞队列还是空的，那么该方法返回null值。



Dubbo中有一个叫做Forking的集群模式，这种集群模式下，支持并行地调用多个查询服务，只要有一个成功返回结果，整个服务就可以返回了。例如你需要提供一个地址转坐标的服务，为了保证该服务的高可用和性能，你可以并行地调用3个地图服务商的API，然后只要有一个正确返回了结果r，那么地址转坐标这个服务就可以直接返回r了。这种集群模式可以容忍2个地图服务商异常，但缺点是消耗的资源偏多。

```java
geocoder(addr) {
  // 并行执行以下 3 个查询服务， 
  r1=geocoderByS1(addr);
  r2=geocoderByS2(addr);
  r3=geocoderByS3(addr);
  // 只要 r1,r2,r3 有一个返回
  // 则返回
  return r1|r2|r3;
}


// 创建线程池
ExecutorService executor =
  Executors.newFixedThreadPool(3);
// 创建 CompletionService
CompletionService<Integer> cs =
  new ExecutorCompletionService<>(executor);
// 用于保存 Future 对象
List<Future<Integer>> futures =
  new ArrayList<>(3);
// 提交异步任务，并保存 future 到 futures 
futures.add(
  cs.submit(()->geocoderByS1()));
futures.add(
  cs.submit(()->geocoderByS2()));
futures.add(
  cs.submit(()->geocoderByS3()));
// 获取最快返回的任务执行结果
Integer r = 0;
try {
  // 只要有一个成功返回，则 break
  for (int i = 0; i < 3; ++i) {
    r = cs.take().get();
    // 简单地通过判空来检查是否成功返回
    if (r != null) {
      break;
    }
  }
} finally {
  // 取消所有任务
  for(Future<Integer> f : futures)
    f.cancel(true);
}
// 返回结果
return r;

```

