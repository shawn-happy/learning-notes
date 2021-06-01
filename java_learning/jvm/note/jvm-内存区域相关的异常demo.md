# 运行时数据区相关异常

## 程序计数器

此区域内是线程私有的，切不会抛出任何异常。如果这一部分抛出异常，那程序的执行就会非常混乱了。

## java虚拟机栈

此区域会抛出:

1. StackOverflowError
2. OutofMemoryError

**抛出StackOverflowError的原因：**

1. 如果线程请求的栈深度大于虚拟机所允许的最大深度，将会抛出StackOverflowError的错误。

**模拟：**

1. 设置-Xss的大小，可以设置的比较小
2. 定义大量的本地变量，增大此栈帧中的本地变量表的长度。

**Demo:**

```java
private int stackLength = 1;

public void recursion(){
 stackLength ++;
 recursion();
}

/*
	设置-Xss的大小，可以设置的比较小
	-Xss128k
*/
public static void main(String[] args) {
 StackOverFlowDemo demo = new StackOverFlowDemo();
 try {
  demo.recursion();
 } catch (Throwable e) {
  System.out.println("current stack depth: " + demo.stackLength);
  throw e;
 }
}
/*
output:
current stack depth: 993
Exception in thread "main" java.lang.StackOverflowError
	at com.shawn.jvm.StackOverFlowDemo.recursion(StackOverFlowDemo.java:13)
	at com.shawn.jvm.StackOverFlowDemo.recursion(StackOverFlowDemo.java:14)
*/
```

```java
private static long stackLength = 0;

private static void test() {
  long l1, l2, l3, l4, l5, l6, l7, l8, l9, l10,
      l11, l12, l13, l14, l15, l16, l17, l18, l19, l20,
      l21, l22, l23, l24, l25, l26, l27, l28, l29, l30,
      l31, l32, l33, l34, l35, l36, l37, l38, l39, l40,
      l41, l42, l43, l44, l45, l46, l47, l48, l49, l50,
      l51, l52, l53, l54, l55, l56, l57, l58, l59, l60,
      l61, l62, l63, l64, l65, l66, l67, l68, l69, l70,
      l71, l72, l73, l74, l75, l76, l77, l78, l79, l80,
      l81, l82, l83, l84, l85, l86, l87, l88, l89, l90,
      l91, l92, l93, l94, l95, l96, l97, l98, l99, l100;
  stackLength++;
  test();

  l1 = l2 = l3 = l4 = l5 = l6 = l7 = l8 = l9 = l10 =
      l11 = l12 = l13 = l14 = l15 = l16 = l17 = l18 = l19 = l20 =
          l21 = l22 = l23 = l24 = l25 = l26 = l27 = l28 = l29 = l30 =
              l31 = l32 = l33 = l34 = l35 = l36 = l37 = l38 = l39 = l40 =
                  l41 = l42 = l43 = l44 = l45 = l46 = l47 = l48 = l49 = l50 =
                      l51 = l52 = l53 = l54 = l55 = l56 = l57 = l58 = l59 = l60 =
                          l61 = l62 = l63 = l64 = l65 = l66 = l67 = l68 = l69 = l70 =
                              l71 = l72 = l73 = l74 = l75 = l76 = l77 = l78 = l79 = l80 =
                                  l81 = l82 = l83 = l84 = l85 = l86 = l87 = l88 = l89 = l90 =
                                      l91 = l92 = l93 = l94 = l95 = l96 = l97 = l98 = l99 = l100 = 0;
}

public static void main(String[] args) {
  try{
    test();
  }catch (Throwable t){
    System.err.println("stack length: " + stackLength);
    throw t;
  }
}
```

**抛出OutofMemoryError的原因：**

1. 如果虚拟机的栈内存允许动态扩展，当扩展栈容量无法申请到足够的内存时就会抛出`OutofMemoryError`

**模拟：**

假设操作系统分配给每个进程的内存限制最大是2G，jvm通过参数设置了java heap和Method area的最大内存，假设是512m，直接内存也可以通过参数指定，比如512M，再加上jvm本身使用的内存空间，pc寄存器的内存使用状态可以忽略，那还是2G-0.5G*3=512M左右是留给java虚拟机栈和java本地方法栈的，如果这部分的空间因为虚拟机栈的动态扩展，也会操作无法申请到足够的内存，导致oom。

**DEMO:**

```java
while (true){
  new Thread(() -> {
    while (true){
      System.out.println("11111");
    }
  }).start();
}
```

<span style="color: red"><b>注意：不要在window系统上运行，会造成系统卡死。</b></span>

**异常信息：**

`unable to create new native thread`

## java本地方法栈

与java虚拟机栈类似。

## java堆

是OOM的主要发生区域。

**异常信息：**`java heap space`

模拟：只需要不断的创建对象，并且保证GC Root到对象之间有可达路径来避免对象被回收。

```java
/**
 * VM -Args -Xms6m -Xmx6m -XX:+HeapDumpOnOutOfMemoryError
 * 演示运行时数据区-java heap中出现的OOM异常情况
 * @author shawn
 */
public class HeapDemo {

 public static void main(String[] args) {
  List<Object> list = new ArrayList<>();
  int i = 0;
  try {
   while (true){
    byte[] bytes = new byte[1024];
    list.add(bytes);
    i++;
   }
  } catch (Throwable e) {
   long maxMemory = Runtime.getRuntime().maxMemory();
   long totalMemory = Runtime.getRuntime().totalMemory();
   System.out.println("i = " + i);
   System.out.println("maxMemory = " + maxMemory);
   System.out.println("totalMemory = " + totalMemory);
   throw e;
  }
 }
}
```

<span style="color: red">注意：生成的内存映射文件可以是Eclipse的MAT工具进行分析</span>

## 非堆和运行时常量池

### 运行时常量池

主要异常: OOM

**异常信息：**

使用的vm args: -XX:PermSize=6M -XX:MaxPermSize=6M

1. jdk7之前（不包括7），例如jdk6的异常信息：`PermGen space`
2. jdk7之后包括7，继续使用-XX:MaxPermSize或者Jdk8及其以后的版本的参数-XX:MaxMetaspaceSize,都不会复现`PermGen space`这种异常信息。是因为从jdk7开始，把之前存放在方法区的字符串常量池迁移到了java堆中了。所以修改方法区的容量对该测试用例毫无意义了，但是使用-Xmx设置最大的堆内存容量为6M，会看到java heap space的异常信息。

**DEMO:**

```java
/**
 * jdk7之前的版本，不包括7： vm -Args: -XX:PermSize=6M -XX:MaxPermSize=6M
 * jdk7及其以后的版本：vm -Args: -Xms10m -Xmx10m -XX:+PrintGCDetails
 * @author shawn
 */
public class RuntimeConstantPoolOOMDemo {
 static String  base = "string";
 public static void main(String[] args) {
  List<String> list = new ArrayList<String>();
  for (int i=0;i< Integer.MAX_VALUE;i++){
   String str = base + base;
   base = str;
   list.add(str.intern());
  }
 }
}
```

注意：

```java
String str = new StringBuilder("计算机").append("软件").toString();
System.out.println("str.intern() == str: " + (str.intern() == str));

String java = "虚拟机";
String str2 = new StringBuilder("虚拟").append("机").toString();
System.out.println("java.intern() == str2: " + (java.intern() == str2));
```

上面的代码如果在jdk6的运行结果都是false，在7及其以后的版本一个true, 一个false。

原因：

1. jdk6：intern方法会把首次遇到的字符串实例复制到perm的字符串常量池中存储，返回的也是永久代里面的这个字符串实例的引用，但是StringBuilder创建的对象字符串实例是在java堆中，必然不可能是同一个引用，所以是false。
2. =>jdk7: 因为现在字符串常量池已经迁移到了java堆中，那只需要记录一次首次出现的字符串实例引用即可。因此intern()返回的引用和由StringBuilder创建的那个字符串实例就是同一个。

### 非堆其他区域

```java
public static void main(String[] args){
    while(true){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(OOMObject.class);
        enhancer.setUseCache(false);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                return methodProxy.invokeSuper(obj, args);
            }
        });
        enhancer.create();
    }
    
    static class OOMObject{
        
    }
}
```

jdk7: `PermGen space`

## 直接内存

容量可以通过-XX:MaxDirectMemorySize参数来指定。

```java
public static void main(String[] args) throws Throwable {
 System.out.println("配置的MaxDirectMemorySize"
  + VM.maxDirectMemory()/(double)1024/1024+"MB");

 TimeUnit.SECONDS.sleep(2);

 // ByteBuffer.allocate(); 分配JVM的堆内存，属于GC管辖
 // ByteBuffer.allocateDirect() ; // 分配本地OS内存，不属于GC管辖
 ByteBuffer byteBuffer = ByteBuffer.allocateDirect(6 * 1024 * 1024);
 // java.lang.OutOfMemoryError: Direct buffer memory
}
```

异常信息：Direct buffer memory

## 其余OOM相关的错误

参考文章：

英文原文：https://plumbr.io/outofmemoryerror

中文参考：https://renfufei.blog.csdn.net/article/details/78061354