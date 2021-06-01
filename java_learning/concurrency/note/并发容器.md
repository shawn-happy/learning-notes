---
typora-root-url: image
---

## 如何将线程不安全的容器变成线程安全的容器

代码示例：

```java
SafeArrayList<T>{
  // 封装 ArrayList
  List<T> c = new ArrayList<>();
  // 控制访问路径
  synchronized
  T get(int idx){
    return c.get(idx);
  }

  synchronized
  void add(int idx, T t) {
    c.add(idx, t);
  }

  synchronized
  boolean addIfNotExist(T t){
    if(!c.contains(t)) {
      c.add(t);
      return true;
    }
    return false;
  }
}

```

SDK:

```java
List list = Collections.
  synchronizedList(new ArrayList());
Set set = Collections.
  synchronizedSet(new HashSet());
Map map = Collections.
  synchronizedMap(new HashMap());

```

**组合操作需要注意竞态条件问题，组合操作往往隐藏着竞态条件问题，即使每个操作都能保证原子性，也不能保证组合操作的原子性。**



在容器领域**一个容易被忽视的“坑”是用迭代器遍历容器。**

代码反例：

```java
List list = Collections.
  synchronizedList(new ArrayList());
Iterator i = list.iterator(); 
// 该处存在并发问题，这些组合操作不具备原子性
while (i.hasNext())
  foo(i.next());

```

正确案例：

```java
List list = Collections.
  synchronizedList(new ArrayList());
synchronized (list) {  // 锁住list再执行遍历操作
  Iterator i = list.iterator(); 
  while (i.hasNext())
    foo(i.next());
}    

```

synchronized修饰的容器被称为同步容器

Vector,Stack,Hashtable。

遍历需要加锁保证互斥。

## 并发容器

![img](/并发容器框架图.png)



### List

CopyOnWriteArrayList,CopyOnWrite,写的时候会将共享变量重新复制一份出来，这样做的好处是读操作完全无锁。

原理：

CopyOnWriteArrayList内部维护了一个数组，成员变量array就指向这个内部数组，所有的读操作都是基于array进行的。如果迭代器遍历array



如果在遍历array的同时，还有写操作，比如新增元素。那么CopyOnWriteArrayList就会将array copy一份，然后在copy之后的数组上执行写操作，执行完，再将array指向这个新的数组。



CopyOnWriteArrayList需要主要两个方面：

1. 应用场景：仅适用于写少读多的场景，并且能够容忍读写短暂不一致。
2. 迭代器是只读的，不支持增删改。因为迭代器遍历的仅仅是一个快照。



### Map

ConcurrentHashMap和ConcurrentSkipListMap

**ConcurrentHashMap的key是无序的，而ConcurrentSkipListMap的key是有序的。key value都不能为空。**

![img](/并发容器Map.png)

### Set

CopyOnWriteArraySet,ConcurrentSkipListSet，参考list,map。

### Queue

两个维度：

1. **阻塞与非阻塞**。阻塞，当队列已满时，入队操作阻塞；当队列已空时，出队操作阻塞。
2. **单端与双端**。单端-->只能队尾入队，队首出队；





四大分类：

1. 单端阻塞队列：

   ArrayBlockingQueue:内部队列是数组

   LinkedBlockingQueue：内部队列是链表

   SynchronousQueue：内部不持有队列，此时生产者线程的入队操作必须等待消费者的出队操作。

   LinkedTransferQueue：LinkedBlockingQueue + SynchronousQueue的功能，性能比LinkedBlockingQueue更好。

   PriorityBlockingQueue：优先级

   DelayQueue：延时。

2. 双端阻塞队列：LinkedBlockingDeque.

3. 单端非阻塞队列：ConcurrentLinkedQueue.

4. 双端非阻塞队列：ConcurrentLinkedDeque.

其中只有ArrayBlockingQueue和LinkedBlockingQueue支持有界。其余都是无界队列。

**在使用无界队列时，一定要充分考虑是否存在OOM的隐患。**