# CompletionService简介

在学习[future](./future.md)的时候，我们提到，`future.get()`方法会阻塞线程，所以如果A,B,C三个线程同时获取执行结果，如果A先执行，但是A的执行时间很长，那么即使B,C执行很短，也无法获取到B,C的执行结果，因为主线程阻塞在`A.get()`上了。

```java
ExecutorService executorService = Executors.newFixedThreadPool(4);
List<Future> futures = new ArrayList<Future<Integer>>();
futures.add(executorService.submit(A));
futures.add(executorService.submit(B));
futures.add(executorService.submit(C));

// 遍历 Future list，通过 get() 方法获取每个 future 结果
for (Future future:futures) {
	Integer result = future.get();
	// 其他业务逻辑 如果A执行时间很长，阻塞
}
```

那么如何让B,C也有机会能够获取到执行结果呢？答案就是`java.util.concurrent.CompletionService`。

`CompletionService`是Java8的新增接口，JDK为其提供了一个实现类`ExecutorCompletionService`。这个类是为线程池中`Task`的执行结果服务的，即为`Executor`中`Task`返回`Future`而服务的。`CompletionService`的实现目标是**任务先完成可优先获取到，即结果按照完成先后顺序排序。**

```java
ExecutorService executorService = Executors.newFixedThreadPool(4);

// ExecutorCompletionService 是 CompletionService 唯一实现类
CompletionService completionService = new ExecutorCompletionService<>(executorService );

List<Future> futures = new ArrayList<Future<Integer>>();
futures.add(completionService.submit(A));
futures.add(completionService.submit(B));
futures.add(completionService.submit(C));

// 遍历 Future list，通过 get() 方法获取每个 future 结果
for (int i = 0; i < futures.size(); i++) {
    Integer result = completionService.take().get();
    // 其他业务逻辑
}
```

# CompletionService原理

我们来试想一下，如果是你应该如何解决上述`Feture`带来的阻塞问题呢？可以通过阻塞队列来实现，伪代码如下：

```java
// 创建阻塞队列
BlockingQueue<Integer> bq =
  new LinkedBlockingQueue<>();
// 任务A 异步进入阻塞队列  
executor.execute(() -> bq.put(A.get()));
// 任务B 异步进入阻塞队列  
executor.execute(() -> bq.put(B.get()));
// 任务C 异步进入阻塞队列  
executor.execute(()-> bq.put(C.get()));
  
for (int i = 0; i < 3; i++) {
  Integer r = bq.take();
  // 异步执行所有业务逻辑
  executor.execute(()->action(r));
}
```

实际上`CompletionService`的实现原理也是内部维护了一个阻塞队列，当任务执行结束就把任务的执行结果加入到阻塞队列中，不同的是CompletionService是把任务执行结果的Future对象加入到阻塞队列中。

`CompletionService`是一个接口，`submit()`用于提交任务，`take()和poll()`用于从阻塞队列中获取并移除一个元素，它们的区别在于如果阻塞队列是空的，那么调用`take()`方法的线程就会被阻塞，而`poll()`方法会返回`null`值。

```java
public interface CompletionService<V> {
    Future<V> submit(Callable<V> task);
    Future<V> submit(Runnable task, V result);
    Future<V> take() throws InterruptedException;
    Future<V> poll();
    Future<V> poll(long timeout, TimeUnit unit) throws InterruptedException;
}
```

其实现类`ExecutorCompletionService`，实际上可以看做是`Executor`和 `BlockingQueue`的结合体，`ExecutorCompletionService`把具体的计算任务交给 `Executor`完成，通过`BlockingQueue`的`take()`方法获得任务执行的结果。

`ExecutorCompletionService`有两个构造函数

```java
public ExecutorCompletionService(Executor executor) {
    if (executor == null)
        throw new NullPointerException();
    this.executor = executor;
    // 判断executor是不是ThreadPoolExecutor,ScheduledThreadPoolExecutor,ForkJoinPool
    // 其余框架也有实现了AbstractExecutorService抽象类，目前JDK里只有上述的三种实现
    // 如果不是，则为null
    this.aes = (executor instanceof AbstractExecutorService) ?
        (AbstractExecutorService) executor : null;
    this.completionQueue = new LinkedBlockingQueue<Future<V>>();
}

public ExecutorCompletionService(Executor executor,
                                 BlockingQueue<Future<V>> completionQueue) {
    if (executor == null || completionQueue == null)
        throw new NullPointerException();
    this.executor = executor;
    // 判断executor是不是ThreadPoolExecutor,ScheduledThreadPoolExecutor,ForkJoinPool
    // 其余框架也有实现了AbstractExecutorService抽象类，目前JDK里只有上述的三种实现
    // 如果不是，则为null
    this.aes = (executor instanceof AbstractExecutorService) ?
        (AbstractExecutorService) executor : null;
    this.completionQueue = completionQueue;
}
```

两个构造器都需要传入`Executor`，如果不传`BlockingQueue<Futrue>`，默认会创建一个`LinkedBlockingQueue<Future<V>>`的队列，该`BlockingQueue`的作用是保存`Executor`执行的结果。

`submit()`源码如下：

```java
public Future<V> submit(Callable<V> task) {
    if (task == null) throw new NullPointerException();
    RunnableFuture<V> f = newTaskFor(task);
    executor.execute(new QueueingFuture<V>(f, completionQueue));
    return f;
}

public Future<V> submit(Runnable task, V result) {
    if (task == null) throw new NullPointerException();
    RunnableFuture<V> f = newTaskFor(task, result);
    executor.execute(new QueueingFuture<V>(f, completionQueue));
    return f;
}
```

当提交一个任务到`ExecutorCompletionService`时，首先需要将`task`封装成`RunableFuture<V>`，通过`newTaskFor()`完成，然后再将`RunableFuture`封装成`QueueingFuture`，它是`FutureTask`的一个子类，然后改写`FutureTask`的`done`方法，之后把`Executor`执行的计算结果放入`BlockingQueue`中。

`newTaskFor()`的源码如下：

```java
private RunnableFuture<V> newTaskFor(Callable<V> task) {
    // aes是AbstractExecutorService，其实现类是ThreadPoolExecutor，ForkJoinPool，SchedulerThreadPoolExecutor
    if (aes == null) 
        return new FutureTask<V>(task);
    else
        return aes.newTaskFor(task);
}

private RunnableFuture<V> newTaskFor(Runnable task, V result) {
    if (aes == null)
        return new FutureTask<V>(task, result);
    else
        return aes.newTaskFor(task, result);
}
```

`QueueingFuture`的源码如下：

```java
private static class QueueingFuture<V> extends FutureTask<Void> {
    QueueingFuture(RunnableFuture<V> task,
                   BlockingQueue<Future<V>> completionQueue) {
        super(task, null);
        this.task = task;
        this.completionQueue = completionQueue;
    }
    private final Future<V> task;
    private final BlockingQueue<Future<V>> completionQueue;
    // 会被java.util.concurrent.FutureTask#finishCompletion调用，判读是否计算完成
    // 计算结果放在阻塞队列中
    protected void done() { completionQueue.add(task); }
}
```

`take()`和`poll()`方法如下：

```java
// 从结果队列中获取并移除一个已经执行完成的任务的结果，如果没有就会阻塞，直到有任务完成返回结果。
public Future<V> take() throws InterruptedException {
    return completionQueue.take();
}

// 从结果队列中获取并移除一个已经执行完成的任务的结果，如果没有就会返回null，该方法不会阻塞。
public Future<V> poll() {
    return completionQueue.poll();
}

// 从结果队列中获取并移除一个已经执行完成的任务的结果，如果没有就会返回null，该方法不会阻塞。
// 超时
public Future<V> poll(long timeout, TimeUnit unit)
        throws InterruptedException {
    return completionQueue.poll(timeout, unit);
}
```