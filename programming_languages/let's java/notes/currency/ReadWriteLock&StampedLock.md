## 应用场景

读多写少

## 什么是读写锁

三条基本原则：

1. 允许多个线程同时读共享变量。
2. 只允许一个线程写共享变量。
3. 如果一个写线程正在执行写操作，此时禁止读线程读共享变量。

读写锁与互斥锁一个重要区别就是**读写锁允许多个线程同时读共享变量**，而互斥锁是不允许的。但是读写锁的写操作是互斥的。

## 快速实现一个缓存

1. 声明了一个Cache<K,V>类，其中K表示key的类型，V表示value的类型。
2. 内部维护一个HashMap，由于HashMap是线程不安全，我们用ReadWriteLock来保证线程安全。
3. ReadWriteLock是一个接口，实现类是ReentrantReadWriteLock。

```java
class Cache<K,V> {
  final Map<K, V> m =
    new HashMap<>();
  final ReadWriteLock rwl =
    new ReentrantReadWriteLock();
  // 读锁
  final Lock r = rwl.readLock();
  // 写锁
  final Lock w = rwl.writeLock();
  // 读缓存
  V get(K key) {
    r.lock();
    try { return m.get(key); }
    finally { r.unlock(); }
  }
  // 写缓存
  V put(String key, Data v) {
    w.lock();
    try { return m.put(key, v); }
    finally { w.unlock(); }
  }
}

```

**缓存首先要解决缓存数据的初始化问题。**

1. 如果元数据的数据量不大，可以一次性加载到内存。
2. 如果元数据的数据量很大，可以采用懒加载。

改进代码如下：

```java
class Cache<K,V> {
  final Map<K, V> m =
    new HashMap<>();
  final ReadWriteLock rwl = 
    new ReentrantReadWriteLock();
  final Lock r = rwl.readLock();
  final Lock w = rwl.writeLock();
 
  V get(K key) {
    V v = null;
    // 读缓存
    r.lock();         
    try {
      v = m.get(key); 
    } finally{
      r.unlock();     
    }
    // 缓存中存在，返回
    if(v != null) {   
      return v;
    }  
    // 缓存中不存在，查询数据库
    w.lock();         
    try {
      // 再次验证
      // 其他线程可能已经查询过数据库
      v = m.get(key); 
      if(v == null){  
        // 查询数据库
        v= 省略代码无数
        m.put(key, v);
      }
    } finally{
      w.unlock();
    }
    return v; 
  }
}

```

## 读写锁的升级与降级

```java
// 读缓存
r.lock();         
try {
  v = m.get(key); 
  if (v == null) {
    w.lock();
    try {
      // 再次验证并更新缓存
      // 省略详细代码
    } finally{
      w.unlock();
    }
  }
} finally{
  r.unlock();     
}

```

先获取读锁，在升级为写锁--->锁的升级。

ReadWriteLock并不支持这种升级，所以上述代码，读锁还没有释放，此时获取写锁，会导致写锁永久等待，最终导致相关线程都被阻塞，永远也没有机会被唤醒。

锁的升级不允许，但是降级是允许的，以下是ReadWriteLock的官方示例

```java
class CachedData {
  Object data;
  volatile boolean cacheValid;
  final ReadWriteLock rwl =
    new ReentrantReadWriteLock();
  // 读锁  
  final Lock r = rwl.readLock();
  // 写锁
  final Lock w = rwl.writeLock();
  
  void processCachedData() {
    // 获取读锁
    r.lock();
    if (!cacheValid) {
      // 释放读锁，因为不允许读锁的升级
      r.unlock();
      // 获取写锁
      w.lock();
      try {
        // 再次检查状态  
        if (!cacheValid) {
          data = ...
          cacheValid = true;
        }
        // 释放写锁前，降级为读锁
        // 降级是可以的
        r.lock(); ①
      } finally {
        // 释放写锁
        w.unlock(); 
      }
    }
    // 此处仍然持有读锁
    try {use(data);} 
    finally {r.unlock();}
  }
}

```

## StampedLock

ReadWriteLock支持两种模式：读锁，写锁

StampedLock支持三种模式：写锁，悲观读锁，乐观读。

其中StampedLock的写锁和悲观读的语义与ReadWriteLock的写锁，读锁类似。

不同的是：StampedLock的写锁和悲观锁加锁成功后，都会返回一个stamp，然后解锁的时候，需要传入这个stamp。

```java
final StampedLock sl = 
  new StampedLock();
  
// 获取 / 释放悲观读锁示意代码
long stamp = sl.readLock();
try {
  // 省略业务相关代码
} finally {
  sl.unlockRead(stamp);
}

// 获取 / 释放写锁示意代码
long stamp = sl.writeLock();
try {
  // 省略业务相关代码
} finally {
  sl.unlockWrite(stamp);
}

```

StampedLock的乐观读，是允许一个线程获取写锁的，并不是所有的写操作都会被阻塞。

我们用的是乐观读，而不是乐观读锁，乐观读这个操作是无锁的。相比readwritelock的读锁，乐观读的性能要更好一些。

官方案例：

```java
class Point {
  private int x, y;
  final StampedLock sl = 
    new StampedLock();
  // 计算到原点的距离  
  int distanceFromOrigin() {
    // 乐观读
    long stamp = 
      sl.tryOptimisticRead();
    // 读入局部变量，
    // 读的过程数据可能被修改
    int curX = x, curY = y;
    // 判断执行读操作期间，
    // 是否存在写操作，如果存在，
    // 则 sl.validate 返回 false
    if (!sl.validate(stamp)){
      // 升级为悲观读锁
      stamp = sl.readLock();
      try {
        curX = x;
        curY = y;
      } finally {
        // 释放悲观读锁
        sl.unlockRead(stamp);
      }
    }
    return Math.sqrt(
      curX * curX + curY * curY);
  }
}

```

如果存在写操作，会把乐观读升级为悲观读锁，这个做法是合理的，否则就需要在一个循环里执行乐观读，浪费大量的CPU，直到执行乐观读操作的期间没有写操作（这样才能保证X,Y的正确性和一致性），升级为悲观读锁，代码简练并且不易出错。

## 乐观锁

数据库乐观锁场景案例：在ERP的生产模块中，会有多个人通过ERP系统提供的UI同时修改同一条生产订单，那如何保证生产订单数据是并发安全的呢？

乐观锁的实现很简单：在生产订单表product_doc里增加一个数值型版本号字段version，每次更新product_doc，version+1，生产订单的UI在展示的时候，需要查询数据库。此时将这个VERSION字段和其他业务字段一起返回给生产订单UI

```sql
select id,version from product_doc where id = 777;
```

更新

```sql
update product_doc set version = version + 1... where id = 777 and version = 9;
```

如果这条SQL语句执行成功并且成功返回的条数=1，那么说明从生产订单UI执行查询操作到执行保存操作的期间，没有其他人修改过这条数据。因为如果这期间有其他人修改过这条数据，version > 9;

## StampedLock使用注意事项

1. StampedLock的功能仅仅是ReadWriteLock的子集。

2. StampedLock在命名上没有增加Reentrant，StampedLock不支持冲入。

3. StampedLock的悲观读锁，写锁都不支持条件变量。

4. 如果线程阻塞在StampedLock的readLock()或者writeLock()上时，如果此时调用该阻塞线程的interrupt()方法，会导致CPU飙升。

   代码示例：

   ```java
   final StampedLock lock
     = new StampedLock();
   Thread T1 = new Thread(()->{
     // 获取写锁
     lock.writeLock();
     // 永远阻塞在此处，不释放写锁
     LockSupport.park();
   });
   T1.start();
   // 保证 T1 获取写锁
   Thread.sleep(100);
   Thread T2 = new Thread(()->
     // 阻塞在悲观读锁
     lock.readLock()
   );
   T2.start();
   // 保证 T2 阻塞在读锁
   Thread.sleep(100);
   // 中断线程 T2
   // 会导致线程 T2 所在 CPU 飙升
   T2.interrupt();
   T2.join();
   
   ```

   **使用StampedLock一定不要调用中断操作，如果需要支持中断操作，一定使用可中断悲观读锁readLockInterruptibly()，writeLockInterruptibly()**

## 模板

```java
final StampedLock sl = 
  new StampedLock();

// 乐观读
long stamp = 
  sl.tryOptimisticRead();
// 读入方法局部变量
......
// 校验 stamp
if (!sl.validate(stamp)){
  // 升级为悲观读锁
  stamp = sl.readLock();
  try {
    // 读入方法局部变量
    .....
  } finally {
    // 释放悲观读锁
    sl.unlockRead(stamp);
  }
}
// 使用方法局部变量执行业务操作
......

    
    
    long stamp = sl.writeLock();
try {
  // 写共享变量
  ......
} finally {
  sl.unlockWrite(stamp);
}

```

