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

### Collection Interface

集合框架的两个基础接口是`java.util.Collection`和`java.util.Map`接口。

`Collection`接口主要提供了顺序线性表的操作，在jdk层面，`Collection`接口属于Root Interface，只定义了顺序线性表的基本操作，没有任何直接实现它的类。因为有些`Collection`允许存储多个重复的值，但有些不允许；有些`Collection`是有序的，有些是无序的；有些`Collection`的行为是不变的，有些是可变的，等等诸如此类的需求。

`Collection`接口规定所有通用的实现类，都需要提供两个标准的构造函数：一个是无参构造，主要用来创造一个空集合。另一个需要提供一个集合类型的参数，主要用来创建一个与参数元素类型相同的新集合。当然，这不是一个强制约束的条件，因为java的interface不能实现构造函数，但是java提供的lib库里都执行了这个约定。

`Collection`提供了一套通用的操作异常定义，其原因是有些方法实现会有一些条件限制，或者不支持该操作。

* 如果是不可变的集合，一经创建，就不能再做修改，也就是有关`add, set, remove`等操作不能再使用，需要抛出`UnsupportedOperationException`异常。例如：`java.util.Collections.UnmodifiableCollection`。

* 有些方法需要保证元素不能为空，例如`java.util.ArrayList#toArray(T[])`，参数不能为空，否则就会报`NullPointerException`。
* 如果有些方法尝试操作一些不合法的参数，比如类型不对（继承，多态），则会报`ClassCastException`。
* `java.util.AbstractList#subList`，如果`fromIndex>toIndex`，则`fromIndex`和`toIndex`属于不合法的参数，会抛出`IllegalArgumentException`。
* `IllegalStateException`异常，通常是在迭代器里出现。`java.util.AbstractList.Itr#remove`。

`Collection`接口还需要重新定义`equals(),hashCode()`。这样不同的集合实现可以自定义不同的行为。例如`contains`方法。

`Collection`有三个主要的子接口

* `java.util.List`：有序集合，又被称为序列，元素有特定的顺序，且可以重复插入，可以按照下标访问，也可以按照下标插入，删除，更新。
* `java.util.Set`：无序集合，不能通过下标操作。
* `java.util.Queue`：表示队列，一种先进先出(FIFO)的线性结构。

Code Example:

```java
public class CollectionDemo {

  public static void main(String[] args) {
    testThrowUnsupportedOperationException();
    testThrowNullPointerException();
    testThrowIllegalArgumentException();

    testEquals();
  }

  private static void testEquals() {
    List<Integer> list1 = new ArrayList<>();
    list1.add(1);
    list1.add(2);
    List<Integer> list2 = new ArrayList<>();
    list2.add(1);
    list2.add(2);
    System.out.println(list1.equals(list2));
  }

  private static void testThrowIllegalArgumentException() {
    List<Integer> collection = new ArrayList<>();
    try {
      collection.subList(1, 0);
    } catch (IllegalArgumentException e) {
      System.out.println("IllegalArgumentException");
    }
  }

  private static void testThrowNullPointerException() {
    Collection<Integer> collection = new ArrayList<>();
    collection.add(1);
    Object[] array = null;
    try {
      collection.toArray(array);
    } catch (NullPointerException e) {
      System.out.println("NullPointerException");
    }
  }

  private static void testThrowUnsupportedOperationException() {
    Collection<Integer> collection =
        Collections.unmodifiableCollection(Arrays.asList(1, 3, 4, 5, 2, 7));
    try {
      collection.add(8);
    } catch (UnsupportedOperationException e) {
      System.out.println("unmodifiable collection");
    }
  }
}
```

### List Interface



### 参考文献

[Java Collection Framework](https://docs.oracle.com/javase/8/docs/technotes/guides/collections/)