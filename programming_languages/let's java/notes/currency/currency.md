# Java并发编程

## 进程，线程，协程

### 进程
进程指计算机中已运⾏的程序。进程为曾经是分时系统的基本运作单位。在⾯向进程设计的系统中，进程是程序的基本执⾏实体；在⾯向线程设计的系统中，进程本身不是基本运⾏单位，⽽是线程的容器。程序本身只是指令、数据及其组织形式的描述，进程才是程序的真正运⾏实例。
[Code Exampe](https://www.geeksforgeeks.org/fork-system-call/)

#### Java进程管理

### 线程
操作系统能够进⾏运算调度的最⼩单位。它被包含在进程之中，是进程中的实际运作单位。⼀条线程指的是进程中⼀个单⼀顺序的控制流，⼀个进程中可以并发多个线程，每条线程并⾏执⾏不同的任务。在Unix System V及SunOS中也被称为轻量进程，但轻量进程更多指内核线程，⽽把⽤户线程称为线程。

#### Java线程
* GreenThread, java1.2之前的java Thread实现，是模拟多线程并发。
* Native Os Thread, Java 1.2 之后 Java Thread 实现，基于 OS 线程实现，数量映射为 1:1。

##### Java线程编程模型
* < Java 5：Thread、Runnable
* Java 5：Executor、Future、Callable
* Java 7：ForkJoin
* Java 8：CompletionStage、CompletableFuture
* Java 9：Flow(Publisher、Subscriber、Subscription、Processor)
Code Example:
```java
```
##### Java线程状态
* NEW：线程已创建 ，尚未启动
* RUNNABLE：表示线程处于可运⾏状态，不代表⼀定运⾏
* BLOCKED：被 Monitor 锁阻塞，表示当前线程在同步锁的场景运作
* WAITTING：线程处于等待状态，由 Object#wait()、Thread#join() 或 LockSupport#park() 引起
* TIMED_WAITTING：线程处于规定时间内的等待状态
* TERMINATED：线程执⾏结束

##### Java线程生命周期

##### Java线程通讯

##### Java线程池
* < Java 5：⾃定义 Thread Pool
* Java 5+：ExecutorService
	- ThreadPoolExecutor
	- ScheduledThreadPoolExecutor
* Java 7+：ForkJoinPool

##### Java并发框架
* Java 5：Java Util Concurrent
* Java 7：Fork/Join
* Java 8：CompletableFuture、RxJava、Reactor
* Java 9：Flow API、Reactive Streams

#### POSIX线程
POSIX线程（英语：POSIX Threads，常被缩写為Pthreads）是POSIX的线程标准，定义了创建和操纵线程的⼀套API。实现POSIX线程标准的库常被称作Pthreads，⼀般⽤于Unix-like POSIX 系统，如Linux、Solaris。但是Microsoft Windows上的实现也存在，例如直接使⽤Windows API实现的第三⽅库pthreads-w32；⽽利⽤Windows的SFUSUA⼦系统，则可以使⽤微软提供的⼀部分原⽣POSIX API - https://sourceware.org/pthreads-win32/

### 协程
是计算机程序的一类组件，推广了协作式多任务的子例程，允许执行被挂起与被恢复。相对子例程而言，协程更为一般和灵活，但在实践中使用没有子例程那样广泛。协程更适合于用来实现彼此熟悉的程序组件，如协作式多任务、异常处理、事件循环、迭代器、无限列表和管道。

## 同步，异步，阻塞
同步：最常⻅的编程⼿段，是指任务发起⽅和执⾏⽅在同⼀线程中完成。
异步：常⻅的提升吞吐⼿段，是指任务发起⽅和执⾏⽅在不同线程中完成。
阻塞：⼀种编程模型，由通知状态被动的回调执⾏，同步或异步执⾏均可。

