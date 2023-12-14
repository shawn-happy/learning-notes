1. GC算法

   | 算法          | 优点                       | 缺点                                                         |
   | ------------- | -------------------------- | ------------------------------------------------------------ |
   | 标记-清除算法 | 不需要移动对象，简单高效   | 空间碎片化，效率低                                           |
   | 复制算法      | 简单高效，不会产生空间碎片 | 内存缩小为原来的一半。<br />在对象存活率较高时就需要进行较多的复制操作，效率将会变低。 |
   | 标记-整理算法 | 综合了前两种算法的优点     | 仍需要移动局部对象                                           |
   | 分代收集算法  | 分区回收                   | 对于长时间存活对象的场景回收效果不明显<br />甚至起到反作用。 |

2. GC垃圾回收器

   | 回收器类型             | 算法                                                | 特点                                                         | 设置参数                                                     |
   | ---------------------- | --------------------------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
   | Serial New、Serial Old | Serial New采用复制算法；Serial Old采用标记-整理算法 | 1. 单线程复制回收，简单高效；2. stop the world；             | `-XX:+UseSerialGC // 年轻代，老年代回收器分别为：Serial New, Serial Old` |
   | ParNew New、ParNew Old | ParNew New采用复制算法；ParNew Old采用标记-整理算法 | 1. Stop the world；2. 多线程回收，减低了Stop The World的时间；3.增加了上下文切换 | `-XX:+UseParNewGC// 年轻代，老年代回收器为：ParNew New, Serial Old, JDK1.8中无效；-XX:+UseParallelOldGC //年轻代，老年代：Parallel Scavenge, Parallel Old` |
   | Parallel Scavenge      | 复制算法                                            | 并行回收器，追求高吞吐量，高效利用CPU                        | `-XX:+UseParallelGC //年轻代，老年代回收器分别为：Parallel Scavenge,Serial Old -XX:ParallelGCThreads=4//设置并发线程` |
   | CMS                    | 标记-清除算法                                       | 老年代回收器，高并发，低停顿，追求最短GC回收停顿时间，CPU占用比较高，响应时间快，停顿时间短 | `-XX:+UseConcMarkSweepGC//年轻代，老年代回收器分别为：ParNew New,CMS(Serial Old备用)` |
   | G1                     | 标记-整理+复制算法                                  | 高并发，低停顿，可预测停顿时间                               | `-XX:+UseG1GC// 年轻代，老年代回收器分别为：G1,G1;-XX:+MaxGCPauseMillis=200 //设置最大暂停时间` |

   