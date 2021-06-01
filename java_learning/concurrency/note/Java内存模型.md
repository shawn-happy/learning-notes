## 什么是java内存模型
我们已经知道，导致可见性问题的是缓存，导致有序性问题的是编译优化，那么解决这两个问题的最直接的办法就是禁用缓存和编译优化。我们可以按需禁用缓存和编译优化。那我们，我们如何才能做到按需禁用呢？java内存模型是一个很复杂的规范，可以从不同的视角来解读，按照我们程序员的理解，Java内存模型规范了jvm如何提供按需禁用缓存和编译优化的方法。具体来说，这些方法包括：**volatile,synchronized和final这三个关键字，以及六项happens-before原则。**

## volatile
volatile在c语言时代就已经存在，它最原始的意义就是禁用cpu缓存。用于修饰变量，那么编译器对这个变量的读写，不能使用cpu缓存，必须从内存中读取或者写入。

```java
public class VolatileExample{
    int x = 0;
    volatile boolean v = false;
    public void write(){
        x = 42;
        v = true;
    }
    
    public void read(){
        if(v == true){
            // 这里x会是多少呢？
        }
    }
}
```

上面的程序，直觉看来x=42,但是这需要分清楚jdk的版本，如果是1.5之前的，那么x的值不一定就是42，但是1.5以后的版本，一定是42。这是由于1.5版本，对volatile语义进行了一次增强。利用happens-before原则。

## happens-before
概念：前面一个操作的结果对后续操作是可见的。也就是说，happens-before约束了编译器的优化行为，虽允许编译器优化，但是要求编译器优化后一定要遵守happens-before原则。

happens-before一共有六项规则：
### 程序的顺序性规则
前面的操作happens-before于后续的任意操作。

例如上面的代码
x=42happens-before于v=true;，比较符合单线程的思维，程序前面对某个变量的修改一定是对后续操作可见的。

### volatile变量规则
对一个volatile变量的写操作，happens-befores与后续对这个变量的读操作。
这怎么看都有禁用缓存的意思，貌似和1.5版本之前的语义没有变化，这个时候我们需要关联下一个规则来看这条规则。

### 传递性
a happens-before于 b,b happens-before于c，那么a happens-before与c。
1. x = 42 happens-before于 写变量v = true; —-规则1
2. 写变量v = true happens-before于 读变量v==true。—-规则2
3. 所以x = 42 happens-before于 读变量v == true;—-规则3

如果线程b读到了v= true,那么线程a设置的x=42对线程b是可见的。也就是说线程b能看到x=42。

### 管程中锁的原则
对一个锁的解锁happens-before于对后续这个锁的加锁操作。
synchronized是java里对管程的实现。

管程中的锁在java里是隐式实现的，例如下面的代码，在进入同步块之前，会自动加锁，而在代码块执行完会自动释放锁。加锁和解锁的操作都是编译器帮我们实现的。

```java
synchronzied(this){// 此处自动加锁
    // x 是共享变量，初始值是10；
    if(this.x < 12){
        this.x = 12;
    }
}// 此处自动解锁
```

根据管程中锁的原则，线程a执行完代码块后x的值变成12，线程B进入代码块时，能够看到线程a对x的写的操作，也就是线程b能看到x=12。


### start()
主线程a启动子线程b后，子线程B能够看到主线程a在启动子线程b前的操作。也就是在线程a中启动了线程b，那么该start()操作happens-before于线程b中任意操作。

```java
int x = 0;
Thread B = new Thread(()->{
    // 这里能看到变量x的值，x = 12;
});
x = 12;
B.start();
```

### join
主线程a等待子线程b完成(主线程a调用子线程b的join方法实现)，当b完成后(主线程a中join方法返回)，主线程a能够看到子线程b的任意操作。这里都是对共享变量的操作。

如果在线程a中调用了线程b的join()并成功返回，那么线程b中任意操作happens-before于该join操作的返回。

```java
int x = 0;
Thread b = new Thread(()->{
    x = 11;
});

x = 12;
b.start();
b.join();
// x = 11;
```

## final
final修饰变量时，初衷是告诉编译器：这个变量生而不变，可以可劲儿优化，这就导致在1.5版本之前优化的很努力，以至于都优化错了，双重检索方法创建单例，构造函数的错误重排导致线程可能看到final变量的值会变化。但是1.5以后已经对final修饰的变量的重排进行了约束。

## 总结
happens-before的语义本质上是一种可见性，a happens-before B意味着a对b是可见的，无论a b是否在同一个线程里。
java内存模型主要分为两部分，一部分面向开发人员，另外一部分是面向jvm的实现人员，我们只要重点关注前者。

## 思考
如何让一个共享变量在一个线程里赋值后，其他线程是可见的。
1. volitale
2. synchronized
3. join
