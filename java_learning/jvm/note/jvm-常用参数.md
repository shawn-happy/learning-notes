---
typora-root-url: image
---

# JVM参数

| 参数                         | 作用                                                         |
| ---------------------------- | ------------------------------------------------------------ |
| `-XX:+PrintGC`               | 输出GC日志                                                   |
| `-XX:+PrintGCDetails`        | 输出GC详细日志                                               |
| `-XX:+PrintGCTimeStamps`     | 输出GC的时间戳（以基准时间的格式）                           |
| `-XX:+PrintGCDateStamps`     | 输出GC的时间戳（以日期格式）                                 |
| `-XX:+PrintHeapAtGC`         | 在进行GC的前后打印出堆的信息                                 |
| `-Xloggc:../logs/gc.log`     | 日志文件的输出路径                                           |
| `-XX:NewRatio`               | 年轻代，老年代默认比列是1:2，可以通过此参数修改              |
| `-XX:SurvivorRatio`          | 8:1:1 ，可以通过此参数修改                                   |
| `-XX:+UseAdaptiveSizePolicy` | jvm动态调整java堆各个区域大小，以及进入老年代的年龄，<br />上述两个参数将会失效。jdk8是默认开启的 |
|                              |                                                              |





# jdk命令行工具

```bash
java -Xint -version          # 解释执行
java -Xcomp -version         # 第一次使用就编译成本地的代码
java -Xmixed -version        # 混合模式（Java默认）
```

![jvm-编译方式](/jvm-编译方式.png)



| 名称   | 主要作用                    |
| ------ | --------------------------- |
| jps    | 显示指定系统内所有的jvm进程 |
| jstat  | jvm统计信息见识工具         |
| jinfo  | java配置信息工具            |
| jmap   | java内存映像工具            |
| jhat   | jvm heap转储快照分析工具    |
| jstack | java堆栈跟踪工具            |



## jps

| options | 作用                                        |
| ------- | ------------------------------------------- |
| -q      | 只输出LVMID，省略主类的名称                 |
| -m      | 输出jvm进程启动时传递给主类main()函数的参数 |
| -l      | 输出主类的全名，如果是jar，输出jar路径      |
| -v      | 输出jvm进程启动时jvm参数                    |

## jstat

| options           | 作用                                                         |
| ----------------- | ------------------------------------------------------------ |
| -class            | 监视类装载，卸载数量，总空间以及类装载所耗费的时间           |
| -gc               | java堆（Eden,from/to survivor,old,permgen）容量，已用空间，GC时间合计 |
| -gccapacity       | 同-gc，输出主要关注java堆各个区域使用到的最大和最小空间      |
| -gcutil           | 同-gc，主要关注已使用空间占总空间的百分比                    |
| -gccause          | 同-gcutil，额外输出上一次GC产生的原因                        |
| -gcnew            | Young gc                                                     |
| -gcnewcapacity    | 同-gcnew，主要关注使用到的最大和最小空间                     |
| -gcold            | Old gc                                                       |
| -gcoldcapacity    | 同-gcold，主要关注使用到的最大和最小空间                     |
| -gcpermcapacity   | 主要关注permgen使用到的最大和最小空间                        |
| -compiler         | JIT编译器编译过的方法，耗时等信息                            |
| -printcompilation | 已经被jit编译的方法                                          |

## jinfo

```bash
jinfo -flag
```

## jmap

同`-XX:+HeapDumpOnOutOfMemoryError`参数

| options        | 作用                                                         |
| -------------- | ------------------------------------------------------------ |
| -dump          | 生成java堆转储快照。格式为：`-dump:[live,]format=b,file=<filename>`,其中live子参数说明是否只dump出存活的对象 |
| -finalizerinfo | finalize方法的对象，只在Linux、Solaris平台下有效             |
| -heap          | 显示java堆详细信息，使用哪种回收器，参数配置，分代情况等。Linux |
| -permstat      | 以ClassLoader为统计口径显示永久代内存状态。Linux             |
| -histo         | 显示堆中对象统计信息，包括类，实例数量和合计容量             |
| -F             | 当虚拟机进程对-dump选项没有响应时，可使用这个选项强制生成dump快照。linux |

## jstat

| options | 作用                                       |
| ------- | ------------------------------------------ |
| -F      | 当正常输出请求不被响应时，强制输出线程堆栈 |
| -m      | 如果调用本地方法，可以显示c/c++的堆栈      |
| -l      | 除堆栈外，显示关于锁的附加信息             |

# jdk可视化工具

## JConsole

## VisualVM

# Jprofiler

> Jprofiler 插件

一款性能瓶颈分析插件



> 安装 Jprofiler

1、IDEA安装 JProfiler 插件

2、window上安装 JProfiler （无脑下一步即可：注意路径中不能有中文和空格）

3、激活

```
注册码仅供大家参考

L-Larry_Lau@163.com#23874-hrwpdp1sh1wrn#0620
L-Larry_Lau@163.com#36573-fdkscp15axjj6#25257
L-Larry_Lau@163.com#5481-ucjn4a16rvd98#6038
L-Larry_Lau@163.com#99016-hli5ay1ylizjj#27215
L-Larry_Lau@163.com#40775-3wle0g1uin5c1#0674
```

4、在IDEA 中绑定 JProfiler 

> 快速体验

```java
package com.coding.oom;

import java.util.ArrayList;
import java.util.List;

// -Xmx10m -Xms10m -XX:+HeapDumpOnOutOfMemoryError
public class Demo03 {

    byte[] bytes = new byte[1*1024*1024]; // 1M

    public static void main(String[] args) throws InterruptedException {
        // 泛型：约束！
        List<Demo03> list = new ArrayList<Demo03>();

        int count = 0;

        try {
            // Error
            while (true){
                list.add(new Demo03());
                count = count + 1;
            }
        } catch (Throwable e) { // Throwable 或者 Error
            System.out.println("count="+count);
            e.printStackTrace();
        }
    }
}
```

分析dump出来的快照，查看异常对象；分析定位到具体的类和代码问题！