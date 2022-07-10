## 信号量模型

**一个计数器，一个等待队列，三个方法。**

在信号量模型里，计数器和等待队列对外是透明，所以只能通过信号量模型提供的三个方法访问，init(),down(),up()--这些方法都是原子性的。

init():**设置计数器的初始值。**

down():**计数器的值减1；如果此时计数器的值小于0，则当前线程将被阻塞，否则当前线程可以继续执行。**

up():**计数器的值加1；如果此时计数器的值小于等于0，则唤醒等待队列中的一个线程，并将其从等待队列中移除。**

```java
class Semaphore{
  // 计数器
  int count;
  // 等待队列
  Queue queue;
  // 初始化操作
  Semaphore(int c){
    this.count=c;
  }
  // 
  void down(){
    this.count--;
    if(this.count<0){
      // 将当前线程插入等待队列
      // 阻塞当前线程
    }
  }
  void up(){
    this.count++;
    if(this.count<=0) {
      // 移除等待队列中的某个线程 T
      // 唤醒线程 T
    }
  }
}

```

## 如何使用信号量

利用累加的例子来说明信号量的使用。

在累加器的例子里面，count+=1操作是个临界区，只允许一个线程执行，也就是说要保证互斥。

我们只需要在进入临界区之前执行以下down()操作，退出临界区之前执行以下up()操作就可以了。



```java
static int count;
// 初始化信号量
static final Semaphore s 
    = new Semaphore(1);
// 用信号量保证互斥    
static void addOne() {
  s.acquire();
  try {
    count+=1;
  } finally {
    s.release();
  }
}

```

假设T1和T2同时访问addOne()方法，当他们同时调用acquire()的时候，由于acquire()操作具有原子性，所以只能有一个线程（T1）访问，把信号量里的计数器减为0；另外一个线程T2则将计数器减为-1,。对于T1，信号量里面的计数器是0，所以线程T1会继续执行；T2里的计数器是-1，小于0，T2线程将被阻塞。所有只有T1会进入临界区执行count+=1;

T1执行release()操作，信号量里的计数器由-1变为0，所有等待队列中的T2将被唤醒，于是T2在T1执行完临界区代码之后才有了进入临界区执行的机会，从而保证了互斥性。

## 限流器

semaphore其中一个功能是lock不容易实现的，那就是：**semaphore可以允许多个线程访问同一个临界区。**

比较常见的需求就是我们工作中遇到各种池化资源，例如连接池，对象池，线程池等等。其中，最熟悉的可能是数据库连接池，在同一时刻，一定是允许多个线程同时使用连接池的，当然，每个链接在被释放前，是不允许其他线程使用的。

对象池：一次性创建出N个对象，之后所有的线程重复利用这N个对象，对象在被释放前，也是不允许其他线程使用的。对象池，可以用List保存实例对象。



```java
class ObjPool<T, R> {
  final List<T> pool;
  // 用信号量实现限流器
  final Semaphore sem;
  // 构造函数
  ObjPool(int size, T t){
    pool = new Vector<T>(){};
    for(int i=0; i<size; i++){
      pool.add(t);
    }
    sem = new Semaphore(size);
  }
  // 利用对象池的对象，调用 func 限流
  R exec(Function<T,R> func) {
    T t = null;
    sem.acquire();
    try {
      t = pool.remove(0);
      return func.apply(t);
    } finally {
      pool.add(t);
      sem.release();
    }
  }
}
// 创建对象池
ObjPool<Long, String> pool = 
  new ObjPool<Long, String>(10, 2);
// 通过对象池获取 t，之后执行  
pool.exec(t -> {
    System.out.println(t);
    return t.toString();
});

```

