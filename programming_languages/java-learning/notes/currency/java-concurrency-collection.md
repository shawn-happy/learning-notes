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

`HashMap`是线程不安全的，java提供`Hashtable,SynchronziedMap`是线程安全的，但其实现过于简单粗暴，所有操作都加上锁，锁粒度粗，导致并发性不好，所以JUC框架提供了基于分段加锁实现的`ConcurrentHashMap`，来提高并发度。

### put()

`ConcurrentHashMap#put()`函数跟`HashMap#put()`的逻辑大概都分为写操作，树化，扩容三个阶段。源代码如下：

```java
final V putVal(K key, V value, boolean onlyIfAbsent) {
    // key和value都不能为null
    if (key == null || value == null) throw new NullPointerException();
    int hash = spread(key.hashCode());
    int binCount = 0;
    for (Node<K,V>[] tab = table;;) {
        Node<K,V> f; int n, i, fh; K fk; V fv;
        // 如果tab未被初始化或者没有数据，则初始化Node数组
        if (tab == null || (n = tab.length) == 0)
            tab = initTable();
        // 如果tab[i]上已经有元素，则进入下一次循环，重新操作
        else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
            // 如果tab[i]上没有元素，并且使用CAS插入元素成功，则break跳出循环。
            if (casTabAt(tab, i, null, new Node<K,V>(hash, key, value)))
                break;                   // no lock when adding to empty bin
        }
        else if ((fh = f.hash) == MOVED)
            // 如果需要数据迁移，则在当前线程做数据迁移
            tab = helpTransfer(tab, f);
        else if (onlyIfAbsent // check first node without acquiring lock
                 && fh == hash
                 && ((fk = f.key) == key || (fk != null && key.equals(fk)))
                 && (fv = f.val) != null)
            // 如果key存在，则不操作，直接返回旧value。
            return fv;
        else {
            V oldVal = null;
            synchronized (f) { // 锁住当前节点
                if (tabAt(tab, i) == f) { // 再次检测tab[i]是否有变化，如果有变化则进入下一次循环，从头来过
                    if (fh >= 0) { // 如果第一个元素的hash值不是MOVED，则使用链表的方式存储
                        binCount = 1; // tab中元素个数赋值为1
                        for (Node<K,V> e = f;; ++binCount) { // 遍历整个tab，每次结束binCount加1
                            K ek;
                            if (e.hash == hash &&
                                ((ek = e.key) == key ||
                                 (ek != null && key.equals(ek)))) {
                                oldVal = e.val;
                                if (!onlyIfAbsent) // 如果找到了这个元素并且onlyIfAbsent=false，则赋值了新值
                                    e.val = value;
                                break;
                            }
                            Node<K,V> pred = e;
                            if ((e = e.next) == null) { // 如果到链表尾部还没有找到这个元素，则插入到链表尾部。
                                pred.next = new Node<K,V>(hash, key, value);
                                break;
                            }
                        }
                    }
                    else if (f instanceof TreeBin) { // 如果第一个节点是红黑树节点
                        Node<K,V> p;
                        binCount = 2; // tab中的元素个数赋值为2
                        // 插入到红黑树节点
                        if ((p = ((TreeBin<K,V>)f).putTreeVal(hash, key,
                                                       value)) != null) {
                            oldVal = p.val;
                            if (!onlyIfAbsent)
                                p.val = value;
                        }
                    }
                    else if (f instanceof ReservationNode)
                        throw new IllegalStateException("Recursive update");
                }
            }
            // 表示插入成功
            if (binCount != 0) {
                // 如果tab中的元素个数大于等于8，则需要树化。
                if (binCount >= TREEIFY_THRESHOLD)
                    treeifyBin(tab, i);
                // 如果要插入的元素已经存在，则返回旧值
                if (oldVal != null)
                    return oldVal;
                break;
            }
        }
    }
    // 成功插入元素，元素个数加1
    addCount(1L, binCount);
    return null;
}
```

#### initTable()

```java
private final Node<K,V>[] initTable() {
    Node<K,V>[] tab; int sc;
    while ((tab = table) == null || tab.length == 0) {
        if ((sc = sizeCtl) < 0) // 正在初始化或者扩容，让出cpu
            Thread.yield(); // lost initialization race; just spin
        else if (U.compareAndSetInt(this, SIZECTL, sc, -1)) {
            try {
                // 再次检查table是否为空，防止ABA问题
                if ((tab = table) == null || tab.length == 0) {
                    int n = (sc > 0) ? sc : DEFAULT_CAPACITY;
                    @SuppressWarnings("unchecked")
                    Node<K,V>[] nt = (Node<K,V>[])new Node<?,?>[n];
                    table = tab = nt;
                    // n - (n >>> 2) = n - n/4 = 0.75n
                    // 可见这里装载因子和扩容门槛都是写死了的
                    // 这也正是没有threshold和loadFactor属性的原因
                    sc = n - (n >>> 2);
                }
            } finally {
                sizeCtl = sc;
            }
            break;
        }
    }
    return tab;
}
```

#### helpTransfer()

```java
final Node<K,V>[] helpTransfer(Node<K,V>[] tab, Node<K,V> f) {
    Node<K,V>[] nextTab; int sc;
    // 如果tab数组不为空，f为ForwardingNode类型，并且f.nextTab不为空
    // 说明当前tab已经迁移完毕了，才去帮忙迁移其它tab的元素
    if (tab != null && (f instanceof ForwardingNode) &&
        (nextTab = ((ForwardingNode<K,V>)f).nextTable) != null) {
        int rs = resizeStamp(tab.length);
        while (nextTab == nextTable && table == tab &&
               (sc = sizeCtl) < 0) {
            if ((sc >>> RESIZE_STAMP_SHIFT) != rs || sc == rs + 1 ||
                sc == rs + MAX_RESIZERS || transferIndex <= 0)
                break;
            if (U.compareAndSetInt(this, SIZECTL, sc, sc + 1)) {
                transfer(tab, nextTab);
                break;
            }
        }
        return nextTab;
    }
    return table;
}
```

#### addCount()

```java
private final void addCount(long x, int check) {
    CounterCell[] as; long b, s;
    // 先尝试把数量加到baseCount上，如果失败再加到分段的CounterCell上
    if ((as = counterCells) != null ||
            !U.compareAndSwapLong(this, BASECOUNT, b = baseCount, s = b + x)) {
        CounterCell a; long v; int m;
        boolean uncontended = true;
        if (as == null || (m = as.length - 1) < 0 ||
                (a = as[ThreadLocalRandom.getProbe() & m]) == null ||
                !(uncontended =
                        U.compareAndSwapLong(a, CELLVALUE, v = a.value, v + x))) {
            fullAddCount(x, uncontended);
            return;
        }
        if (check <= 1)
            return;
        // 计算元素个数
        s = sumCount();
    }
    //检查是否需要扩容，默认check=1,需要检查
    if (check >= 0) {
        Node<K,V>[] tab, nt; int n, sc;
        // 如果元素个数达到了扩容门槛，则进行扩容
        // 注意，正常情况下sizeCtl存储的是扩容门槛，即容量的0.75倍
        while (s >= (long)(sc = sizeCtl) && (tab = table) != null &&
                (n = tab.length) < MAXIMUM_CAPACITY) {
            // rs是扩容时的一个标识
            int rs = resizeStamp(n);
            if (sc < 0) {
                // sc<0说明正在扩容中
                if ((sc >>> RESIZE_STAMP_SHIFT) != rs || sc == rs + 1 ||
                        sc == rs + MAX_RESIZERS || (nt = nextTable) == null ||
                        transferIndex <= 0)
                    // 扩容已经完成了，退出循环
                    break;
                // 扩容未完成，则当前线程加入迁移元素中
                // 并把扩容线程数加1
                if (U.compareAndSwapInt(this, SIZECTL, sc, sc + 1))
                    transfer(tab, nt);
            }
            else if (U.compareAndSwapInt(this, SIZECTL, sc,
                    (rs << RESIZE_STAMP_SHIFT) + 2))
                // 进入迁移元素
                transfer(tab, null);
            // 重新计算元素个数
            s = sumCount();
        }
    }
}
```

### get()

```java
public V get(Object key) {
    Node<K,V>[] tab; Node<K,V> e, p; int n, eh; K ek;
    int h = spread(key.hashCode());
    // 判断数组是否为空，通过key定位到数组下标是否为空；
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (e = tabAt(tab, (n - 1) & h)) != null) {
        // 如果第一个元素就是要找的元素，直接返回
        if ((eh = e.hash) == h) {
            if ((ek = e.key) == key || (ek != null && key.equals(ek)))
                return e.val;
        }
        else if (eh < 0) 
            // eh<0，说明是树或者正在扩容
            // 使用find寻找元素，find的寻找方式依据Node的不同子类有不同的实现方式
            return (p = e.find(h, key)) != null ? p.val : null;
        // 遍历整个链表寻找元素
        while ((e = e.next) != null) {
            if (e.hash == h &&
                ((ek = e.key) == key || (ek != null && key.equals(ek))))
                return e.val;
        }
    }
    return null;
}
```

get()函数对应的就是读操作。在get()函数的代码实现中，我们没有发现任何加锁等线程安全的处理逻辑，因此，get()函数可以跟任何操作（读操作、写操作、树化、扩容）并行执行，并发性能极高。

流程也相对来说比较简单：

1. 判断数组是否为空，通过key定位到数组下标是否为空；
2. 判断node节点第一个元素是不是要找到，如果是直接返回；
3. 如果是红黑树结构，就从红黑树里面查询；
4. 如果是链表结构，循环遍历判断。

## 阻塞等待

阻塞等待主要应用于阻塞队列。

读阻塞指的是，当从队列中读取数据时，如果队列已空，那么读操作阻塞，直到队列有新数据写入，读操作才成功返回。

写阻塞指的是，当往队列中写入数据时，如果队列已满，那么写操作阻塞，直到队列重新腾出空位置，写入操作才成功返回。

阻塞并发队列一般用于实现生产者-消费者模型。

JUC提供的阻塞并发队列有很多，比如`ArrayBlockingQueue`、`LinkedBlockingQueue`、`LinkedBlockingDeque`、`PriorityBlockingQueue`、`DelayQueue`、`SynchronousQueue`、`LinkedTransferQueue`。

其中`ArrayBlockingQueue`、`LinkedBlockingQueue`、`LinkedBlockingDeque`、`PriorityBlockingQueue`的实现原理类似，它们都是基于`ReentrantLock`锁来实现线程安全，基于`Condition`条件变量来实现阻塞等待。`ArrayBlockingQueue`是基于数组实现的有界阻塞并发队列，队列的大小在创建时指定。`ArrayBlockingQueue`跟普通队列的使用方式基本一样，唯一的区别在于读写可阻塞。

接下来，我们结合源码具体讲解它的实现原理，重点看下它是如何实现线程安全且可阻塞的。`ArrayBlockingQueue`的部分源码如下所示。

```java
public class ArrayBlockingQueue<E> extends AbstractQueue<E>
        implements BlockingQueue<E>, java.io.Serializable {
    final Object[] items; // 内部数组
    int takeIndex; // 下一次出队时，出队数据的下标位置
    int putIndex;  // 下一次入队时，数据存储的下标位置
    int count; // 队列中的元素个数
    final ReentrantLock lock; // 加锁实现线程安全
    private final Condition notEmpty; // 用来阻塞读，等待非空条件的发生
    private final Condition notFull;  // 用来阻塞写，等待非满条件的发生
  
    public ArrayBlockingQueue(int capacity) {
        this(capacity, false);
    }

    // 公平锁，非公平锁
    public ArrayBlockingQueue(int capacity, boolean fair) {
        if (capacity <= 0)
            throw new IllegalArgumentException();
        this.items = new Object[capacity];
        lock = new ReentrantLock(fair);
        notEmpty = lock.newCondition();
        notFull =  lock.newCondition();
    }
  
    private void enqueue(E e) {
        final Object[] items = this.items;
        items[putIndex] = e;
        if (++putIndex == items.length) putIndex = 0;
        count++;
        notEmpty.signal(); // 唤醒阻塞读，阻塞队列非空的线程
    }
    private E dequeue() {
        final Object[] items = this.items;
        @SuppressWarnings("unchecked")
        E e = (E) items[takeIndex];
        items[takeIndex] = null;
        if (++takeIndex == items.length) takeIndex = 0;
        count--;
        if (itrs != null)
            itrs.elementDequeued();
        notFull.signal(); // 唤醒阻塞写，阻塞队列非满的线程
        return e;
    }

    // 阻塞写
    public void put(E e) throws InterruptedException {
        Objects.requireNonNull(e);
        final ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        try {
            while (count == items.length) 
                notFull.await(); // 阻塞队列非满
            enqueue(e);
        } finally {
            lock.unlock();
        }
    }
    
    // 阻塞读
    public E take() throws InterruptedException {
        final ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        try {
            while (count == 0)
                notEmpty.await(); // 阻塞队列非空
            return dequeue();
        } finally {
            lock.unlock();
        }
    }
}
```