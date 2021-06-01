---
typora-root-url: image
---

利用CompletableFuture来实现烧水泡茶程序

分工：

任务一负责洗水壶，烧开水

任务二负责洗茶壶，洗茶杯，拿茶叶。

任务三泡茶。

![CompletableFuture-任务分工图](/CompletableFuture-任务分工图.png)

```java
// 任务 1：洗水壶 -> 烧开水
CompletableFuture<Void> f1 = 
  CompletableFuture.runAsync(()->{
  System.out.println("T1: 洗水壶...");
  sleep(1, TimeUnit.SECONDS);

  System.out.println("T1: 烧开水...");
  sleep(15, TimeUnit.SECONDS);
});
// 任务 2：洗茶壶 -> 洗茶杯 -> 拿茶叶
CompletableFuture<String> f2 = 
  CompletableFuture.supplyAsync(()->{
  System.out.println("T2: 洗茶壶...");
  sleep(1, TimeUnit.SECONDS);

  System.out.println("T2: 洗茶杯...");
  sleep(2, TimeUnit.SECONDS);

  System.out.println("T2: 拿茶叶...");
  sleep(1, TimeUnit.SECONDS);
  return " 龙井 ";
});
// 任务 3：任务 1 和任务 2 完成后执行：泡茶
CompletableFuture<String> f3 = 
  f1.thenCombine(f2, (__, tf)->{
    System.out.println("T1: 拿到茶叶:" + tf);
    System.out.println("T1: 泡茶...");
    return " 上茶:" + tf;
  });
// 等待任务 3 执行结果
System.out.println(f3.join());

void sleep(int t, TimeUnit u) {
  try {
    u.sleep(t);
  }catch(InterruptedException e){}
}
// 一次执行结果：
T1: 洗水壶...
T2: 洗茶壶...
T1: 烧开水...
T2: 洗茶杯...
T2: 拿茶叶...
T1: 拿到茶叶: 龙井
T1: 泡茶...
上茶: 龙井

```

1. 无需手工维护线程，没有繁琐的手工维护线程的工作，给任务分配的线程的工作也不需要我们关注。
2. 语义更加清晰，例如f3 = f1.thenCombine(f2,()->{}),能够清晰地表达任务三要等任务一和二都完成后才开始。
3. 代码更加简练并且专注于业务逻辑，几乎所有代码都是业务逻辑相关的。



**创建CompletableFuture对象**

4种静态方法

1. runAsync(Runnable runnable)
2. supplyAsync(Supplier< U >   supplier ) Supplier有返回值，可以通过get()获取。

前两种方法和后两种方法的区别在于：后两种方法可以指定线程池参数。

默认情况下CompletableFuture会使用公共的ForkJoinPool线程池，这个线程池默认会创建的线程数为CPU的核数（可以通过JVM    option:-Djava.util.concurrent.ForkJoinPool.common.parallelism来设置ForkJoinPool线程池的线程数）。如果所有CompletableFuture共享一个线程池，那么一旦有任务执行一些很慢的IO操作，就会导致线程池中所有线程都阻塞在IO操作上，从而造成线程饥饿，进而影响整个系统的性能，**要根据不同的业务类型创建不同的线程池，以避免互相干扰。**



```java
// 使用默认线程池
static CompletableFuture<Void> 
  runAsync(Runnable runnable)
static <U> CompletableFuture<U> 
  supplyAsync(Supplier<U> supplier)
// 可以指定线程池  
static CompletableFuture<Void> 
  runAsync(Runnable runnable, Executor executor)
static <U> CompletableFuture<U> 
  supplyAsync(Supplier<U> supplier, Executor executor)  

```



CompletableFuture实现了Future接口，可以知道异步操作什么时候结束，获取异步操作的执行结果

CompletableFuture还实现了CompletionStage接口

任务有时序关系，有**串行关系，并行关系，汇聚关系。**

洗水壶，烧开水是串行关系，

洗水壶烧开水和洗茶壶，洗茶杯这两组任务之间就是并行关系。

烧开水，拿茶叶，泡茶就是汇聚关系。



![CompletableFuture-关系图](/CompletableFuture-关系图.png)



CompletionStage可以很清晰的描述任务之间的这种时序关系，f3 = f1.thenCombine(f2,()->{})描述的就是一种汇聚关系，烧水泡茶程序中的汇聚关系是一种AND关系，这里的AND是指所有依赖的任务都完成后才开始执行当前任务。



1. 描述串行关系：thenApply,thenAccept,thenRun和thenCompose这四个系列的接口。

   thenApply系列函数里参数fn的类型是接口Function<T,R>,这个接口里与CompletionStage相关的方法是R apply(T t),这个方法既能接收参数也支持返回值。所以该方法返回的是CompletionStage< R >.

   thenAccept系列函数里参数fn的类型是接口Consumer< T >,这个接口里与CompletionStage相关的方法是void apply(T t),这个方法接收参数 不支持返回值。所以该方法返回的是CompletionStage< Void>.

   thenRun系列函数里参数fn的类型是接口Runnable,这个方法不接收参数 不支持返回值。所以该方法返回的是CompletionStage< Void>.

   thenCompose：会创建出一个子流程，最终结果和thenApply相同

   ```java
   CompletionStage<R> thenApply(fn);
   CompletionStage<R> thenApplyAsync(fn);
   CompletionStage<Void> thenAccept(consumer);
   CompletionStage<Void> thenAcceptAsync(consumer);
   CompletionStage<Void> thenRun(action);
   CompletionStage<Void> thenRunAsync(action);
   CompletionStage<R> thenCompose(fn);
   CompletionStage<R> thenComposeAsync(fn);
   
   CompletableFuture<String> f0 = 
     CompletableFuture.supplyAsync(
       () -> "Hello World")      //①
     .thenApply(s -> s + " QQ")  //②
     .thenApply(String::toUpperCase);//③
   
   System.out.println(f0.join());
   // 输出结果
   HELLO WORLD QQ
   
   ```

2. 描述AND汇聚关系

   thenCombine,thenAcceptBoth,runAfterBoth

   ```java
   CompletionStage<R> thenCombine(other, fn);
   CompletionStage<R> thenCombineAsync(other, fn);
   CompletionStage<Void> thenAcceptBoth(other, consumer);
   CompletionStage<Void> thenAcceptBothAsync(other, consumer);
   CompletionStage<Void> runAfterBoth(other, action);
   CompletionStage<Void> runAfterBothAsync(other, action);
   
   ```

3. 描述OR汇聚关系

   applyToEither,acceptEither,runAfterEither

   ```java
   CompletionStage applyToEither(other, fn);
   CompletionStage applyToEitherAsync(other, fn);
   CompletionStage acceptEither(other, consumer);
   CompletionStage acceptEitherAsync(other, consumer);
   CompletionStage runAfterEither(other, action);
   CompletionStage runAfterEitherAsync(other, action);
   
   
   
   CompletableFuture<String> f1 = 
     CompletableFuture.supplyAsync(()->{
       int t = getRandom(5, 10);
       sleep(t, TimeUnit.SECONDS);
       return String.valueOf(t);
   });
   
   CompletableFuture<String> f2 = 
     CompletableFuture.supplyAsync(()->{
       int t = getRandom(5, 10);
       sleep(t, TimeUnit.SECONDS);
       return String.valueOf(t);
   });
   
   CompletableFuture<String> f3 = 
     f1.applyToEither(f2,s -> s);
   
   System.out.println(f3.join());
   
   ```

4. 异常

   fn,consumer,action都不允许抛出可检查异常，但是却无法限制他们抛出运行时异常。

   ```java
   CompletionStage exceptionally(fn); // try{}catch
   CompletionStage<R> whenComplete(consumer); //try{} catch finally 不支持返回值
   CompletionStage<R> whenCompleteAsync(consumer);
   CompletionStage<R> handle(fn);// try catch finally 支持返回值
   CompletionStage<R> handleAsync(fn);
   
   ```

   