| 名称   | 作用                                                         |
| ------ | ------------------------------------------------------------ |
| jps    | jvm process status tool,显示指定系统内所有jvm进程            |
| jstat  | jvm statistics monitoring tool,用于收集jvm各方面的运行数据   |
| jinfo  | configuration info for java,用于显示jvm配置信息              |
| jmap   | memory map for java，生成jvm的内存转储快照(heapdump)文件     |
| jhat   | jvm heap dump browser,用于分析heapdump文件，它会建立一个HTTP/HTML服务器，让用户可以在浏览器上查看分析结果 |
| jstack | stack trace for java，显示jvm的线程快照                      |



### jps：虚拟机进程状况工具

类似linux中ps命令。

| 选项 | 作用                                               |
| ---- | -------------------------------------------------- |
| -q   | 只输出lvmid,省略主类的名称                         |
| -m   | 输出虚拟机进程启动时传递给主类main函数的参数       |
| -l   | 输出主类的全名，如果进程执行的是Jar包，输出jar路径 |
| -v   | 输出虚拟机进程启动时jvm参数                        |

### jstat: jvm统计信息监视工具

| 选项              | 作用                                                         |
| ----------------- | ------------------------------------------------------------ |
| -class            | 监视类装载，卸载数量，总空间以及类装载所耗费的时间           |
| -gc               | 监视java heap，包括eden,2 survivor区，old,永久代等容量，已用空间，gc时间合计等信息 |
| -gccapacity       | 与gc相同，主要关注java heap中各个区域使用到的最大最小空间    |
| -gcutil           | 与gc相同,主要关注java heap中各个区域使用到的空间占总空间的百分比 |
| -gccause          | 与util相同,额外输出导致上一次gc的原因                        |
| -gcnew            | 监视新生代gc状况                                             |
| -gcold            | 监视老年代gc状况                                             |
| -gcnewcapacity    | 与gcnew相同，主要关注使用到的最大最小空间                    |
| -gcoldcapacity    | 与gcold相同，主要关注使用到的最大最小空间                    |
| -gcpermcapacity   | 永久代使用到的最大，最小空间                                 |
| -compiler         | jit编译器编译过的方法，耗时等信息                            |
| -printcompilation | jit编译器已经编译过的方法                                    |



### jinfo:java配置信息工具

实时的查看和调整jvm各项参数。

jps -v可以显示jvm启动时的参数列表，但如果想知道未被显示指定的参数的系统默认值，可以使用jinfo -flag

或者使用java -XX:PrintFlagsFinal

jinfo -sysprops == system.getProperties();



### jmap:java内存映像工具

生成jvm的内存转储快照(heapdump)文件

--> -XX:HeapDumpOnOutOfMemoryError参数，在发生oom的时候生成dump文件

-XX:+HeapDumpOnCtrlBreak,可以让jvm在使用ctrl + break键生成dump文件。

linux kill -3

还可以查询finalize执行队列，java堆和永久代的详细信息（空间使用率，哪种收集器）

| 名称          | 作用                                                         |
| ------------- | ------------------------------------------------------------ |
| -dump         | 生成java堆转储快照。-dump:[live,]format=b,file=< filename >,live参数说明是否只dump出存活的对象 |
| -finalizeinfo | 显示f-queue中等待finalizer线程执行finalize方法的对象。只在linux平台下有效 |
| -heap         | 显示heap详细信息，如使用哪种回收器，参数配置，分代状态等。只在linux平台下有效 |
| -histo        | 显示heap中对象统计信息，包括类，实例数量，合计容量。         |
| -permstat     | 以classloader为统计口径显示永久代内存状态。Linux             |
| -F            | linux....当jvm进程对-dump没有响应时，强制生成dump快照        |

### jhat:jvm heap转储快照分析工具

与jmap搭配使用，分析dump文件。内置了一个Http/html服务器，可以在浏览器中查看

一般不使用

1. 一般不会在部署应用程序的服务器上直接分析dump文件，即使可以这样做，也会尽量将dump文件复制到其他服务器上分析。
2. 功能简陋。



### jstack:java 堆栈跟踪工具

用于生成jvm当前时候的线程快照(threaddump,javacore文件)。

线程快照就是当前jvm内每一条线程正在执行的方法堆栈的集合。

生成快照的主要目的是定位线程出现长时间停顿的原因。线程死锁，死循环，响应时间过长等

| 选项 | 作用                                         |
| ---- | -------------------------------------------- |
| -F   | 当正常输出的请求不被响应时，强制输出线程堆栈 |
| -l   | 除堆栈外，显示关于锁的附加信息               |
| -m   | 如果调用到本地方法，可以显示c/c++的堆栈      |





GUI篇

### JConsole: Java监视与管理控制台

jconsole是一种基于jmx的可视化监视，管理工具。

位于jdk/bin目录下。

![image-20191028233346432](C:\Users\shao\AppData\Roaming\Typora\typora-user-images\image-20191028233346432.png)



主界面包括：概述，内存，线程，类，vm摘要，MBean

概述

![image-20191028233649767](C:\Users\shao\AppData\Roaming\Typora\typora-user-images\image-20191028233649767.png)

堆内存使用量，线程，类，cpu占用率

内存：

![image-20191028233752063](C:\Users\shao\AppData\Roaming\Typora\typora-user-images\image-20191028233752063.png)

相当于jstat命令。

用于监控java堆和永久代的变化趋势。





javap:

