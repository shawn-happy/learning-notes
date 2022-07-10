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
        * NavigableSet (since java 1.6)
    * Queue (since java 1.5)
      - Deque (since java 1.6)
    * concurrency interface
      - BlockingQueue(since java 1.5)
      - BlockingDeque(since java 1.6)
      - TransferQueue(since java 1.7)
  * Map
    * SortedMap
      * NavigableMap(since java 1.6)
    * concurrency interface
      - ConcurrentMap(since java 1.5)
      - ConcurrentNavigableMap(since java 1.6)
* Infrastructure:
  * Iterator
    * ListIterator
* Abstract Implementations: 抽象实现
  * AbstractCollection
    * AbstractList
      * AbstractSequentialList
    * AbstractSet
    * AbstractQueue（Since Java 1.5)
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
  - LinkedBlockingQueue
  - ArrayBlockingQueue
  - PriorityBlockingQueue
  - DelayQueue
  - SynchronousQueue
  - LinkedBlockingDeque
  - LinkedTransferQueue
  - CopyOnWriteArrayList
  - CopyOnWriteArraySet
  - ConcurrentSkipListSet
  - ConcurrentHashMap
  - ConcurrentSkipListMap
* Algorithms：
  * Sort
  * Binary Search

### Root Interface

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


### Implementations

#### General-purpose implementations
|interfaces|hash table implementations|Resizable Array Implementations|Tree Implementations|Linked list Implementations|Hash table + Linked list Implementations|
|-|-|-|-|-|-|
|List||ArrayList||LinkedList||
|Set|HashSet||TreeSet||LinkedHashSet|
|Queue||||||
|Dueue||ArrayDeque||LinkedList||
|Map|HashMap||TreeMap||LinkedHashMap|
#### Convenience implementations
⾼性能的最⼩化的实现接⼝,比较经典的做法是提供一个静态的工厂方法实现。
* java9之前的主要api有：
  - Collections
  - Arrays
  - BitSet
  - EnumSet
* java9+的api有：
  - List
  - Set
  - Map
  
API类型可以分为：
1. 创建单例集合
2. 创建空集合
3. 转换
4. 列举

创建单例集合的方式有：
1. Collections.singletonList创建List类型的单例集合
2. Collections.singleton创建Set类型的单例集合
3. Collections.singletonMap创建Map类型的单例集合
Code Example:
```java
List<String> singletonList = Collections.singletonList("Singleton List");
Set<String> singletonSet = Collections.singleton("Singleton Set");
Map<String, String> singletonMap = Collections.singletonMap("key", "Singleton Map");
```
经过简单的测试，发现单例集合不能有添加，删除，修改等操作，会报`UnsupportedOperationException`异常
```java
@Test(expected = UnsupportedOperationException.class)
public void createSingletonCollectionAndAddOtherElementThrowException(){
  List<String> singletonList = Collections.singletonList("Singleton List");
  singletonList.add("other element");
  Assert.assertEquals(2, singletonList.size());
}

@Test(expected = UnsupportedOperationException.class)
public void createSingletonCollectionAndSetElementThrowException(){
  List<String> singletonList = Collections.singletonList("Singleton List");
  Assert.assertEquals("Singleton List", singletonList.get(0));
  singletonList.set(0, "Hello Singleton List");
  Assert.assertEquals(1, singletonList.size());
  Assert.assertEquals("Hello Singleton List", singletonList.get(0));
}

@Test(expected = UnsupportedOperationException.class)
public void createSingletonCollectionAndRemoveElementThrowException(){
  List<String> singletonList = Collections.singletonList("Singleton List");
  Assert.assertEquals("Singleton List", singletonList.get(0));
  singletonList.remove(0);
  Assert.assertEquals(0, singletonList.size());
}
```
原因（Collections.singletonList为例）：
Collections.singletonList继承了AbstractList,AbstractList里的add,remove,set方法默认是抛出UnsupportedOperationException异常，而singletonList并没有重新实现这些方法。

创建空集合的方式有：
1. 创建空的枚举：`Collections.emptyEnumeration()`
2. 创建空的迭代器：`Collections.emptyIterator();Collections.emptyListIterator()`
3. 创建空的List: `Collections.emptyList();Collections.EMPTY_LIST`
4. 创建空的Set: `Collections.emptySet();Collections.emptySortedSet();Collections.emptyNavigableSet();Collections.EMPTY_SET`
5. 创建空的Map：`Collections.emptyMap();Collections.emptySortedMap();Collections.emptyNavigableMap();Collections.EMPTY_MAP`
code Example:
```java
// create empty enumeration
Enumeration<Object> emptyEnumeration = Collections.emptyEnumeration();

// create empty list
List<Object> emptyList = Collections.emptyList();
List emptyList1 = Collections.EMPTY_LIST;

// create empty set
Set emptySet = Collections.EMPTY_SET;
Set<Object> emptySet1 = Collections.emptySet();
SortedSet<Object> emptySortedSet = Collections.emptySortedSet();
NavigableSet<Object> emptyNavigableSet = Collections.emptyNavigableSet();

// create empty map
Map emptyMap = Collections.EMPTY_MAP;
Map<Object, Object> emptyMap1 = Collections.emptyMap();
SortedMap<Object, Object> emptySortedMap = Collections.emptySortedMap();
NavigableMap<Object, Object> emptyNavigableMap = Collections.emptyNavigableMap();

// create empty iterator
Iterator<Object> emptyIterator = Collections.emptyIterator();
ListIterator<Object> emptyListIterator = Collections.emptyListIterator();
```

转换集合的方式有：
1. 数组（可变参数）转List：`Arrays.asList();Arrays.stream().collect(Collectors.toList());List.of()`
2. 枚举类型转List: `Collections.list(Enumeration<T> e)`
3. Collection转成枚举类型：`Collections.enumeration(Collection<T> c)`
4. 转成Set: `Collections.newSetFromMap(Map<E, Boolean> map);Set.of();stream().collect(Collectos.toSet())`
5. 转成Map: `Map.of();stream().collect(Collectors.toMap())`;
6. 转成Queue: `Collections.asLifoQueue(Deque<T> deque)`
7. HashCode: `Arrays.hashCode()`
8. toString: `Arrays.toString()`
Code Exampe:
```java
// array to list
String[] strings = new String[] {"Java", "Spring", "Web", "Tomcat"};
List<String> stringList = Arrays.asList(strings);
List<String> stringList1 = Arrays.stream(strings).collect(Collectors.toList());
List<String> stringList2 = List.of(strings);
Assert.assertEquals(4, stringList.size());
Assert.assertEquals(4, stringList1.size());
Assert.assertEquals(4, stringList2.size());

// map to set
Map<String, Boolean> map = new HashMap<>();
Set<String> set = Collections.newSetFromMap(map);
set.add("Java");
set.add("Spring");
set.add("Web");
set.add("Tomcat");
Assert.assertEquals(4, set.size());
Assert.assertTrue(set.contains("Java"));

// enumeration to list
Vector<String> vector = new Vector<>();
vector.add("Java");
vector.add("Spring");
vector.add("Web");
vector.add("Tomcat");
Enumeration<String> elements = vector.elements();
ArrayList<String> list = Collections.list(elements);
Assert.assertEquals(4, list.size());
Assert.assertTrue(list.contains("Java"));

int hashCode = Arrays.hashCode(strings);
System.out.println(hashCode);

String s = Arrays.toString(strings);
System.out.println(s);
```

列举集合的方式：
1. `List.of`
2. `Set.of`
3. `Map.of`
4. `Stream.of()`
5. `BitSet.valueOf()`
6. `EnumSet.of()`

#### Wrapper implementations
功能性添加，⽐如同步以及其他实现
设计原则: Wrapper模式原则，⼊参集合类型与返回类型相同或者其⼦类
主要⼊⼝: `java.util.Collections`
* 同步接口：
  - `synchronizedCollection()`
  - `synchronizedList()`
  - `synchronizedSet()`
  - `synchronizedSortedSet()`
  - `synchronizedNagiableSet()`
  - `synchronizedMap()`
  - `synchronizedSortedMap()`
  - `synchronizedNagiableMap()`
* 不变集合：
  - `unmodifiableCollection`
  - `unmodifiableList`
  - `unmodifiableSet`
  - `unmodifiableSortedSet`
  - `unmodifiableNavigableSet`
  - `unmodifiableMap`
  - `unmodifiableSortedMap`
  - `unmodifiableNavigableMap`
* 类型安全包装接⼝:
  - `checkedCollection`
  - `checkedList`
  - `checkedSet`
  - `checkedSortedSet`
  - `checkedNavigableSet`
  - `checkedMap`
  - `checkedSortedMap`
  - `checkedNavigableMap`
  - `checkedQueue`
  
code Example:
```java
// https://bbs.huaweicloud.com/blogs/detail/218405
List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);
List<Integer> integerList = Collections.synchronizedList(list);
synchronized (integerList) {
  Iterator<Integer> iterator = integerList.iterator();
  while (iterator.hasNext()) {
    Integer next = iterator.next();
    System.out.println(next);
  }
}

List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);
List<Integer> unmodifiableList = Collections.unmodifiableList(list);
try {
  unmodifiableList.add(7);
} catch (Exception e) {
  System.out.println("unsupported add operation");
}
try {
  unmodifiableList.remove(5);
} catch (Exception e) {
  System.out.println("unsupported remove operation");
}
try {
  unmodifiableList.set(0, 7);
} catch (Exception e) {
  System.out.println("unsupported set operation");
}

// List 元素类型是 java.lang.Integer
List<Integer> values = new ArrayList<>(Arrays.asList(1, 2, 3));
//        values.add("1"); // 编译错误
// 泛型是编译时检查，运行时擦写

// 引用 List<Integer> 类型的对象 values
List referencedValues = values;

System.out.println(referencedValues == values);

referencedValues.add("A"); // 添加 "A" 进入 List<Integer> values

// 运行时的数据 List<Integer>  == List<Object> == List
// values.add("1") // 运行时允许，因为成员类型是 Object

for (Object value : values) {
  System.out.println(value);
}

// values
// [0] = 1, [1] = 2, [2] = 3, [3] = "A"
// 创建时尚未检查内部的数据是否类型相同，操作时做检查，Wrapper 模式（装饰器模式）的运用
// Collections.checked* 接口是弥补泛型集合在运行时中的擦写中的不足
// 强约束：编译时利用 Java 泛型、运行时利用  Collections.checked* 接口
List<Integer> checkedTypeValues = Collections.checkedList(values, Integer.class);
//        checkedTypeValues.add("1"); // 编译错误
// 运行时检查

// 又将 checkedTypeValues 引用给 referencedValues
referencedValues = checkedTypeValues;

System.out.println(referencedValues == values);

System.out.println(referencedValues == checkedTypeValues);

// 添加 "B" 进入 referencedValues
referencedValues.add("B");
```

#### Special-purpose implementations
为特殊场景设计实现，这些实现表现出⾮标准性能特征、使⽤限制或者⾏为。
例如：
* 弱引用Map:
  - `WeakHashMap`
  - `ThreadLocalMap`
* 对象鉴定 Map
  - `IdentityHashMap`
* 优先级队列
  - `PriorityQueue`
* 枚举Set
  - `EnumSet`
Code Example:
```java
  @Test
  public void demoWeakHashMap() throws InterruptedException {

    // 强引用
    // value 变量是局部变量，存放在栈
    // "abc" 是常量，在 Java 8 之前是放在 Perm 区域，Java 8+ 是存放在 META 区域
    // 在 demoWeakHashMap() 方法执行结束后，value 变量会被立即回收，"abc" 常量常驻
    String value = "abc";

    ReferenceQueue<User> queue = new ReferenceQueue();

    // 弱引用
    WeakReference<User> userReference = new WeakReference<>(new User("张三"), queue);

    // WeakReference 继承为 Reference，Reference 入队 ReferenceQueue
    // 获取引用对象
    for (int i = 0; i < 100; i++) {
      TimeUnit.SECONDS.sleep(1);
      System.out.println(userReference.get());
    }
  }

  private static void demoIdentityHashCodeAndHashCode(Object a, Object b) {

    System.out.printf("System.identityHashCode(%s) == %d \n", a, System.identityHashCode(a));
    System.out.printf("%s.hashCode() == %d \n", a, a.hashCode());

    System.out.printf(
        "System.identityHashCode(%s) == System.identityHashCode(%s) == %s \n",
        a, b, System.identityHashCode(a) == System.identityHashCode(b));

    System.out.printf(
        "%s.hashCode() == %s.hashCode() == %s \n", a, b, a.hashCode() == b.hashCode());
  }

  @Test
  public void testIdentityHashMap() {
    // 如果类覆盖了 Object 的 equals(Object) 方法，那么 hashCode() 方法需不需要覆盖？
    // 说明：不强制覆盖，建议实现，注意不要将 hashCode() 作为 equals 方法的实现，可参考
    // Objects.hash(Object...) 以及 Arrays.hashCode(Object[])，hashCode() 是一个计算较重的实现
    // equals 通常是做对象属性的比较

    // 如果类覆盖了 Object 的 hashCode() ，那么 equals(Object) 方法 方法需不需要覆盖？
    // 说明：必须实现，hashCode() 是用于 Hash 计算，比如普通的 HashMap，由于不同对象的 hashCode() 方法可能返回相同的数据
    // 原因一：int 数据范围 2^31-1，原因二：hashCode() 方法计算问题
    // 当不同对象 hashCode() 相同是，再做对象 equals(Object) 方法比较
    demoHashMap();

    // 场景，需要对对象本身做鉴别
    demoIdentityHashMap();

    // System.identityHashCode() 与 覆盖 hashCode() 方法的差异

    //        Object a = new Object();
    //        demoIdentityHashCodeAndHashCode(a, a);
    //
    //        Object b = new Object();
    //        demoIdentityHashCodeAndHashCode(a, b);

    String string1 = "1";
    String string2 = "1";
    //        demoIdentityHashCodeAndHashCode(string1, string2);

    // 不同对象
    string2 = new String("1");
    demoIdentityHashCodeAndHashCode(string1, string2);
  }

  private static void demoMap(Map<String, Integer> map) {
    System.out.println("A" == new String("A")); // false
    System.out.println("A".equals(new String("A"))); // true
    System.out.println("A".hashCode() == new String("A").hashCode()); // true

    map.put("A", 1);
    map.put(new String("A"), 1);
    System.out.println(map.size());
  }

  private static void demoIdentityHashMap() {
    demoMap(new IdentityHashMap<>());
  }

  private static void demoHashMap() {
    demoMap(new HashMap<>());
  }

  @Test
  public void testPriorityQueue(){
    PriorityQueue<Integer> integerQueue = new PriorityQueue<>();

    integerQueue.add(1);
    integerQueue.add(4);
    integerQueue.add(2);
    integerQueue.add(3);

    integerQueue.forEach(System.out::println);
  }

  private static class User {

    private String name;

    public User(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return "User{" + "name='" + name + '\'' + '}';
    }
  }
```
#### Legacy Implementations
* Hashtable
* Vector
* Stack
* Enumeration
* BitSet
##### Hashtable VS HashMap
1. Hashtable产生于jdk1.0；HashMap是jdk1.2
2. Hashtable继承了Dictionary；HashMap继承了AbstractMap.
3. Hashtable不允许key或者value为null；HashMap允许有一个key为null，允许多个value为null；
4. Hashtable的数据结构是链表+数组实现的hash表，HashMap在jdk1.8之前都是链表+数组，之后就变成了链表+数组+红黑树。
5. Hashtable默认的初始大小为11，之后每次扩充为原来的2n+1。HashMap默认的初始化大小为16，之后每次扩充为原来的2倍。如果在创建时给定了初始化大小，Hashtable会直接使用你给定的大小，而HashMap会将其扩充为2的幂次方大小。
6. Hashtable线程安全的，很多方法都有synchronized修饰，但同时因为加锁导致单线程环境下效率较低;HashMap是线程不安全的，在多线程环境下会容易产生死循环，但是单线程环境下运行效率高。
7. Hashtable不推荐使用，如果需要线程安全，推荐使用ConcurrentHashMap。
8. Properties的父类是Hashtable
具体可以参考此文：https://zhuanlan.zhihu.com/p/37607299
##### Vector VS List
1. ArrayList是最常用的List实现类，内部是通过数组实现的，它允许对元素进行快速随机访问。数组的缺点是每个元素之间不能有间隔，当数组大小不满足时需要增加存储能力，就要讲已经有数组的数据复制到新的存储空间中。当从ArrayList的中间位置插入或者删除元素时，需要对数组进行复制、移动、代价比较高。因此，它适合随机查找和遍历，不适合插入和删除。
2. Vector与ArrayList一样，也是通过数组实现的，不同的是它支持线程的同步，即某一时刻只有一个线程能够写Vector，避免多线程同时写而引起的不一致性，但实现同步需要很高的花费，因此，访问它比访问ArrayList慢。
3. LinkedList是用链表结构存储数据的，很适合数据的动态插入和删除，随机访问和遍历速度比较慢。另外，他还提供了List接口中没有定义的方法，专门用于操作表头和表尾元素，可以当作堆栈、队列和双向队列使用。
4. ArrayList在内存不够时默认是扩展50% + 1个，Vector是默认扩展1倍。
5. Stack是Vector的子类，Vector 是 FIFO，Stack 是 LIFO。
### Algorithms
#### Sorting

#### Search
### 参考文献

[Java Collection Framework](https://docs.oracle.com/javase/8/docs/technotes/guides/collections/)