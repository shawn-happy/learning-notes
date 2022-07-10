小知识：这个限制可以通过字节码工具绕开。也就是说，在编译完成之后，我们可以再向class文件中添加方法名和参数类型相同，而返回类型不同的方法。当这种包括多个方法名相同、参数类型相同，而返回类型不同的方法的类，出现在Java编译器的用户类路径上时，它是怎么确定需要调用哪个方法的呢？当前版本的Java编译器会直接选取第一个方法名以及参数类型匹配的方法。并且，它会根据所选取方法的返回类型来决定可不可以通过编译，以及需不需要进行值转换等。

重载的方法在编译过程中即可完成识别。具体到每一个方法调用，java编译器会根据所传入参数的声明类型（注意与实际类型区分）来选取重载方法，选取的过程分三个阶段：

1. 在不考虑对基本类型auto-boxing，auto-unboxing，以及可变参数的情况下选取重载方法。
2. 如果1中没有找到适配的方法，那么在允许auto-(un)boxing，但不允许可变参数的情况下选取重载方法。
3. 如果2中没有找到适配的方法，那么允许auto-(un)boxing和可变参数的情况下选取重载方法。

如果java编译期在同一个阶段找到了多个适配的方法，那么它会在其中选择一个最为贴切的，而贴切程度取决于形参类型的继承关系。

如果子类定义了父类中非私有的同名方法，参数类型不同，那么在子类中也构成了重载关系。



如果子类定义了父类中非私有的同名方法，参数类型相同：

1. 如果两个方法都是静态方法，那么子类中的方法隐藏了父类中的方法。
2. 如果都不是静态，且都不是私有的，那么子类的方法重写了父类中的方法。





jvm识别方法的关键在于类名，方法名以及方法描述符（有方法的参数类型以及返回类型所构成）

在同一个类中，如果同时出现多个名字相同且描述符相同的方法，那么jvm在验证阶段就会报错。

名字+参数类型+返回值类型，jvm可以准确识别。

如果子类定义了父类中非私有，非静态的同名方法，并且参数类型一致返回值类型一直，jvm才会判定为重写。

静态绑定：在解析时便能够直接识别目标方法的情况

动态绑定：需要在运行过程中根据调用者的动态类型来识别目标方法的情况。





java字节码与调用相关的指令：

1. invokestatic: 用于调用静态方法
2. invokespecial:用于调用私有实例方法、构造器，以及使用super关键字调用父类的实例方法或者构造器，和所实现接口的默认方法。
3. invokevirtual:用于调用非私有实例方法。
4. invokeinterface: 用于调用接口方法。
5. invokedynamic: 用于调用动态方法。



```java

interface 客户 {
  boolean isVIP();
}

class 商户 {
  public double 折后价格(double 原价, 客户 某客户) {
    return 原价 * 0.8d;
  }
}

class 奸商 extends 商户 {
  @Override
  public double 折后价格(double 原价, 客户 某客户) {
    if (某客户.isVIP()) {                         // invokeinterface      
      return 原价 * 价格歧视();                    // invokestatic
    } else {
      return super.折后价格(原价, 某客户);          // invokespecial
    }
  }
  public static double 价格歧视() {
    // 咱们的杀熟算法太粗暴了，应该将客户城市作为随机数生成器的种子。
    return new Random()                          // invokespecial
           .nextDouble()                         // invokevirtual
           + 0.8d;
  }
}
```



在编译过程中，我们并不知道目标方法的具体内存地址。因此jvm会暂时用符号引用来表示该目标方法。包括目标方法所在类或接口的名字，以及目标方法的方法名和方法描述符。存放在常量池中。分为接口和非接口符号引用。

```

// 在奸商.class的常量池中，#16为接口符号引用，指向接口方法"客户.isVIP()"。而#22为非接口符号引用，指向静态方法"奸商.价格歧视()"。
$ javap -v 奸商.class ...
Constant pool:
...
  #16 = InterfaceMethodref #27.#29        // 客户.isVIP:()Z
...
  #22 = Methodref          #1.#33         // 奸商.价格歧视:()D
...
```

在执行使用了符号引用的字节码之前，jvm需要解析这些符号引用，并替换为实际引用。

对于非接口：

1. 在C中查找符号名字以及描述符的方法。
2. 如果没有找到，去c的父类，直到object。
3. 如果没有找到，在C所直接实现或者间接实现的接口中搜索，这一步搜索得到的目标方法必须是非私有，非静态的，并且，如果目标方法在间接实现的接口中，则需要满足c与该接口之间没有其他符合条件的目标方法，如果有，则任意返回其中一个。

从这个解析方法，静态方法也可以通过子类来调用，子类的静态方法会隐藏父类中同名，通描述符的静态方法。



对于接口：

1. 在I中查找符号名字和描述符的方法。
2. 如果没有，在object公有实例方法中搜索。
3. 如果没有，在I的超接口中搜索，这一步的搜索结果的要求与非接口3一致。



```java

abstract class Passenger {
  abstract void passThroughImmigration();
  @Override
  public String toString() { ... }
}
class ForeignerPassenger extends Passenger {
   @Override
   void passThroughImmigration() { /* 进外国人通道 */ }
}
class ChinesePassenger extends Passenger {
  @Override
  void passThroughImmigration() { /* 进中国人通道 */ }
  void visitDutyFreeShops() { /* 逛免税店 */ }
}

Passenger passenger = ...
passenger.passThroughImmigration();
```



方法表（以InvokeVirtual）

本质上是一个数组，每个数组元素指向一个当前类及其祖先类中非私有的实例方法。

两个性质：

1. 子类方法表包含父类方法表中所有方法；
2. 子类方法在方法表中的索引值，与它重写父类方法的索引值相同。

静态绑定的方法，实际引用将指向具体的目标方法

动态--> 实际引用则是方法表的索引值。

在执行过程，jvm将获取调用者的实际类型，并在该实际类型的方法表中，根据索引值获取目标方法。这个过程就是动态绑定。

 ![img](https://static001.geekbang.org/resource/image/f1/c3/f1ff9dcb297a458981bd1d189a5b04c3.png) 





内联缓存：一种加快动态绑定优化技术，它能够缓存虚方法调用中调用者的动态类型，以及该类型所对应的目标方法，在之后的执行过程中，如果碰到已经缓存的类型，内联缓存便会直接调用该类型所对应的目标方法，如果没有碰到已缓存的类型，内联缓存则会退化至使用基于方法表的动态绑定。



```java

// Run with: java -XX:CompileCommand='dontinline,*.passThroughImmigration' Passenger
public abstract class Passenger {
   abstract void passThroughImmigration();
  public static void main(String[] args) {
    Passenger a = new ChinesePassenger();
  Passenger b = new ForeignerPassenger();
    long current = System.currentTimeMillis();
    for (int i = 1; i <= 2_000_000_000; i++) {
      if (i % 100_000_000 == 0) {
        long temp = System.currentTimeMillis();
        System.out.println(temp - current);
        current = temp;
      }
      Passenger c = (i < 1_000_000_000) ? a : b;
      c.passThroughImmigration();
  }
  }
}
class ChinesePassenger extends Passenger {
  @Override void passThroughImmigration() {} 
}
class ForeignerPassenger extends Passenger {
  @Override void passThroughImmigration() {}
}
```

