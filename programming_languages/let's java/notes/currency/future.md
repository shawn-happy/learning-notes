---
typora-root-url: image
---

利用ThreadPoolExecutor的void execute(Runnable command)方法，利用这个方法虽说可以提交任务，但是却没有办法获取任务执行结果。

那么我们如果需要获取任务的执行结果，可以使用submit()方法和FutureTask工具类来实现。

```java
// 提交 Runnable 任务
// 由于Runnable接口的run方法没有返回值，所以，Future仅仅是用来断言任务已经结束，有点类似join();
Future<?> 
  submit(Runnable task);
// 提交 Callable 任务
// Callable里的call方法是有返回值的，所以这个方法返回的Future对象可以通过调用其get()方法来获取任务的执
//行结果。
<T> Future<T> 
  submit(Callable<T> task);
// 提交 Runnable 任务及结果引用  
// Future的返回值就是传给submit()方法的参数result。
<T> Future<T> 
  submit(Runnable task, T result);

```

Future接口5个方法：

```java
// 取消任务
boolean cancel(
  boolean mayInterruptIfRunning);
// 判断任务是否已取消  
boolean isCancelled();
// 判断任务是否已结束
boolean isDone();
// 获得任务执行结果 阻塞，被调用时，如果任务还没有执行完，那么调用get()方法的线程会阻塞。直到任务执行完
// 才会被唤醒
get();
// 获得任务执行结果，支持超时
get(long timeout, TimeUnit unit);

```



<T> Future<T> 
  submit(Runnable task, T result);经典用法：

```java
ExecutorService executor 
  = Executors.newFixedThreadPool(1);
// 创建 Result 对象 r
Result r = new Result();
r.setAAA(a);
// 提交任务
Future<Result> future = 
  executor.submit(new Task(r), r);  
Result fr = future.get();
// 下面等式成立
fr === r;
fr.getAAA() === a;
fr.getXXX() === x

class Task implements Runnable{
  Result r;
  // 通过构造函数传入 result
  Task(Result r){
    this.r = r;
  }
  void run() {
    // 可以操作 result
    a = r.getAAA();
    r.setXXX(x);
  }
}

```

FutureTask是一个工具类，Future是一个接口，FutureTask实现了Runnable和Future接口，由于实现了Runnable接口，所以可以将FutureTask对象作为任务提交给ThreadPoolExecutor去执行，也可以直接被Thread执行；又因为实现了Future接口，所以也能用来获得任务的执行结果。

```java
// 创建 FutureTask
FutureTask<Integer> futureTask
  = new FutureTask<>(()-> 1+2);
// 创建线程池
ExecutorService es = 
  Executors.newCachedThreadPool();
// 提交 FutureTask 
es.submit(futureTask);
// 获取计算结果
Integer result = futureTask.get();

```

```java
// 创建 FutureTask
FutureTask<Integer> futureTask
  = new FutureTask<>(()-> 1+2);
// 创建并启动线程
Thread T1 = new Thread(futureTask);
T1.start();
// 获取计算结果
Integer result = futureTask.get();

```

FutureTask可以很容易获取子线程的执行结果。





烧水泡茶：

![img](/Future-任务分工图.png)

并发编程可以总结为三个核心问题：分工，同步和互斥。编写并发程序，首先要做分工。

1. T1负责洗水壶，烧开水，泡茶这三道工序
2. T2负责洗茶壶，洗茶杯，拿茶叶三道工序。
3. T1在执行泡茶这道工序需要等到T2完成拿茶叶的工作。（join,countDownLatch，阻塞队列都可以完成）

![img](/Future-关系图.png)

```java
// 创建任务 T2 的 FutureTask
FutureTask<String> ft2
  = new FutureTask<>(new T2Task());
// 创建任务 T1 的 FutureTask
FutureTask<String> ft1
  = new FutureTask<>(new T1Task(ft2));
// 线程 T1 执行任务 ft1
Thread T1 = new Thread(ft1);
T1.start();
// 线程 T2 执行任务 ft2
Thread T2 = new Thread(ft2);
T2.start();
// 等待线程 T1 执行结果
System.out.println(ft1.get());

// T1Task 需要执行的任务：
// 洗水壶、烧开水、泡茶
class T1Task implements Callable<String>{
  FutureTask<String> ft2;
  // T1 任务需要 T2 任务的 FutureTask
  T1Task(FutureTask<String> ft2){
    this.ft2 = ft2;
  }
  @Override
  String call() throws Exception {
    System.out.println("T1: 洗水壶...");
    TimeUnit.SECONDS.sleep(1);
    
    System.out.println("T1: 烧开水...");
    TimeUnit.SECONDS.sleep(15);
    // 获取 T2 线程的茶叶  
    String tf = ft2.get();
    System.out.println("T1: 拿到茶叶:"+tf);

    System.out.println("T1: 泡茶...");
    return " 上茶:" + tf;
  }
}
// T2Task 需要执行的任务:
// 洗茶壶、洗茶杯、拿茶叶
class T2Task implements Callable<String> {
  @Override
  String call() throws Exception {
    System.out.println("T2: 洗茶壶...");
    TimeUnit.SECONDS.sleep(1);

    System.out.println("T2: 洗茶杯...");
    TimeUnit.SECONDS.sleep(2);

    System.out.println("T2: 拿茶叶...");
    TimeUnit.SECONDS.sleep(1);
    return " 龙井 ";
  }
}
// 一次执行结果：
//T1: 洗水壶...
//T2: 洗茶壶...
//T1: 烧开水...
//T2: 洗茶杯...
//T2: 拿茶叶...
//T1: 拿到茶叶: 龙井
//T1: 泡茶...
//上茶: 龙井

```

