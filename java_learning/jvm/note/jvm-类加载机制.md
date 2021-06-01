---
typora-root-url: image
---

# 类加载机制

## jvm加载类的顺序

![jvm-类加载到卸载](/jvm-类加载到卸载.png)

jvm类加载顺序：加载，链接，初始化。链接过程需要验证，准备，解析，但是解析阶段不一定非要在初始化开始之前执行，也可以在初始化之后解析，原因是为了支持java语言的运行时绑定。

* **加载：**查找并加载类的二进制数据

* **链接：**

  * **验证：**保证被加载的类的正确性；

  * **准备：**给类静态变量分配内存空间，赋值一个默认的初始值；

  * **解析：**把类中的符号引用转换为直接引用;

    >在把java编译为class文件的时候，虚拟机并不知道所引用的地址；助记符：符号引用！
    >转为真正的直接引用，找到对应的直接地址！

* **初始化：**给类的静态变量赋值正确的值；

## 加载

1. **通过一个类的全限定名来获取定义此类的二进制字节流**。
2. 将这个字节流所代表的静态存储结构转化为方法区的运行时数据结构
3. 在java堆中生成一个代表这个类的java.lang.Class对象，作为方法区这些数据的访问入口。

## 链接

### 验证

目的：确保class文件的字节流中包含的**信息符合当前jvm的要求**，并且不会危害jvm自身的**安全**。

分为以下四个阶段：

1. 文件格式验证：验证字节流是否符合class文件格式的规范，并且能被当前jvm处理。主要目的**保证输入的字节流能正确地解析并存储于方法区之内。**
2. 元数据验证：对字节码描述的信息进行语义分析，主要目的：**保证其描述的信息符合java语言规范的要求。**
3. 字节码验证：进行数据流和控制流分析。主要目的：**保证被校验类的方法在运行时不会做出危害jvm安全的行为。**
4. 符号引用验证：符合引用转化为直接引用，**在解析阶段的时候发生**，主要目的：**确保解析动作能够正常执行。**

### 准备

目的：为类变量分配内存并设置类变量的初始值，这些内存在**方法区**里分配。

`public static int value = 123;// 变量value在准备阶段过后的初始值是0而不是123，在初始化阶段过后才是123`

### 解析

目的：将符号引用转化为直接引用。

>什么是符号引用？
>
>
>
>什么是直接引用？
>
>可以是直接指向目标的指针，相对偏移量，句柄等。

四种引用：

* CONSTANT_Class_info：类或接口的解析
* CONSTANT_Fieldref_info：字段解析
* CONSTANT_Methodref_info：方法解析
* CONSTANT_InterfaceMethodref_info：接口方法解析

## 初始化

到了初始化阶段，才真正开始执行类的定义的java code。

执行类构造器`<clint>()`方法。

# 类加载器

* **Bootstrap ClassLoader:**负责加载`java_home/lib`目录下的或者`-Xbootclasspath`参数所指定的路径中的。
* **Extension ClassLoader:** 负责加载`java_home/lib/ext`目录下的或者`java.ext.dirs`系统变量所指定的路径中的。
* **Application ClassLoader：**`CLASSPATH`上所指定的类库。
* **User ClassLoader： **自定义ClassLoader.

# 双亲委派机制



![jvm-双亲委派机制](jvm-双亲委派机制.png)

**双亲委派机制 可以保护java的核心类不会被自己定义的类所替代**

**一层一层的让父类去加载，如果顶层的加载器不能加载，然后再向下类推**

# 其余

* 基本类型：由jvm预先定义好的。

* 引用类型：类，接口，数组类，泛型。由于泛型会在编译过程中被擦除，因此jvm实际上只有前三种。

​					数组是由jvm直接生成的（递归加载引用类型），其他两种则有对应的字节流。

* 字节流：.class文件。java applet,jsp。

# 总结：

```java
public static int value = 123;
/*
加载：
	javac--> .class。通过类加载器加载到jvm

链接：
	验证：保证class信息正确，安全
	准备：分配内存空间，value = 0;
	解析：符合引用-->直接引用

初始化：
	value = 123;

*/


public class Parent{
    public static int value = 123;
    static{
        System.out.println(SuperClass);
    }
}

public class Child extends Parent{
    static {
        System.out.println("Child Class!");
    }
}
// -XX:+TraceClassLoading // 用于追踪类的加载信息并打印出来
public class Test{
     public static void main(String[] args) {
        System.out.println(Child.value);
        // 运行的结果
        /**
         * Child Class!
         * 123
         */
         // 原因
         /*
         对于静态字段，只有直接定义这个字段的类才会被初始化。
         */
    }
}


public class Test2{
     public static void main(String[] args) {
        Parent[] arr = new Parent[10];
        // 运行的结果
        /**
         * 父类是Object,没有触发Parent的初始化阶段
         */
    }
}

public class Constant{
    static {
        System.out.println("Constant Class!");
    }
    public static final String STR = "Hello, World";
}

public class Test3{
     public static void main(String[] args) {
       System.out.println(Constant.STR);
        // 运行的结果
        /**
         * Hello, World
         */
         // 原因
         /**
         * 编译阶段Hello, World存储到了Test3类的常量池中，对Constant.STR引用实际转换为
         * Test3对自身常量池的引用了，无对Constant的符号引用
         */
    }
}

public class Constant2{
    static {
        System.out.println("Constant2 Class!");
    }
    public static final String STR = UUID.randomUUID().toString();
}

public class Test4{
     public static void main(String[] args) {
       System.out.println(Constant.STR);
        // 运行的结果
        /**
         * constant2 class
			6f104466-a52b-4a3b-b7ba-9d40d0e80cae
         */
         // 原因
         /**
         * 常量没有在编译时就可以确定，无法存放在常量池中，所以还是会初始化它原本所在的类
         */
    }
}


```

**jvm何时初始化**

1. **jvm启动时，初始化用户指定的类；**
2. **当遇到用以新建目标类实例的new指令时，初始化new指令的目标类；**
3. **调用静态方法，初始化静态方法所在的类；**
4. **静态字段**
5. **子类初始化，先触发父类的初始化。**
6. **接口，default方法，那么直接实现或者间接实现该接口的类的初始化，会触发该接口的初始化；**
7. **使用反射API对某个类进行反射调用，初始化这个类；**
8. **当初次调用methodhandle实例时，初始化该methodhandle指向的方法所在类。**





