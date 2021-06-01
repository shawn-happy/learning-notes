# 组合模式定义

`Compose objects into tree structure to represent part-whole hierarchies. Composite lets client treat individual objects and compositions of objects uniformly.`

**将一组对象组织成树形结构**，以表示一种部分-整体的层次。组合让客户端可以统一单个对象和组合对象的处理逻辑。

需求：设计一个类来表示文件系统中目录，能方便地实现下面这些功能：

* 动态地添加，删除某个目录下的子目录或文件。
* 统计指定目录下的文件个数。
* 统计指定目录下的文件总大小。

组合模式的设计思路，是对业务场景的一种数据结构和算法的抽象。