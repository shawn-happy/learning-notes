# 内存分配与回收策略

* **对象优先在eden区分配**

  大多数情况下，对象在新生代eden区中分配。如果eden区内存不足，jvm会触发一次minor GC。

  -XX:PrintGCDetails，在发生垃圾收集时打印内存日志。

  Full GC和Minor GC的区别

  * Minor GC,指发生在新生代的垃圾收集动作，因为java对象大多数都具备朝生夕灭的特性，所以Minor GC非常频繁，一般回收速度也比较快。
  * Full GC，指发生在老年代的垃圾收集操作，出现了Major gc，经常会伴随至少一次Minor gc,速度比Minor gc慢。

* 大对象直接进入老年代

  大对象是指需要大量连续内存空间的java对象，最典型的大对象就是那种很长的字符串以及数组。经常出现大对象容易导致内存还有不少空间时就提前出发垃圾收集以获取足够的连续空间来安置它们。

  -XX:PretenureSizeThreshold，令大于这个设置值的对象直接在老年代分配。这样做的目的是避免在eden区已经两个survivor区之间发生大量的内存复制。

* 长期存活的对象将进入老年代

  既然jvm采用分代收集的思想来管理内存，那么内存回收时就必须能识别哪些对象应放在新生代，哪些对象应放在老年代中。

  jvm给每个对象定义了一个对象age，如果对象在eden出生并经历过第一个gc还存活着，并且能被survivor容纳的话，将被移动到survivor空间中，并且age = 1，对象每在survivor中熬过一次gc，age += 1，得到-XX:MaxTenuringThreshold设置。

* 动态对象年龄判定

  如果在survivor空间中相同年龄所有对象大小的总和大于survivor空间的一半，年龄大于或等于该年龄的对象可以直接进入老年代。

* 空间分配担保

  在minor gc之前，jvm会先检查老年代最大可用的连续空间是否大于新生代所有对象总空间，如果这个条件成立，那么minor gc可以确保是安全的。

  如果不成立。jvm查看handlePromotionFailure设置值是否允许担保失败。如果允许，那么会继续检查老年代最大可用的连续空间是否大于历次晋升到老年代对象的平均大小，如果大于，将尝试着进行一次minor GC,有风险，如果小于，或者handlePromotionFailure设置不允许冒险，full gc。