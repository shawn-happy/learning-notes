**解决并发编程问题，首要问题也是解决宏观的分工问题**

## Thread-Per-Message模式

现实世界里，很多事情我们需要委托他人办理，一方面受限于我们的能力，总有很多搞不定的事，比如教育小朋友，搞不定怎么办呢？只能委托学校老师了，另一方面受限于我们的时间，比如忙着写Bug，哪有时间买房子呢？只能委托房产中介了，委托他人代办有一个非常大的好处，那就是可以专心做自己的事了。

比如写一个HTTP Server，很显然只能在主线程中接收请求，而不能处理HTTP请求，因为如果在主线程中处理HTTP请求的话；那同一时间只能处理一个请求，太慢了！怎么办呢？可以利用代办的思路，创建一个子线程，委托子线程去处理HTTP请求。

Thread-Per-Message模式：简言之就是为每个任务分配一个独立的线程。这是一种最简单的分工方法，实现起来也非常简单。

## Thread实现Thread-Per-Message模式

Thread-Per-Message模式的一个最经典的应用场景是网络编程里服务端的实现，服务端为每个客户端请求创建一个独立的线程，当线程处理完请求后，自动销毁，这是一种最简单的并发处理网络请求的方法。

网络编程里最简单的程序当数echo程序了，echo程序的服务端会原封不动地客户端的请求发送回客户端。例如，客户端发送TCP请求“Hello World”，那么服务端也会返回“Hello World”。

下面我们就以echo程序的服务端为例，介绍如何实现Thread-Per-Message模式。

```java
final ServerSocketChannel ssc = 
  ServerSocketChannel.open().bind(
    new InetSocketAddress(8080));
// 处理请求    
try {
  while (true) {
    // 接收请求
    SocketChannel sc = ssc.accept();
    // 每个请求都创建一个线程
    new Thread(()->{
      try {
        // 读 Socket
        ByteBuffer rb = ByteBuffer
          .allocateDirect(1024);
        sc.read(rb);
        // 模拟处理请求
        Thread.sleep(2000);
        // 写 Socket
        ByteBuffer wb = 
          (ByteBuffer)rb.flip();
        sc.write(wb);
        // 关闭 Socket
        sc.close();
      }catch(Exception e){
        throw new UncheckedIOException(e);
      }
    }).start();
  }
} finally {
  ssc.close();
}   

```

问题：

上面这个echo服务的实现方案是不具备可行性的。原因在于java中的线程是一个重量级的对象，创建成本很高，一方面创建线程比较耗时，另一方面线程占用的内存也比较大。所以，为每个请求创建一个新的线程并不适合高并发场景。

## Fiber实现Thread-Per-Message模式

```java
final ServerSocketChannel ssc = 
  ServerSocketChannel.open().bind(
    new InetSocketAddress(8080));
// 处理请求    
try {
  while (true) {
    // 接收请求
    SocketChannel sc = ssc.accept();
    // 每个请求都创建一个线程
    new Thread(()->{
      try {
        // 读 Socket
        ByteBuffer rb = ByteBuffer
          .allocateDirect(1024);
        sc.read(rb);
        // 模拟处理请求
        Thread.sleep(2000);
        // 写 Socket
        ByteBuffer wb = 
          (ByteBuffer)rb.flip();
        sc.write(wb);
        // 关闭 Socket
        sc.close();
      }catch(Exception e){
        throw new UncheckedIOException(e);
      }
    }).start();
  }
} finally {
  ssc.close();
}   

```

