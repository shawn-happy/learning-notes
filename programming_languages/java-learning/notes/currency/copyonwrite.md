String里的replace()方法，并没有去更改原字符串里面value数组的内容，而是将原来的value[]复制一份，然后再新复制的一份数组里进行操作。

这就是copy-on-write原则。写时复制。

## 应用领域

copyonwriteArrayList和CopyOnWriteArraySet这两个容器。

操作系统

unix的操作系统中创建进程的API是fork()，传统的fork()函数会创建父进程的一个完整副本，例如父进程的地址空间现在用到了1G的内存，那么fork()子进程的时候要复制父进程整个进程地址空间（占有1G内存）给子进程，这个过程很耗时。而Linux中fork()子进程的时候，并不复制整个进程的内存空间，而是让父子进程共享桶一个地址空间；只用在父进程或者子进程需要写入的时候才会复制地址空间，从而使父子进程拥有各自的地址空间。

本质上讲，父子进程的地址空间以及数据都是要隔离的，使用COW更多地体现的是一种延时策略，只有在真正需要复制的时候才复制，而不是提前复制好，同时cow还支持按需复制，所以cow在操作系统领域是能够提升性能的。

java中cow容器，由于在修改的同时会复制整个容器，所以在提升读操作性能的同时，是以内存复制为代价的，这里你会发现，同样是应用cow，不同的场景，对性能的英雄也是不同的。

文件系统：Btrfs(B-Tree File System),aufs(advanced multi-layered unification file system)等。

docker容器镜像的设计

git

函数式编程领域，函数式编程的基础是不可变性的，所以函数式编程里面所有的修改都是cow来解决的。

cow可以按需复制





```java
// 路由信息
public final class Router{
  private final String  ip;
  private final Integer port;
  private final String  iface;
  // 构造函数
  public Router(String ip, 
      Integer port, String iface){
    this.ip = ip;
    this.port = port;
    this.iface = iface;
  }
  // 重写 equals 方法
  public boolean equals(Object obj){
    if (obj instanceof Router) {
      Router r = (Router)obj;
      return iface.equals(r.iface) &&
             ip.equals(r.ip) &&
             port.equals(r.port);
    }
    return false;
  }
  public int hashCode() {
    // 省略 hashCode 相关代码
  }
}
// 路由表信息
public class RouterTable {
  //Key: 接口名
  //Value: 路由集合
  ConcurrentHashMap<String, CopyOnWriteArraySet<Router>> 
    rt = new ConcurrentHashMap<>();
  // 根据接口名获取路由表
  public Set<Router> get(String iface){
    return rt.get(iface);
  }
  // 删除路由
  public void remove(Router router) {
    Set<Router> set=rt.get(router.iface);
    if (set != null) {
      set.remove(router);
    }
  }
  // 增加路由
  public void add(Router router) {
    Set<Router> set = rt.computeIfAbsent(
      route.iface, r -> 
        new CopyOnWriteArraySet<>());
    set.add(router);
  }
}

```



