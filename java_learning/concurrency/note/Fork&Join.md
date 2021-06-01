---
typora-root-url: image
---

对于简单的并行任务，我们可以通过“线程池+Future”的方案来解决；如果任务之间有聚合关系，无论是AND聚合还是OR聚合，都可以通过CompletableFutrue来解决，而批量的并行任务，则可以通过CompletionService来解决。

并发编程分为三个层面的问题：分工，协作，互斥。

当你关注与任务的时候，你会发现你的视角已经从并发编程的细节中跳出来了，你应用的更多的是现实世界的思维模式，类比的往往是现实世界里的分工。

我们把线程池，Future,CompletableFuture,CompletionService都列到了分工里面。

分治：

![img](/Fork&join分解子任务.png)





Fork/Join是一个并行计算的框架，主要就是用来支持分治任务模型的，这个计算框架里的Fork对应的是分治任务模型里的任务分解，join对应的是结果合并。Fork/Join计算框架主要包含两个部分，一部分是分治任务的线程池ForkJoinPool，另一部分是分治任务ForkJoinTask，有点类似ThreadPoolExecutor和Runnable的关系，都可以理解为提交任务到线程池。

ForkJoinTask是一个抽象类，核心方法是fork().join()

fork()会异步地执行一个子任务，join()方法则会阻塞当前线程来等待子任务的执行结果。

ForkJoinTask两个子类：RecursiveAction.RecursiveTask,compute()---RecursiveAction没有返回值。

```java
static void main(String[] args){
  // 创建分治任务线程池  
  ForkJoinPool fjp = 
    new ForkJoinPool(4);
  // 创建分治任务
  Fibonacci fib = 
    new Fibonacci(30);   
  // 启动分治任务  
  Integer result = 
    fjp.invoke(fib);
  // 输出结果  
  System.out.println(result);
}
// 递归任务
static class Fibonacci extends 
    RecursiveTask<Integer>{
  final int n;
  Fibonacci(int n){this.n = n;}
  protected Integer compute(){
    if (n <= 1)
      return n;
    Fibonacci f1 = 
      new Fibonacci(n - 1);
    // 创建子任务  
    f1.fork();
    Fibonacci f2 = 
      new Fibonacci(n - 2);
    // 等待子任务结果，并合并结果  
    return f2.compute() + f1.join();
  }
}

```

工作原理：

ForkJoinPool本质上也是一个生产者-消费者的实现，ThreadPoolExecutor内部只有一个任务队列，而ForkJoinPool内部有多个任务队列，当我们通过ForkJoinPool的invoke()和submit()方法提交任务时，ForkJoinPool根据一定的路由规则把任务提交到一个任务队列中，如果任务在执行过程中会创建出子任务，那么子任务会提交到工作线程对应的任务队列中。

如果工作线程对应的任务队列空了，是不是就没活儿干了呢？不是，ForkJoinPool支持一种叫做“任务窃取”的机制，如果工作线程空闲了，那它就可以窃取其他工作任务队列里的任务。

T2对应的任务队列已经空了，它可以窃取线程T1对应的任务队列的任务。

ForkJoinPool中的任务队列采用的是双端队列，工作线程正常获取任务和窃取任务分别是从任务队列不同的端消费，这样就能避免很多不必要的数据竞争。

![img](https://static001.geekbang.org/resource/image/e7/31/e75988bd5a79652d8325ca63fcd55131.png)





模拟MapReduce统计单词数量

我们可以先用二分法递归地将一个文件拆分成更小的文件，直到文件里只有一行数据，然后统计这一行数据里单词的数量，最后再逐级汇总结果。

```java
static void main(String[] args){
  String[] fc = {"hello world",
          "hello me",
          "hello fork",
          "hello join",
          "fork join in world"};
  // 创建 ForkJoin 线程池    
  ForkJoinPool fjp = 
      new ForkJoinPool(3);
  // 创建任务    
  MR mr = new MR(
      fc, 0, fc.length);  
  // 启动任务    
  Map<String, Long> result = 
      fjp.invoke(mr);
  // 输出结果    
  result.forEach((k, v)->
    System.out.println(k+":"+v));
}
//MR 模拟类
static class MR extends 
  RecursiveTask<Map<String, Long>> {
  private String[] fc;
  private int start, end;
  // 构造函数
  MR(String[] fc, int fr, int to){
    this.fc = fc;
    this.start = fr;
    this.end = to;
  }
  @Override protected 
  Map<String, Long> compute(){
    if (end - start == 1) {
      return calc(fc[start]);
    } else {
      int mid = (start+end)/2;
      MR mr1 = new MR(
          fc, start, mid);
      mr1.fork();
      MR mr2 = new MR(
          fc, mid, end);
      // 计算子任务，并返回合并的结果    
      return merge(mr2.compute(),
          mr1.join());
    }
  }
  // 合并结果
  private Map<String, Long> merge(
      Map<String, Long> r1, 
      Map<String, Long> r2) {
    Map<String, Long> result = 
        new HashMap<>();
    result.putAll(r1);
    // 合并结果
    r2.forEach((k, v) -> {
      Long c = result.get(k);
      if (c != null)
        result.put(k, c+v);
      else 
        result.put(k, v);
    });
    return result;
  }
  // 统计单词数量
  private Map<String, Long> 
      calc(String line) {
    Map<String, Long> result =
        new HashMap<>();
    // 分割单词    
    String [] words = 
        line.split("\\s+");
    // 统计单词数量    
    for (String w : words) {
      Long v = result.get(w);
      if (v != null) 
        result.put(w, v+1);
      else
        result.put(w, 1L);
    }
    return result;
  }
}

```

