## Collections Framework

### 基本介绍

集合（有时称为容器）只是一个将多个元素组合成一个单元的对象。集合用于存储、检索、操作（CURD）、聚合数据。

什么是集合框架？集合框架是用于表示和操作集合的统一架构。所有集合框架都包含以下内容： 

* `Interfaces`：这些是代表集合的抽象数据类型。接口允许独立于其表示的细节来操作集合。在面向对象的语言中，接口通常形成层次结构
* `Implementations`：这些是集合接口的具体实现。本质上，它们是可重用的数据结构。
* `Algorithms`：这些是对实现集合接口的对象执行有用计算（例如搜索和排序）的方法。这些算法被称为是多态的：也就是说，相同的方法可以用于适当集合接口的许多不同实现。本质上，算法是可重用的功能。

Java Collections Framework 提供以下好处：

* 减少编程工作：通过提供有用的数据结构和算法。
* 提高程序速度和质量（多态）：此集合框架提供有用数据结构和算法的高性能、高质量实现。每个接口的各种实现是可以互换的，因此可以通过切换集合实现轻松调整程序。
* 允许无关 API 之间的互操作性：集合接口是 API 来回传递集合的术语。
* 减少学习和使用新 API 的工作量：许多 API 自然地将集合作为输入并作为输出提供。过去，每个这样的 API 都有一个专门用于操作其集合的小型子 API。这些临时集合子 API 之间几乎没有一致性，因此您必须从头开始学习每个 API，并且在使用它们时很容易出错。随着标准集合接口的出现，问题就迎刃而解了。
* 减少设计新 API 的工作量：这是先前优势的另一面。设计者和实现者不必在每次创建依赖于集合的 API 时重新发明轮子；相反，他们可以使用标准的集合接口。
* 促进软件重用：符合标准集合接口的新数据结构本质上是可重用的。

### 基本组成

* Collection Interfaces:
  * Collection
    * List
    * Set
      * SortedSet
        * NavigableSet
    * Queue
    * Deque
  * Map
    * SortedMap
      * NavigableMap
* Infrastructure:
  * Iterator
    * ListIterator
* Abstract Implementations: 抽象实现
  * AbstractCollection
    * AbstractList
      * AbstractSequentialList
    * AbstractSet
    * AbstractQueue
  * AbstractMap
* General-purpose implementations: 集合接口的主要实现
  * ArrayList
  * LinkedList
  * HashSet
  * LinkedHashSet
  * TreeSet
  * ArrayDeque
  * HashMap
  * LinkedHashMap
  * TreeMap
* Legacy implementations：遗留实现
  * Hashtable
  * Vector
  * Stack
  * Dictionary
  * BitSet
  * Enumeration
* Convenience implementations:
  * Collections
  * Arrays
  * List
  * Set
  * Map
* Wrapper implementations:
  * Collections
* Special-purpose implementations:
  * WeakHashMap
  * ThreadLocalMap
  * PriorityQueue
  * EnumSet
  * IdentityHashMap
* Array Utilities:
  * Arrays
* Concurrent implementations:
* Algorithms：
  * Sort
  * Binary Search

### 参考文献

[Java Collection Framework](https://docs.oracle.com/javase/8/docs/technotes/guides/collections/)

