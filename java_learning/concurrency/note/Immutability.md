# 如何利用不变性解决并发问题

多个线程同时读写同一个共享变量就会存在并发问题。这里的必要条件之一是读写，如果只有读，没有写，是不存在并发问题的。

解决并发问题，其实最简单的方法就是让共享变量只有读操作，没有写操作。

**不变性模式：就是对象一旦被创建之后，状态就不再发生变化。**

## 快速实现具备不可变性的类

将一个类所有的属性都设置成final，并且只允许存在只读方法，那么这个类本质上就是不可变的。

或者将这个类声明为final类型的。

JDK里有很多不可变的类，比如String,Long,Integer,Double等。

这些对象的线程安全性都是靠不可变性来保证的。

类和属性都是final的，所有方法均是只读的。

String里方法里也有类似字符串替换操作，怎么能说所有方法都是只读的呢？jdk1.8的String的源码分析如下：

```java
public final class String {
  private final char value[];
  // 字符替换
  String replace(char oldChar, 
      char newChar) {
    // 无需替换，直接返回 this  
    if (oldChar == newChar){
      return this;
    }

    int len = value.length;
    int i = -1;
    /* avoid getfield opcode */
    char[] val = value; 
    // 定位到需要替换的字符位置
    while (++i < len) {
      if (val[i] == oldChar) {
        break;
      }
    }
    // 未找到 oldChar，无需替换
    if (i >= len) {
      return this;
    } 
    // 创建一个 buf[]，这是关键
    // 用来保存替换后的字符串
    char buf[] = new char[len];
    for (int j = 0; j < i; j++) {
      buf[j] = val[j];
    }
    while (i < len) {
      char c = val[i];
      buf[i] = (c == oldChar) ? 
        newChar : c;
      i++;
    }
    // 创建一个新的字符串返回
    // 原字符串不会发生任何变化
    return new String(buf, true);
  }
}

```

String这个类，以及它的属性value都是final的；而replace方法实现，的确没有修改value，而是复制一份value，得到val，对这个val进行替换，然后重新new了一个String对象返回。

如果具备不可变性的类，需要提供类似修改的功能，具体该怎么做呢？那就是创建一个新的不可变对象。这是与可变对象的一个重要区别，可变对象往往是修改自己的属性。

## 利用享元模式避免创建重复对象

利用享元模式可以减少创建对象的数量，从而减少内存占用。java语言里面Long,Integer,Short,Double,Byte等包装类都用到了享元模式。

我们以Long这个类为例子。

享元模式本质上就是一个对象池，利用享元模式创建对象的逻辑也很简单，就是在创建之前，先去对象池里看看是不是存在，如果存在，就利用对象池里的对象，如果不存在，就会新创建一个对象，并且把这个新创建出来的对象放进对象池里。

Long这个类并没有照搬享元模式，Long内部维护了一个静态的对象池，仅缓存了[-128,127]之间的数字，这个对象池在JVM启动的时候就创建好了，而且这个对象池一直不会变化，也就是说它是静态的。之所以这么设计，是因为Long这个对象的状态共有2^64种，实在太多，不宜全部缓存。而[-128,127]之间的数字利用率最高。

```java
Long valueOf(long l) {
  final int offset = 128;
  // [-128,127] 直接的数字做了缓存
  if (l >= -128 && l <= 127) { 
    return LongCache
      .cache[(int)l + offset];
  }
  return new Long(l);
}
// 缓存，等价于对象池
// 仅缓存 [-128,127] 直接的数字
static class LongCache {
  static final Long cache[] 
    = new Long[-(-128) + 127 + 1];

  static {
    for(int i=0; i<cache.length; i++)
      cache[i] = new Long(i-128);
  }
}

```

String和所有的包装类型都不适合做锁，因为他们内部用到了享元模式，这会导致看上去私有的锁，其实是共有的。本意是A用锁al，B用锁bl，互不影响，但实际上al和bl是一个对象，结果A和B共用的是一把锁。

```java
class A {
  Long al=Long.valueOf(1);
  public void setAX(){
    synchronized (al) {
      // 省略代码无数
    }
  }
}
class B {
  Long bl=Long.valueOf(1);
  public void setBY(){
    synchronized (bl) {
      // 省略代码无数
    }
  }
}

```

## 注意事项

1. 对象的所有属性都是final的，并不能保证不可变性；
2. 不可变对象也需要正确发布。

final修饰的属性一旦被赋值，就不可以再修改，但是如果属性的类型是普通对象，那么这个对象的属性是可以被修改的。

**在使用Immutability模式的时候一定要确认保持不变性的边界在哪里，是否要求属性对象也具备不可变性。**

反例：

```java
class Foo{
  int age=0;
  int name="abc";
}
final class Bar {
  final Foo foo;
    // 可以对foo的属性进行修改
  void setAge(int a){
    foo.age=a;
  }
}

```

反例2：

```java
//Foo 线程安全
final class Foo{
  final int age=0;
  final int name="abc";
}
//Bar 线程不安全 持有对foo的引用，对这个foo的修改在多线程中并不能保证可见性和原子性
class Bar {
  Foo foo;
  void setFoo(Foo f){
    this.foo=f;
  }
}

```

如果你的程序仅仅需要foo保证可见性，无需保证原子性，那么可以将foo声明为volatile变量，这样就能保证可见性。如果你的程序需要保证原子性，那么可以通过原子类来实现。

```java
public class SafeWM {
  class WMRange{
    final int upper;
    final int lower;
    WMRange(int upper,int lower){
    // 省略构造函数实现
    }
  }
  final AtomicReference<WMRange>
    rf = new AtomicReference<>(
      new WMRange(0,0)
    );
  // 设置库存上限
  void setUpper(int v){
    while(true){
      WMRange or = rf.get();
      // 检查参数合法性
      if(v < or.lower){
        throw new IllegalArgumentException();
      }
      WMRange nr = new
          WMRange(v, or.lower);
      if(rf.compareAndSet(or, nr)){
        return;
      }
    }
  }
}

```

