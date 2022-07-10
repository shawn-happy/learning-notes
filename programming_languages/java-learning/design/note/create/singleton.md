# Singleton Design Pattern
## 什么是单例模式
**单例设计模式（Singleton Design Pattern）**理解起来非常简单。一个类只允许创建一个对象（或者实例），那这个类就是一个单例类，这种设计模式就叫作单例设计模式，简称单例模式。

## 为什么要使用单例模式

从业务概念上，有些数据在系统中只应该保存一份，就比较适合设计为单例模式。比如，系统的配置信息类。除此之外，我们还可以使用单例解决资源访问冲突的问题。

### 场景

* 处理资源访问冲突
* 全局唯一性
* 配置类

* windows任务管理器
* windows回收站
* 全局配置信息类
* 计数器
* 数据库连接池
* servlet
* spring bean
* ....

## 如何实现单例模式

### 饿汉式

```java
/**
 * 基于饿汉式实现的单例模式
 * @author shao
 */
public class Hungry {

	private static final Hungry instance = new Hungry();

	/**
	 * 提供无参构造
	 */
	private Hungry(){

	}

	public Hungry getInstance(){
		return instance;
	}

}
```

* 优点
  * 线程安全
  * 实现简单
  * 没有加锁，执行效率会提高
* 缺点
  * 不支持lazy加载
  * 类加载时就初始化，浪费内存

### 懒汉式

```java
/**
 * 懒汉式 线程安全
 * @author shawn
 */
public class LazyMan {

	private LazyMan() {

	}

	private static LazyMan lazyMan;

	public static synchronized LazyMan getInstance() {
		if (lazyMan == null) {
			lazyMan = new LazyMan();
		}
		return lazyMan;
	}

}

```

* 优点
  * 线程安全
  * 支持lazy加载
  * 第一次调用才初始化，避免内存浪费。
* 缺点
  * 必须加锁 synchronized 才能保证单例，但加锁会影响效率。

如果这个单例类偶尔会被用到，那这种实现方式还可以接受。但是，如果频繁地用到，那频繁加锁、释放锁及并发度低等问题，会导致性能瓶颈，这种实现方式就不可取了。

### 双检锁/双重校验锁（DCL，即 double-checked locking）

```java
/**
 * double-checked locking
 * @author  shawn
 */
public class DoubleCheckedLocking {

	private volatile static DoubleCheckedLocking singleton;

	private DoubleCheckedLocking (){}

	public static DoubleCheckedLocking getSingleton() {
		if (singleton == null) {
			synchronized (DoubleCheckedLocking.class) {
				if (singleton == null) {
					singleton = new DoubleCheckedLocking();
				}
			}
		}
		return singleton;
	}

}
```

解决了饿汉式和懒汉式的缺点

`volatile`解决了指令重排的问题

<pre>
singleton = new DoubleCheckedLocking();
1. 分配内存
2. 执行构造方法
3. 指向地址    
</pre>

但是实际上，只有很低版本的 Java 才会有这个问题。我们现在用的高版本的 Java 已经在 JDK 内部实现中解决了这个问题（解决的方法很简单，只要把对象 new 操作和初始化操作设计为原子操作，就自然能禁止重排序）

### 静态内部类

```java
/**
 * Static inner class
 * @author com.shawn
 */
public class StaticInnerClass {

	private StaticInnerClass(){

	}

	public static StaticInnerClass getInstance(){
		return InnerClass.INTANCE;
	}

	private static class InnerClass{

		private static final StaticInnerClass INTANCE = new StaticInnerClass();

	}

}
```

`InnerClass`是一个静态内部类，当外部类 `StaticInnerClass`被加载的时候，并不会创建 `InnerClass`实例对象。只有当调用 getInstance() 方法时，`InnerClass`才会被加载，这个时候才会创建 instance。insance 的唯一性、创建过程的线程安全性，都由 JVM 来保证。所以，这种实现方法既保证了线程安全，又能做到延迟加载。

### 枚举

```java
/**
 * enum singleton
 * @author shawn
 */
public enum  EnumSingleton {

	/**
	 * 单例模式
	 */
	INSTANCE;

	public EnumSingleton getInstance(){
		return INSTANCE;
	}

}

```



## 单例模式存在哪些问题

* 单例对 OOP 特性的支持不友好

  一旦你选择将某个类设计成到单例类，也就意味着放弃了继承和多态这两个强有力的面向对象特性，也就相当于损失了可以应对未来需求变化的扩展性

* 单例会隐藏类之间的依赖关系

  单例类不需要显示创建、不需要依赖参数传递，在函数中直接调用就可以了。如果代码比较复杂，这种调用关系就会非常隐蔽。在阅读代码的时候，我们就需要仔细查看每个函数的代码实现，才能知道这个类到底依赖了哪些单例类。

* 单例对代码的扩展性不友好

  单例类只能有一个对象实例。如果未来某一天，我们需要在代码中创建两个实例或多个实例，那就要对代码有比较大的改动。

* 单例对代码的可测试性不友好

  单例模式的使用会影响到代码的可测试性。如果单例类依赖比较重的外部资源，比如 DB，我们在写单元测试的时候，希望能通过 mock 的方式将它替换掉。而单例类这种硬编码式的使用方式，导致无法实现 mock 替换。

* 单例不支持有参数的构造函数

  单例不支持有参数的构造函数，比如我们创建一个连接池的单例对象，我们没法通过参数来指定连接池的大小。针对这个问题，我们来看下都有哪些解决方案。

  第一种解决思路是：创建完实例之后，再调用 init() 函数传递参数。需要注意的是，我们在使用这个单例类的时候，要先调用 init() 方法，然后才能调用 getInstance() 方法，否则代码会抛出异常。

  ```java
  
  public class Singleton {
    private static Singleton instance = null;
    private final int paramA;
    private final int paramB;
  
    private Singleton(int paramA, int paramB) {
      this.paramA = paramA;
      this.paramB = paramB;
    }
  
    public static Singleton getInstance() {
      if (instance == null) {
         throw new RuntimeException("Run init() first.");
      }
      return instance;
    }
  
    public synchronized static Singleton init(int paramA, int paramB) {
      if (instance != null){
         throw new RuntimeException("Singleton has been created!");
      }
      instance = new Singleton(paramA, paramB);
      return instance;
    }
  }
  
  Singleton.init(10, 50); // 先init，再使用
  Singleton singleton = Singleton.getInstance();
  ```

  第二种解决思路是：将参数放到 getIntance() 方法中。

  ```java
  
  public class Singleton {
    private static Singleton instance = null;
    private final int paramA;
    private final int paramB;
  
    private Singleton(int paramA, int paramB) {
      this.paramA = paramA;
      this.paramB = paramB;
    }
  
    public synchronized static Singleton getInstance(int paramA, int paramB) {
      if (instance == null) {
        instance = new Singleton(paramA, paramB);
      }
      return instance;
    }
  }
  
  Singleton singleton = Singleton.getInstance(10, 50);
  ```

  第三种解决思路是：将参数放到另外一个全局变量中。具体的代码实现如下。Config 是一个存储了 paramA 和 paramB 值的全局变量。里面的值既可以像下面的代码那样通过静态常量来定义，也可以从配置文件中加载得到。实际上，这种方式是最值得推荐的

  ```java
  
  public class Config {
    public static final int PARAM_A = 123;
    public static fianl int PARAM_B = 245;
  }
  
  public class Singleton {
    private static Singleton instance = null;
    private final int paramA;
    private final int paramB;
  
    private Singleton() {
      this.paramA = Config.PARAM_A;
      this.paramB = Config.PARAM_B;
    }
  
    public synchronized static Singleton getInstance() {
      if (instance == null) {
        instance = new Singleton();
      }
      return instance;
    }
  }
  ```

## 如何理解单例的唯一性

“一个类只允许创建唯一一个对象”。那对象的唯一性的作用范围是什么呢？是指线程内只允许创建一个对象，还是指进程内只允许创建一个对象？答案是后者，也就是说，单例模式创建的对象是进程唯一的。

## 如何实现线程中的唯一性

HashMap

ThreadLocal

## 如何实现集群下的单例

我们需要把这个单例对象序列化并存储到外部共享存储区（比如文件）。进程在使用这个单例对象的时候，需要先从外部共享存储区中将它读取到内存，并反序列化成对象，然后再使用，使用完成之后还需要再存储回外部共享存储区。为了保证任何时刻，在进程间都只有一份对象存在，一个进程在获取到对象之后，需要对对象加锁，避免其他进程再将其获取。在进程使用完这个对象之后，还需要显式地将对象从内存中删除，并且释放对对象的加锁。

## 如何实现多例模式

“单例”指的是，一个类只能创建一个对象。对应地，“多例”指的就是，一个类可以创建多个对象，但是个数是有限制的，比如只能创建 3 个对象。如果用代码来简单示例一下的话，就是下面这个样子：

```java

public class BackendServer {
  private long serverNo;
  private String serverAddress;

  private static final int SERVER_COUNT = 3;
  private static final Map<Long, BackendServer> serverInstances = new HashMap<>();

  static {
    serverInstances.put(1L, new BackendServer(1L, "192.134.22.138:8080"));
    serverInstances.put(2L, new BackendServer(2L, "192.134.22.139:8080"));
    serverInstances.put(3L, new BackendServer(3L, "192.134.22.140:8080"));
  }

  private BackendServer(long serverNo, String serverAddress) {
    this.serverNo = serverNo;
    this.serverAddress = serverAddress;
  }

  public BackendServer getInstance(long serverNo) {
    return serverInstances.get(serverNo);
  }

  public BackendServer getRandomInstance() {
    Random r = new Random();
    int no = r.nextInt(SERVER_COUNT)+1;
    return serverInstances.get(no);
  }
}
```

实际上，对于多例模式，还有一种理解方式：同一类型的只能创建一个对象，不同类型的可以创建多个对象。这里的“类型”如何理解呢？

```java

public class Logger {
  private static final ConcurrentHashMap<String, Logger> instances
          = new ConcurrentHashMap<>();

  private Logger() {}

  public static Logger getInstance(String loggerName) {
    instances.putIfAbsent(loggerName, new Logger());
    return instances.get(loggerName);
  }

  public void log() {
    //...
  }
}

//l1==l2, l1!=l3
Logger l1 = Logger.getInstance("User.class");
Logger l2 = Logger.getInstance("User.class");
Logger l3 = Logger.getInstance("Order.class");
```

## 如何利用反射来破坏单例

```java
	public static void main(String[] args) {
		try {
			LazyMan lazyMan1 = LazyMan.getInstance();
			Constructor<LazyMan> declaredConstructor = LazyMan.class.getDeclaredConstructor(null);
			declaredConstructor.setAccessible(true);
			LazyMan lazyMan2 = declaredConstructor.newInstance();
			System.out.println(lazyMan1.hashCode());
			System.out.println(lazyMan2.hashCode());
			System.out.println(lazyMan1 == lazyMan2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
```

解决方案：

```java
public class LazyMan {
    private LazyMan() {
        synchronized (LazyMan.class) {
            if (lazyMan != null) {
                throw new RuntimeException("不要试图用反射破坏单例模式");
            }
        }
    }

    private volatile static LazyMan lazyMan;

    public static LazyMan getInstance() {
        if (lazyMan == null) {
            synchronized (LazyMan.class) {
                if (lazyMan == null) {
                    lazyMan = new LazyMan();
                }
            }
        }
        return lazyMan;
    }
}
```

但是这种写法还是有问题：

上面我们是先正常的调用了getInstance方法，创建了LazyMan对象，所以第二次用反射创建对象，私有构造函数里面的判断起作用了，反射破坏单例模式失败。但是如果破坏者干脆不先调用getInstance方法，一上来就直接用反射创建对象，我们的判断就不生效了：

```java
public static void main(String[] args) {
        try {
            Constructor<LazyMan> declaredConstructor = LazyMan.class.getDeclaredConstructor(null);
            declaredConstructor.setAccessible(true);
            LazyMan lazyMan1 = declaredConstructor.newInstance();
            LazyMan lazyMan2 = declaredConstructor.newInstance();
            System.out.println(lazyMan1.hashCode());
            System.out.println(lazyMan2.hashCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
}
```

那么如何防止这种反射破坏呢？

```java
public class LazyMan {
    private static boolean flag = false;
    private LazyMan() {
        synchronized (LazyMan.class) {
            if (flag == false) {
                flag = true;
            } else {
                throw new RuntimeException("不要试图用反射破坏单例模式");
            }
        }
    }
    private volatile static LazyMan lazyMan;
    public static LazyMan getInstance() {
        if (lazyMan == null) {
            synchronized (LazyMan.class) {
                if (lazyMan == null) {
                    lazyMan = new LazyMan();
                }
            }
        }
        return lazyMan;
    }
}
```

看起来很美好，但是还是不能完全防止反射破坏单例模式，因为可以利用反射修改flag的值。

枚举可以彻底解决：

```java
    @CallerSensitive
    public T newInstance(Object ... initargs)
        throws InstantiationException, IllegalAccessException,
               IllegalArgumentException, InvocationTargetException
    {
        if (!override) {
            if (!Reflection.quickCheckMemberAccess(clazz, modifiers)) {
                Class<?> caller = Reflection.getCallerClass();
                checkAccess(caller, clazz, null, modifiers);
            }
        }
                   // 这句话，如果对象是枚举类型，抛异常
        if ((clazz.getModifiers() & Modifier.ENUM) != 0)
            throw new IllegalArgumentException("Cannot reflectively create enum objects");
        ConstructorAccessor ca = constructorAccessor;   // read volatile
        if (ca == null) {
            ca = acquireConstructorAccessor();
        }
        @SuppressWarnings("unchecked")
        T inst = (T) ca.newInstance(initargs);
        return inst;
    }
```



