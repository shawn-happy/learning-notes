java提供了很多数据结构实现，例如`ArrayList,LinkedList,HashMap,TreeMap,HashSet`等，这些集合类对底层数据结构-数组，链表，红黑树等进行了封装实现。但上述提到的集合类，都是线程不安全，为了解决这个问题，JUC框架提供了很多线程安全的并发容器。本节主要对java并发容器做一个系统性的讲解。

# java容器

java提供的容器大致分为5类：

* `List`：`ArrayList,LinkedList,Vector（废弃）`
* `Stack`：`Stack（废弃）`
* `Queue`：`ArrayDeque,LinkedList,PriorityQueue`
* `Set`：`HashSet,TreeSet,LinkedHashSet`
* `Map`：`HashMap,TreeMap,LinkedHashMap,Hashtable（废弃）`

上述提到的容器，`Vector,Stack,Hashtable`三个容器是线程安全的同步容器，是JDK1.0就诞生的，为了兼容老项目代码，就一直保留着。其余的容器均为考虑线程安全性。

# java同步容器

在没有并发问题的情况下，我们会优先选择`ArrayList,HashMap,HashSet`等非线程安全的容器，性能更高。但如果出现了并发问题，需要选择线程安全的容器，例如`Vector,Stack,Hashtable`，但这些同步容器都已经被废弃了，并不推荐使用。`JCF(java collection framework)`提供了另外一种方式，通过`Collections.synchronizedXXX()`方法将一个将非线程安全的容器类包装成线程安全的容器。

`Collections`相关代码如下：

```java
public static <T> Collection<T> synchronizedCollection(Collection<T> c) {
    return new SynchronizedCollection<>(c);
}

static <T> Collection<T> synchronizedCollection(Collection<T> c, Object mutex) {
    return new SynchronizedCollection<>(c, mutex);
}

public static <T> List<T> synchronizedList(List<T> list) {
  return (list instanceof RandomAccess ?
          new SynchronizedRandomAccessList<>(list) :
          new SynchronizedList<>(list));
}

static <T> List<T> synchronizedList(List<T> list, Object mutex) {
  return (list instanceof RandomAccess ?
          new SynchronizedRandomAccessList<>(list, mutex) :
          new SynchronizedList<>(list, mutex));
}

public static <K,V> Map<K,V> synchronizedMap(Map<K,V> m) {
  return new SynchronizedMap<>(m);
}

public static <T> Set<T> synchronizedSet(Set<T> s) {
  return new SynchronizedSet<>(s);
}

static <T> Set<T> synchronizedSet(Set<T> s, Object mutex) {
  return new SynchronizedSet<>(s, mutex);
}

public static <K,V> NavigableMap<K,V> synchronizedNavigableMap(NavigableMap<K,V> m) {
  return new SynchronizedNavigableMap<>(m);
}

public static <T> NavigableSet<T> synchronizedNavigableSet(NavigableSet<T> s) {
  return new SynchronizedNavigableSet<>(s);
}

public static <K,V> SortedMap<K,V> synchronizedSortedMap(SortedMap<K,V> m) {
  return new SynchronizedSortedMap<>(m);
}

public static <T> SortedSet<T> synchronizedSortedSet(SortedSet<T> s) {
  return new SynchronizedSortedSet<>(s);
}
```

* `Collections.synchronizedCollection()` 对应的包装类是`SynchronizedCollection`
* `Collections.synchronizedList()` 对应的包装类是`SynchronizedList`
* `Collections.synchronizedSet()` 对应的包装类是`SynchronizedSet`
* `Collections.synchronizedMap()` 对应的包装类是`SynchronizedMap`
* `Collections.synchronizedNavigableSet()` 对应的包装类是`SynchronizedNavigableSet`
* `Collections.synchronizedNavigableMap()` 对应的包装类是`SynchronizedNavigableMap`
* `Collections.synchronizedSortedMap()` 对应的包装类是`SynchronizedSortedMap`
* `Collections.synchronizedSortedSet()` 对应的包装类是`SynchronizedSortedSet`

Java并发容器的底层实现原理非常简单，跟`Vector、Stack、HashTable`类似，都是通过对方法加锁来避免线程安全问题。我们拿`SynchronizedList`举例讲解，其源码如下所示。

```java
static class SynchronizedList<E>
  extends SynchronizedCollection<E>
  implements List<E> {
  private static final long serialVersionUID = -7754090372962971524L;

  final List<E> list;

  SynchronizedList(List<E> list) {
    super(list);
    this.list = list;
  }
  SynchronizedList(List<E> list, Object mutex) {
    super(list, mutex);
    this.list = list;
  }

  public boolean equals(Object o) {
    if (this == o)
      return true;
    synchronized (mutex) {return list.equals(o);}
  }
  public int hashCode() {
    synchronized (mutex) {return list.hashCode();}
  }

  public E get(int index) {
    synchronized (mutex) {return list.get(index);}
  }
  public E set(int index, E element) {
    synchronized (mutex) {return list.set(index, element);}
  }
  public void add(int index, E element) {
    synchronized (mutex) {list.add(index, element);}
  }
  public E remove(int index) {
    synchronized (mutex) {return list.remove(index);}
  }

  public int indexOf(Object o) {
    synchronized (mutex) {return list.indexOf(o);}
  }
  public int lastIndexOf(Object o) {
    synchronized (mutex) {return list.lastIndexOf(o);}
  }

  public boolean addAll(int index, Collection<? extends E> c) {
    synchronized (mutex) {return list.addAll(index, c);}
  }

  public ListIterator<E> listIterator() {
    return list.listIterator(); // Must be manually synched by user
  }

  public ListIterator<E> listIterator(int index) {
    return list.listIterator(index); // Must be manually synched by user
  }

  public List<E> subList(int fromIndex, int toIndex) {
    synchronized (mutex) {
      return new SynchronizedList<>(list.subList(fromIndex, toIndex),
                                    mutex);
    }
  }

  @Override
  public void replaceAll(UnaryOperator<E> operator) {
    synchronized (mutex) {list.replaceAll(operator);}
  }
  @Override
  public void sort(Comparator<? super E> c) {
    synchronized (mutex) {list.sort(c);}
  }
}
```

由此可见，`SynchronizedXXX`之类的并发容器，其实现就是所有函数都加上`synchronized`关键字加锁，所以也就导致了同一时刻只有一个线程在执行操作该集合，导致并发性很差。

其实`Vector,Hashtable,Stack`也是通过`synchronzied`加锁实现的并发容器。

# JUC并发容器

`SynchronizedXXX`之类的并发容器只能称之为同步容器，实际上也并非每一次操作都会产生线程安全问题，一刀切式的方式去解决线程安全问题往往会带来性能，并发度的问题。所以JUC框架又提供了并发容器，这些容器是可以并发执行的，并且不会产生线程安全问题。

JUC框架提供的并发容器，之所以性能高，是因为JUC框架采用了写时复制，分段加锁，无锁编程等技术对并发容器做了全新的实现，并非像`SynchronizedXXX，Vector,Hashtable, Stack`只是简单的加锁实现。

JUC并发容器大致可以分为五类：

* 写时复制：
  * `CopyOnWriteArrayList`
  * `CopyOnWriteArraySet`
* 阻塞队列：
  * `ArrayBlockingQueue`
  * `LinkedBlockingQueue`
  * `PriorityBlockingQueue`
  * `LinkedTransferQueue`
  * `DelayQueue`
  * `SynchronousQueue`
* 非阻塞队列：
  * `ConcurrentLinkedQueue`
  * `ConcurrentLinkedDeque`
* 分段加锁：
  * `ConcurrentHashMap`
* 并发SkipList：
  * `ConcurrentSkipListMap`
  * `ConcurrentSkipListSet`

## 写时复制

写时复制是一个比较通用的解决并发问题的方式，在并发访问的情景下，当需要修改（增，删，改）java中容器的元素时，不直接修改该容器，而是先复制一份副本，在副本上进行修改。修改完成之后，将指向原来容器的引用指向新的容器(副本容器)。

我们以`CopyOnWriteArrayList`为例，以`add(),set(),remove(),get()`四个函数为切入点，看看Copy-On-Write(COW)是怎么解决并发问题的。

### add()

```java
public boolean add(E e) {
    synchronized (lock) {
        // 获取老数组
        Object[] es = getArray();
        int len = es.length;
        // 复制出新数组
        es = Arrays.copyOf(es, len + 1);
        // 添加元素到新数组中 
        es[len] = e;
        // 把原数组引用指向新数组
        setArray(es);
        return true;
    }
}
```

当往容器中添加数据时，并非直接将数据添加到原始数组中，而是创建一个长度比原始数组大一的数组`es`，将原始数组中的数据拷贝到`es`，然后将数据添加到`es`的末尾。最后，修改`array`引用指向`es`。

除此之外，为了保证写操作的线程安全性，避免两个线程同时执行写时复制，写操作通过加锁来串行执行。

### set()

```java
public E set(int index, E element) {
    synchronized (lock) {
        Object[] es = getArray();
        // 获取老数组中的下标为index的数据
        E oldValue = elementAt(es, index);

        // 如果新值 不等于 旧值，则克隆一份老数组，并赋新值
        if (oldValue != element) {
            es = es.clone();
            es[index] = element;
        }
        // Ensure volatile write semantics even when oldvalue == element
        setArray(es);
        return oldValue;
    }
}
```

其实现与`add()`函数基本一致。

### remove()

```java
public E remove(int index) {
    synchronized (lock) {
        // 获取老数组
        Object[] es = getArray();
        int len = es.length;
        // 获取老数组中的下标为index的数据
        E oldValue = elementAt(es, index);
        // 移动数据的下标
        int numMoved = len - index - 1;
        Object[] newElements;
        // 如果要删的index为len - 1, 数组的末尾
        if (numMoved == 0)
            // 则直接拷贝[0, len - 1)的数据
            newElements = Arrays.copyOf(es, len - 1);
        else {
            newElements = new Object[len - 1];
            // 复制老数组中[0, index)中的数据到新数组中[0, index)中
            System.arraycopy(es, 0, newElements, 0, index);
            // 复制老数组中[index + 1, len)中的数据到新数组中[index, index + numMoved]中
            System.arraycopy(es, index + 1, newElements, index,
                             numMoved);
        }
        setArray(newElements);
        return oldValue;
    }
}
```

`remove()`函数的处理逻辑跟`add()`函数类似，先通过加锁保证写时复制操作的线程安全性，然后申请一个大小比原始数组大小小一的新数组`newElements`。除了待删除数据之外，我们将原始数组中的其他数据统统拷贝到`newElements`。拷贝完成之后，我们将`array`引用指向`newElements`。

### get()

```java
public E get(int index) {
    return elementAt(getArray(), index);
}
```

`get()`函数的逻辑十分简单，直接按照下标访问array数组。从代码中，我们可以发现，读操作没有加锁，因此，即便在多线程环境下，效率也非常高。

根据`get()`和`add(),remove(),set()`的实现来看，写操作都加了锁，但是读是无锁的，所以读读、读写都可以并行执行，唯独写写不可以并行执行。

### CopyOnWriteArraySet

```java
public class CopyOnWriteArraySet<E> extends AbstractSet<E>
        implements java.io.Serializable {
    private static final long serialVersionUID = 5457747651344034263L;

    private final CopyOnWriteArrayList<E> al;

    public CopyOnWriteArraySet() {
        al = new CopyOnWriteArrayList<E>();
    }
  
    public CopyOnWriteArraySet(Collection<? extends E> c) {
        if (c.getClass() == CopyOnWriteArraySet.class) {
            @SuppressWarnings("unchecked") CopyOnWriteArraySet<E> cc =
                (CopyOnWriteArraySet<E>)c;
            al = new CopyOnWriteArrayList<E>(cc.al);
        }
        else {
            al = new CopyOnWriteArrayList<E>();
            al.addAllAbsent(c);
        }
    }

    public int size() {
        return al.size();
    }

    public boolean isEmpty() {
        return al.isEmpty();
    }

   
    public boolean contains(Object o) {
        return al.contains(o);
    }
  
  public boolean remove(Object o) {
        return al.remove(o);
    }
  
    public boolean add(E e) {
        return al.addIfAbsent(e);
    }
  
  // 省略其他函数....
}
```

除了`CopyOnWriteArrayList`，还有`CopyOnWriteArraySet`，其内部实现就是基于`CopyOnWriteArrayList`实现的，`CopyOnWriteArraySet`中的函数委托给`CopyOnWriteArrayList`来实现。

在`HashSet`或者`TreeSet`中，`contains()`函数基于哈希表或者红黑树来实现，查询效率非常高。而在`CopyOnWriteArraySet`中，`contains()`函数基于数组来实现，需要遍历查询，执行效率比较低。

### 特点

#### 读多写少

从以上给出的`CopyOnWriteArrayList`源码，我们可以发现，一方面，写操作需要加锁，只能串行执行。另一方面，写操作执行写时复制逻辑，涉及大量数据的拷贝。因此，写操作的执行效率很低。写时复制并发容器只适用于读多写少的应用场景。

```java
public class CopyOnWriteDemo {

  private static final int SIZE = 100000;

  public static void main(String[] args) {
    CopyOnWriteArrayList<Integer> cowList = new CopyOnWriteArrayList<>();
    long startTime = System.currentTimeMillis();
    for (int i = 0; i < SIZE; i++) {
      cowList.add(i);
    }
    System.out.println("CopyOnWriteArrayList. " + (System.currentTimeMillis() - startTime));
    startTime = System.currentTimeMillis();
    ArrayList<Integer> list = new ArrayList<>();
    for (int i = 0; i < SIZE; i++) {
      list.add(i);
    }
    System.out.println("ArrayList. " + (System.currentTimeMillis() - startTime));

    List<Integer> synchronizedList = Collections.synchronizedList(new ArrayList<Integer>());
    startTime = System.currentTimeMillis();
    for (int i = 0; i < SIZE; i++) {
      synchronizedList.add(i);
    }
    System.out.println("synchronizedList. " + (System.currentTimeMillis() - startTime));

    Vector<Integer> vector = new Vector<>();
    startTime = System.currentTimeMillis();
    for (int i = 0; i < SIZE; i++) {
      vector.add(i);
    }
    System.out.println("vector. " + (System.currentTimeMillis() - startTime));
  }
}
```

测试结果如下：

```
CopyOnWriteArrayList. 1898
ArrayList. 3
synchronizedList. 3
vector. 4
```

可以看出`CopyOnWriteArrayList`的`add()`方法是真的慢。要想写的效率也高，并且线程安全，可以采用`synchronizedList`之类的容器。

#### 弱一致性

通过`CopyOnWriteArrayList`的原码我们也可以发现，由于不会修改原始容器，只修改副本容器。因此，可以对原始容器进行并发地读。其次，实现了读操作与写操作的分离，读操作发生在原始容器上，写操作发生在副本容器上，这就有可能会造成数据一致性问题：读操作的线程可能不会立即读取到新修改的数据，因为修改操作发生在副本上。但最终修改操作会完成并更新容器，因此这是最终一致性。

弱一致性的另一个体现是当使用迭代器的时候, 使用迭代器遍历集合时, 该迭代器只能遍历到创建该迭代器时的数据, 对于创建了迭代器后对集合进行的修改, 该迭代器无法感知; 这是因为创建迭代器时, 迭代器对原始数据创建了一份 快照 (Snapshot); 因此 `CopyOnWriteArrayList` 和 `CopyOnWriteArraySet` 只能适用于对数据实时性要求不高的场景;

```java
static final class COWIterator<E> implements ListIterator<E> {
    /** Snapshot of the array */
    private final Object[] snapshot;
    /** Index of element to be returned by subsequent call to next.  */
    private int cursor;

    COWIterator(Object[] es, int initialCursor) {
        cursor = initialCursor;
        snapshot = es;
    }

    public boolean hasNext() {
        return cursor < snapshot.length;
    }

    public boolean hasPrevious() {
        return cursor > 0;
    }

    @SuppressWarnings("unchecked")
    public E next() {
        if (! hasNext())
            throw new NoSuchElementException();
        return (E) snapshot[cursor++];
    }

    @SuppressWarnings("unchecked")
    public E previous() {
        if (! hasPrevious())
            throw new NoSuchElementException();
        return (E) snapshot[--cursor];
    }

    public int nextIndex() {
        return cursor;
    }

    public int previousIndex() {
        return cursor - 1;
    }
    public void remove() {
        throw new UnsupportedOperationException();
    }
    public void set(E e) {
        throw new UnsupportedOperationException();
    }
    public void add(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void forEachRemaining(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        final int size = snapshot.length;
        int i = cursor;
        cursor = size;
        for (; i < size; i++)
            action.accept(elementAt(snapshot, i));
    }
}
```

#### 连续存储

在写时复制的处理逻辑中，每次执行写操作时，哪怕只添加、修改、删除一个数据，都需要大动干戈，把原始数据重新拷贝一份，如果原始数据比较大，那么，对于链表、哈希表来说，因为数据在内存中不是连续存储的，因此，拷贝的耗时将非常大，写操作的性能将无法满足一个工业级通用类对性能的要求。而`CopyOnWriteArrayList`和`CopyOnWriteArraySet`底层都是基于数组来实现的，数组在内存中是连续存储的，并且底层是通过native方法完成数组的操作，其性能还算可以。这就是为什么java没有提供`CopyOnWriteHashMap`等工具集合类的原因。

那么如何运用写时复制的方式，实现一个`CopyOnWriteHashMap`？ 

```java
public class CopyOnWriteMap<K, V> implements ConcurrentMap<K, V> {

    private volatile Map<K, V> map;

    public CopyOnWriteMap() {
        this.map = Collections.emptyMap();
    }

    public CopyOnWriteMap(Map<K, V> map) {
        this.map = Collections.unmodifiableMap(map);
    }

    @Override
    public boolean containsKey(Object k) {
        return map.containsKey(k);
    }

    @Override
    public boolean containsValue(Object v) {
        return map.containsValue(v);
    }

    @Override
    public Set<java.util.Map.Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    @Override
    public V get(Object k) {
        return map.get(k);
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public synchronized void clear() {
        this.map = Collections.emptyMap();
    }

    @Override
    public synchronized V put(K k, V v) {
        Map<K, V> copy = new HashMap<>(this.map);
        V prev = copy.put(k, v);
        this.map = Collections.unmodifiableMap(copy);
        return prev;
    }

    @Override
    public synchronized void putAll(Map<? extends K, ? extends V> entries) {
        Map<K, V> copy = new HashMap<>(this.map);
        copy.putAll(entries);
        this.map = Collections.unmodifiableMap(copy);
    }

    @Override
    public synchronized V remove(Object key) {
        Map<K, V> copy = new HashMap<>(this.map);
        V prev = copy.remove(key);
        this.map = Collections.unmodifiableMap(copy);
        return prev;
    }

    @Override
    public synchronized V putIfAbsent(K k, V v) {
        if (!containsKey(k))
            return put(k, v);
        else
            return get(k);
    }

    @Override
    public synchronized boolean remove(Object k, Object v) {
        if (containsKey(k) && get(k).equals(v)) {
            remove(k);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public synchronized boolean replace(K k, V original, V replacement) {
        if (containsKey(k) && get(k).equals(original)) {
            put(k, replacement);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public synchronized V replace(K k, V v) {
        if (containsKey(k)) {
            return put(k, v);
        } else {
            return null;
        }
    }

}
```

## 分段加锁

## 阻塞等待