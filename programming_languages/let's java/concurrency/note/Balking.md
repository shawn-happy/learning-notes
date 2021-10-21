Guarded Suspension模式是多线程版本的if。是需要等待的，而且还很执着，必须等到条件为真，但是并不是所有的场景都需要这样，有时候我们需要快速放弃

需要快速放弃的一个最常见的例子是各种编辑器提供的自动保存功能。自动保存功能的实现逻辑一般是隔一段时间自动执行存盘操作，存盘操作的前提是文件做过修改，如果文件没有执行过修改操作，就需要快速放弃存盘。

代码：

```java
class AutoSaveEditor{
  // 文件是否被修改过
  boolean changed=false;
  // 定时任务线程池
  ScheduledExecutorService ses = 
    Executors.newSingleThreadScheduledExecutor();
  // 定时执行自动保存
  void startAutoSave(){
    ses.scheduleWithFixedDelay(()->{
      autoSave();
    }, 5, 5, TimeUnit.SECONDS);  
  }

// 自动存盘操作
void autoSave(){
  synchronized(this){
    if (!changed) {
      return;
    }
    changed = false;
  }
  // 执行存盘操作
  // 省略且实现
  this.execSave();
}
// 编辑操作
void edit(){
  // 省略编辑逻辑
  ......
  change();
}
// 改变状态
void change(){
  synchronized(this){
    changed = true;
  }
}


}

```



在RPC框架中，本地路由表示要和注册中心进行信息同步的，应用启动的时候，会将应用依赖服务的路由表从注册中心同步到本地路由表中，如果应用重启的时候注册中心宕机，那么会导致该应用依赖服务均不可用，因为找不到依赖服务的路由表，为了防止这种极端情况出现，PRC框架可以将本地路由表自动保存到本地文件中，如果重启的时候注册中心宕机，那么就会从本地文件中恢复重启前的路由表。

```java
// 路由表信息
public class RouterTable {
  //Key: 接口名
  //Value: 路由集合
  ConcurrentHashMap<String, CopyOnWriteArraySet<Router>> 
    rt = new ConcurrentHashMap<>();    
  // 路由表是否发生变化
  volatile boolean changed;
  // 将路由表写入本地文件的线程池
  ScheduledExecutorService ses=
    Executors.newSingleThreadScheduledExecutor();
  // 启动定时任务
  // 将变更后的路由表写入本地文件
  public void startLocalSaver(){
    ses.scheduleWithFixedDelay(()->{
      autoSave();
    }, 1, 1, MINUTES);
  }
  // 保存路由表到本地文件
  void autoSave() {
    if (!changed) {
      return;
    }
    changed = false;
    // 将路由表写入本地文件
    // 省略其方法实现
    this.save2Local();
  }
  // 删除路由
  public void remove(Router router) {
    Set<Router> set=rt.get(router.iface);
    if (set != null) {
      set.remove(router);
      // 路由表已发生变化
      changed = true;
    }
  }
  // 增加路由
  public void add(Router router) {
    Set<Router> set = rt.computeIfAbsent(
      route.iface, r -> 
        new CopyOnWriteArraySet<>());
    set.add(router);
    // 路由表已发生变化
    changed = true;
  }
}

```

单次初始化：

```java
class InitTest{
  boolean inited = false;
  synchronized void init(){
    if(inited){
      return;
    }
    // 省略 doInit 的实现
    doInit();
    inited=true;
  }
}

```

单例模式（单次初始化的一种应用）

```java
class Singleton{
  private static
    Singleton singleton;
  // 构造方法私有化  
  private Singleton(){}
  // 获取实例（单例）
  public synchronized static 
  Singleton getInstance(){
    if(singleton == null){
      singleton=new Singleton();
    }
    return singleton;
  }
}

```

双重检查

```java
class Singleton{
  private static volatile 
    Singleton singleton;
  // 构造方法私有化  
  private Singleton() {}
  // 获取实例（单例）
  public static Singleton 
  getInstance() {
    // 第一次检查
    if(singleton==null){
      synchronize{Singleton.class){
        // 获取锁后二次检查
        if(singleton==null){
          singleton=new Singleton();
        }
      }
    }
    return singleton;
  }
}

```

