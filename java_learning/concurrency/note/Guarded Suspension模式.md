---
typora-root-url: image
---

## 前言

给MQ发送消息的线程是处理web请求的线程T1，但是消费MQ结构的线程并不是T1，那么线程T1如何等待MQ的返回结果呢？为了便于你理解这个场景，我将其代码化：

```java
class Message{
  String id;
  String content;
}
// 该方法可以发送消息
void send(Message msg){
  // 省略相关代码
}
//MQ 消息返回后会调用该方法
// 该方法的执行线程不同于
// 发送消息的线程
void onMessage(Message msg){
  // 省略相关代码
}
// 处理浏览器发来的请求
Respond handleWebReq(){
  // 创建一消息
  Message msg1 = new 
    Message("1","{...}");
  // 发送消息
  send(msg1);
  // 如何等待 MQ 返回的消息呢？
  String result = ...;
}

```

异步转同步问题

## 类比

项目组团建要外出K歌，我们提前预定一个包间，直接过去，到那儿后大堂经理看了一眼包间，发现服务员正在收拾，就会告诉我们：您预定的包间服务员正在收拾，请您稍等片刻。过了一会儿，大堂经理发现包间已经收拾完了，于是马上带我们去包间就餐。

我们等待包间收拾完的这个过程和等待MQ返回消息本质上是一样的，都是等待一个条件满足：就餐需要等待包间收拾完，等待MQ返回消息。

现实中大堂经理充当着协调的角色，那么程序时间里的大堂经理应该如何设计呢？

Guarded Suspension模式就诞生了。保护性地暂停

Guarded Suspension模式的结构图：

![img](/Guarded Suspension模式的结构图.png)

一个对象GuardedObject内部有一个成员变量——受保护的对象，以及两个成员方法——get(Predicate< T > p)和onChanged(T obj)方法。其中GuardedObject就是我们前面提到的大堂经理的角色。受保护对象就是餐厅里面的包间，get()方法对应的是我们的就餐，就餐的前提条件是包间已经收拾好了，参数p就是用来描述这个前提条件的。受保护对象的onchange方法对应的是服务员吧包间已经收拾好了，通过onchange方法可以fire一个时间，而这个事件往往能改变前提条件p的计算结果。

GuardedObject的内部实现非常简单，是管程一个经典用法。

```java
class GuardedObject<T>{
  // 受保护的对象
  T obj;
  final Lock lock = 
    new ReentrantLock();
  final Condition done =
    lock.newCondition();
  final int timeout=1;
  // 获取受保护对象  
  T get(Predicate<T> p) {
    lock.lock();
    try {
      //MESA 管程推荐写法
      while(!p.test(obj)){
        done.await(timeout, 
          TimeUnit.SECONDS);
      }
    }catch(InterruptedException e){
      throw new RuntimeException(e);
    }finally{
      lock.unlock();
    }
    // 返回非空的受保护对象
    return obj;
  }
  // 事件通知方法
  void onChanged(T obj) {
    lock.lock();
    try {
      this.obj = obj;
      done.signalAll();
    } finally {
      lock.unlock();
    }
  }
}

```

## 扩展

在处理web请求方法handleWebReq()中，可以调用GuardedObject的get方法实现等待，在MQ的消费方法onMessage()中可以调用GuardedObject的onChanged()方法来实现唤醒。

```java
// 处理浏览器发来的请求
Respond handleWebReq(){
  // 创建一消息
  Message msg1 = new 
    Message("1","{...}");
  // 发送消息
  send(msg1);
  // 利用 GuardedObject 实现等待
  GuardedObject<Message> go
    =new GuardObjec<>();
  Message r = go.get(
    t->t != null);
}
void onMessage(Message msg){
  // 如何找到匹配的 go？
  GuardedObject<Message> go=???
  go.onChanged(msg);
}

```

但是在实现的时候会遇到一个问题，handleWebReq()里面创建了GuardedObject对象的实例go，并调用其get()方法等待结果，那么在onMessage()中，如何才能找到匹配的go实例呢？

我们可以委会一个MQ消息id和GuardedObject对象实例的关系。

```java
class GuardedObject<T>{
  // 受保护的对象
  T obj;
  final Lock lock = 
    new ReentrantLock();
  final Condition done =
    lock.newCondition();
  final int timeout=2;
  // 保存所有 GuardedObject
  final static Map<Object, GuardedObject> 
  gos=new ConcurrentHashMap<>();
  // 静态方法创建 GuardedObject
  static <K> GuardedObject 
      create(K key){
    GuardedObject go=new GuardedObject();
    gos.put(key, go);
    return go;
  }
  static <K, T> void 
      fireEvent(K key, T obj){
    GuardedObject go=gos.remove(key);
    if (go != null){
      go.onChanged(obj);
    }
  }
  // 获取受保护对象  
  T get(Predicate<T> p) {
    lock.lock();
    try {
      //MESA 管程推荐写法
      while(!p.test(obj)){
        done.await(timeout, 
          TimeUnit.SECONDS);
      }
    }catch(InterruptedException e){
      throw new RuntimeException(e);
    }finally{
      lock.unlock();
    }
    // 返回非空的受保护对象
    return obj;
  }
  // 事件通知方法
  void onChanged(T obj) {
    lock.lock();
    try {
      this.obj = obj;
      done.signalAll();
    } finally {
      lock.unlock();
    }
  }
}


// 处理浏览器发来的请求
Respond handleWebReq(){
  int id= 序号生成器.get();
  // 创建一消息
  Message msg1 = new 
    Message(id,"{...}");
  // 创建 GuardedObject 实例
  GuardedObject<Message> go=
    GuardedObject.create(id);  
  // 发送消息
  send(msg1);
  // 等待 MQ 消息
  Message r = go.get(
    t->t != null);  
}
void onMessage(Message msg){
  // 唤醒等待的线程
  GuardedObject.fireEvent(
    msg.id, msg);
}

```

