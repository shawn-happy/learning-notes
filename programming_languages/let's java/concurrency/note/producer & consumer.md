## 优点

1. 解耦
2. 支持异步，并且能够平衡生产者和消费者的速度差异
3. 任务队列：平衡生产者和消费者的速度差异

## 支持批量执行以提升性能

```java
// 任务队列
BlockingQueue<Task> bq=new
  LinkedBlockingQueue<>(2000);
// 启动 5 个消费者线程
// 执行批量任务  
void start() {
  ExecutorService es=xecutors
    .newFixedThreadPool(5);
  for (int i=0; i<5; i++) {
    es.execute(()->{
      try {
        while (true) {
          // 获取批量任务
          List<Task> ts=pollTasks();
          // 执行批量任务
          execTasks(ts);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }
}
// 从任务队列中获取批量任务
List<Task> pollTasks() 
    throws InterruptedException{
  List<Task> ts=new LinkedList<>();
  // 阻塞式获取一条任务
  Task t = bq.take();
  while (t != null) {
    ts.add(t);
    // 非阻塞式获取一条任务
    t = bq.poll();
  }
  return ts;
}
// 批量执行任务
execTasks(List<Task> ts) {
  // 省略具体代码无数
}

```

## 支持分阶段提交以提升性能

写文件如果同步刷新磁盘性能可能会很慢，所以对于不是很重要的数据，我们可以异步刷盘的方式。

1. Error级别的日志需要立即刷盘；
2. 数据积累到500需要立即刷盘；
3. 存在为刷盘数据，且5S内未曾刷盘，需要立即刷盘；

这个日志组件的异步刷盘操作本质上其实就是一种分阶段提交。



```java
class Logger {
  // 任务队列  
  final BlockingQueue<LogMsg> bq
    = new BlockingQueue<>();
  //flush 批量  
  static final int batchSize=500;
  // 只需要一个线程写日志
  ExecutorService es = 
    Executors.newFixedThreadPool(1);
  // 启动写日志线程
  void start(){
    File file=File.createTempFile(
      "foo", ".log");
    final FileWriter writer=
      new FileWriter(file);
    this.es.execute(()->{
      try {
        // 未刷盘日志数量
        int curIdx = 0;
        long preFT=System.currentTimeMillis();
        while (true) {
          LogMsg log = bq.poll(
            5, TimeUnit.SECONDS);
          // 写日志
          if (log != null) {
            writer.write(log.toString());
            ++curIdx;
          }
          // 如果不存在未刷盘数据，则无需刷盘
          if (curIdx <= 0) {
            continue;
          }
          // 根据规则刷盘
          if (log!=null && log.level==LEVEL.ERROR ||
              curIdx == batchSize ||
              System.currentTimeMillis()-preFT>5000){
            writer.flush();
            curIdx = 0;
            preFT=System.currentTimeMillis();
          }
        }
      }catch(Exception e){
        e.printStackTrace();
      } finally {
        try {
          writer.flush();
          writer.close();
        }catch(IOException e){
          e.printStackTrace();
        }
      }
    });  
  }
  // 写 INFO 级别日志
  void info(String msg) {
    bq.put(new LogMsg(
      LEVEL.INFO, msg));
  }
  // 写 ERROR 级别日志
  void error(String msg) {
    bq.put(new LogMsg(
      LEVEL.ERROR, msg));
  }
}
// 日志级别
enum LEVEL {
  INFO, ERROR
}
class LogMsg {
  LEVEL level;
  String msg;
  // 省略构造函数实现
  LogMsg(LEVEL lvl, String msg){}
  // 省略 toString() 实现
  String toString(){}
}

```

