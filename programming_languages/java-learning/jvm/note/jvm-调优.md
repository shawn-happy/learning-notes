## GC调优策略

1. **降低Minor GC频率**

   新生代空间较小，Eden区很快就被填满，就会导致频繁的Minor GC。可以增加新生代的内存空间

2. **降低Full GC频率**

   * 减少创建大对象
   * 扩大堆内存空间

3. **选择合适的GC回收器**

   * CMS
   * G1

## 内存分配

参考指标：

* GC频率
* 堆内存
* 吞吐量
* 延时

方法：

* 调整堆内存空间大小减少Full GC:
  * `-Xms`堆初始化大小
  * `-Xmx`堆最大值
* 调整年轻代减少Minor GC:
  * `-Xmn`年轻代大小
* 设置Eden,survivor比例-