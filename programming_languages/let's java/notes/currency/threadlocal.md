## threadlocal使用方法

下面这个静态类threadid会为每个线程分配一个唯一的线程id，如果一个线程前后两次调用threadid的get()方法,两次get()方法的返回值是相同的，如果是两个线程分别调用ThreadId的get()方法，那么两个线程看到的get()方法的返回值是不同的。

```java
static class ThreadId {
  static final AtomicLong 
  nextId=new AtomicLong(0);
  // 定义 ThreadLocal 变量
  static final ThreadLocal<Long> 
  tl=ThreadLocal.withInitial(
    ()->nextId.getAndIncrement());
  // 此方法会为每个线程分配一个唯一的 Id
  static long get(){
    return tl.get();
  }
}

```

SimpleDateFormat不是线程安全的，我们可以用ThreadLocal来解决。

```java
static class SafeDateFormat {
  // 定义 ThreadLocal 变量
  static final ThreadLocal<DateFormat>
  tl=ThreadLocal.withInitial(
    ()-> new SimpleDateFormat(
      "yyyy-MM-dd HH:mm:ss"));
      
  static DateFormat get(){
    return tl.get();
  }
}
// 不同线程执行下面代码
// 返回的 df 是不同的
DateFormat df =
  SafeDateFormat.get()；

```

## threadlocal工作原理

threadlocal的目标是让不同的线程拥有不同的变量v，那么最直接的方法就是创建一个map，它的key是线程，value是每个线程拥有的变量v，threadlocal内部持有这样一个map就可以了

```java
class MyThreadLocal<T> {
  Map<Thread, T> locals = 
    new ConcurrentHashMap<>();
  // 获取线程变量  
  T get() {
    return locals.get(
      Thread.currentThread());
  }
  // 设置线程变量
  void set(T t) {
    locals.put(
      Thread.currentThread(), t);
  }
}

```

那么java里threadlocal是如何实现的呢？

java的实现也有一个Map，叫做ThreadLocalMap，不过持有这个Map的不是threadlocal，还是Thread。

Thread内部有一个私有属性threadLocals其类型就是ThreadLocalMap，ThreadLocalMap的key是ThreadLocal。

```java
class Thread {
  // 内部持有 ThreadLocalMap
  ThreadLocal.ThreadLocalMap 
    threadLocals;
}
class ThreadLocal<T>{
  public T get() {
    // 首先获取线程持有的
    //ThreadLocalMap
    ThreadLocalMap map =
      Thread.currentThread()
        .threadLocals;
    // 在 ThreadLocalMap 中
    // 查找变量
    Entry e = 
      map.getEntry(this);
    return e.value;  
  }
  static class ThreadLocalMap{
    // 内部是数组而不是 Map
    Entry[] table;
    // 根据 ThreadLocal 查找 Entry
    Entry getEntry(ThreadLocal key){
      // 省略查找逻辑
    }
    //Entry 定义
    static class Entry extends
    WeakReference<ThreadLocal>{
      Object value;
    }
  }
}

```

在java里ThreadLocal仅仅是一个代理工具类，内部并不持有任何与线程相关的数据，所有和线程相关的数据都存储在Thread里。

另外**不容易产生内存泄漏**。在我们的设计方案里，ThreadLocal持有Map会持有Thread对象，这就意味着，只要ThreadLocal对象存在，那么Map中的Thread对象就永远不会被回收。ThreadLocal的生命周期往往都比线程长，所以这种设计方案容易内存泄漏。

java中Thread持有ThreadLocalMap，而且ThreadLocalMap里对ThreadLocal的引用是弱引用（WeakReference），所以只要Thread对象可以被回收，那么ThreadLocalMap就能被回收。

但是还是有可能会发生内存泄漏。

## 内存泄漏

线程池中线程的存活时间太长，往往都是和程序同生共死，这就意味着Thread持有的ThreadLocalMap一直都不会被回收，再加上ThreadLocalMap中的Entry对ThreadLocal是弱引用，所以只要ThreadLocal结束了自己的生命周期是可以被回收的。但是entry中的value是被entry强引用的，所以即使value的生命周期结束了，value也是无法被回收的，导致内存泄漏。

那么在线程池中，我们该如何正确使用ThreadLocal？

JVM不能做到自动释放value的强引用，那么我们就手动释放

try{}finally{}

```java
ExecutorService es;
ThreadLocal tl;
es.execute(()->{
  //ThreadLocal 增加变量
  tl.set(obj);
  try {
    // 省略业务逻辑代码
  }finally {
    // 手动清理 ThreadLocal 
    tl.remove();
  }
});

```

## InheritableThreadLocal

通过ThreadLocal创建的线程变量，其子线程是无法继承的，也就是说你在线程中通过ThreadLocal创建了线程变量V，而后该线程创建了子线程，你在子线程中是无法通过ThreadLocal来访问父线程的线程变量v。

可以用InheritableThreadLocal来完成。和ThreadLocal用法一样

不建议使用

1. 内存泄漏
2. 线程池中线程的创建是动态的，很容易导致继承关系错乱，如果你的业务逻辑依赖InheritableThreadLocal，那么很可能导致业务逻辑计算错误，而这个错误往往比内存泄漏更加致命。

