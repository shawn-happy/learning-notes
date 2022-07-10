基本类型能够在执行效率以及内存使用两方面提升软件性能。



```java
public class Foo{
    public static void main(String[] args) {
        boolean flag = true;
        if(flag){
            System.out.println("吃了！");
        }
        if(true == flag){
            System.out.println("真吃了！");
        }
    }
}
```

在java语言规范中，boolean类型的值只有两种，true / false。但是这两个值并不能被jvm直接使用。

在jvm规范中，boolean类型则被映射成int类型，具体来说，true被映射成1,false为0。

```
 0: iconst_1
         1: istore_1
         2: iload_1
         3: ifeq          14
         6: getstatic     #2                  // Field java/lang/System.out:Ljava/io/PrintStream;
         9: ldc           #3                  // String 吃了！
        11: invokevirtual #4                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
        14: iconst_1
        15: iload_1
        16: if_icmpne     27
        19: getstatic     #2                  // Field java/lang/System.out:Ljava/io/PrintStream;
        22: ldc           #5                  // String 真吃了！
        24: invokevirtual #4                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
        27: return
```

第一个if语句会被编译成ifeq，如果局部变量flag的值为0，那么就跳过打印语句

第二个if语句会被编译成if_icmpne,如果flag的值!=1,则跳过打印语句。

 ![img](https://static001.geekbang.org/resource/image/77/45/77dfb788a8ad5877e77fc28ed2d51745.png) 



java的基本类型，都有对应的值域和默认值，byte,short,char,int,long,float,double的值域一次扩大，而且前面的值域被后面的包含，因此，前面的基本类型无需强制转换为后面的基本类型，默认值在内存里都是0.

boolean和char是无符号类型。通常我们可以认定char类型的值为非负数，可以作为数组的索引等。

声明char,byte,short类型的局部变量，可以存储超出他们取值范围的数值。

java的浮点类型采用IEEE 754浮点数格式。以float为例，浮点类型通常有两个0,+0.0F以及-0.0F。

在java里是0，后者是符号位为1，其他位均为0的浮点数，在内存中等同于十六进制整数，+0.0F==-0.0F.

正无穷=任意正浮点数 / +0.0F

负无穷 = 任意正浮点数 / -0.0F

0x7F800000和0xFF800000

[0x7F800001,0x7FFFFFFF]和[0xFF800001,0xFFFFFFFF]对应的是NaN（not - a number）

+0.0F/-0.0F = 0x7FC00000  为标准NaN，其余为不标准。

NaN != 始终返回true，其他结果均为false；

### java基本类型的大小

解释栈帧有两个主要组成部分，分别是局部变量区，以及字节码的操作数栈，这里的局部变量是广义的。

jvm中局部变量区== 一个数组，可以用正整数来索引。除了long,double值需要两个数组单元来存储之外，其他基本类型以及引用类型的值均占用一个数组单元。



byte,short,char,int在栈上占用的空间和int是一样的，和引用类型也是一样的。32位4个字节，64位8个字节。

这种情况存在于局部变量，而并不会出现在存储于堆中的字段或者数组，在对上分别是1 2 2

boolean比较特殊，Boolean字段占用一个字节，而boolean数组是用byte数组实现的。为了保证堆中的boolean合法性，jvm在存储时显式地进行掩码操作。只取最后一位值存入boolean字段或者数组中。

boolean,byte,char,short加载到操作数栈上，而后将栈上的值当成int来运算。

boolean,char两个无符号类型，char的大小为2个字节，在加载char的值会被复制到Int类型的低二字节。高位补0.

short的大小为2个字节，short的值会被复制到int类型的低二字节，如果该short为非负数，那么最高位0，否则1