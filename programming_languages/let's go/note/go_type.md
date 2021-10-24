## Primitive

### Boolean 

* 类型名称：bool
* 值只有：true, false。默认是false，不可写为0或1
* 接收表达式，不支持自动或强制类型转换

```golang
var v1 bool
v2:=(1==2)
```

### Numeric Types

#### Integers

##### Signed Integers

| type  | value rang             |
| ----- | ---------------------- |
| int8  | -2^7^ ~ 2^7^-1         |
| int16 | -2^15^ ~ 2^15^-1       |
| int32 | -2^31^ ~ 2^31^-1       |
| int64 | -2^63^ ~ 2 ^63^-1      |
| int   | 与平台有关，至少是32位 |

##### UnSigned Integers

| type   | value rang             |
| ------ | ---------------------- |
| uint8  | -0 ~ 2^8^-1            |
| uint16 | -0 ~ 2^16^-1           |
| uint32 | -0 ~ 2^32^-1           |
| uint64 | -0 ~ 2^64^-1           |
| uint   | 与平台有关，至少是32位 |

#### Float

* **float32** ：IEEE-754 32位浮点型数
* **float64**： IEEE-754 64位浮点型数

```golang
//1、浮点型分为float32(类似C中的float)，float64(类似C中的double)
var f1 float32
f1=12     //不加小数点，被推导为整型
f2:=12.0  //加小数点，被推导为float64
f1=float32(f2)  //需要执行强制转换
//2、浮点数的比较
//浮点数不是精确的表达方式，不能直接使用“==”来判断是否相等，可以借用math的包math.Fdim
```

#### Complex Numbers

* **complex64**：32 位实数和虚数
* **complex128**：64 位实数和虚数

```golang
//1、复数的表示
var v1 complex64
v1=3.2+12i
//v1 v2 v3 表示为同一个数
v2:=3.2+12i
v3:=complex(3.2,12)
//2、实部与虚部
//z=complex(x,y),通过内置函数实部x=real(z),虚部y=imag(z)
```

### operator

假定 a值为 10，b 值为 20。

#### Arithmetic operator

| operator | describe       | example |
| :------- | :------------- | :------ |
| +        | Addition       | a+b=30  |
| -        | subtraction    | a-b=-10 |
| *        | multiplication | a*b=200 |
| /        | division       | b/a=2   |
| %        | remainder      | b%a=0   |
| ++       | auto-increment | a++=11  |
| --       | auto-decrement | a--=9   |

code example:

```golang
var a int = 21
var b int = 10
var c int
c = a + b
fmt.Printf("第一行 - c 的值为 %d\n", c )
c = a - b
fmt.Printf("第二行 - c 的值为 %d\n", c )
c = a * b
fmt.Printf("第三行 - c 的值为 %d\n", c )
c = a / b
fmt.Printf("第四行 - c 的值为 %d\n", c )
c = a % b
fmt.Printf("第五行 - c 的值为 %d\n", c )
a++
fmt.Printf("第六行 - a 的值为 %d\n", a )
a=21   // 为了方便测试，a 这里重新赋值为 21
a--
fmt.Printf("第七行 - a 的值为 %d\n", a )

```

#### Relational operator

| operator | describe                                                     | example           |
| :------- | :----------------------------------------------------------- | :---------------- |
| ==       | 检查两个值是否相等，如果相等返回 True 否则返回 False。       | (A == B) 为 False |
| !=       | 检查两个值是否不相等，如果不相等返回 True 否则返回 False。   | (A != B) 为 True  |
| >        | 检查左边值是否大于右边值，如果是返回 True 否则返回 False。   | (A > B) 为 False  |
| <        | 检查左边值是否小于右边值，如果是返回 True 否则返回 False。   | (A < B) 为 True   |
| >=       | 检查左边值是否大于等于右边值，如果是返回 True 否则返回 False。 | (A >= B) 为 False |
| <=       | 检查左边值是否小于等于右边值，如果是返回 True 否则返回 False。 | (A <= B) 为 True  |

```golang
var a int = 21
var b int = 10

if a == b {
    fmt.Printf("第一行 - a 等于 b\n")
} else {
    fmt.Printf("第一行 - a 不等于 b\n")
}
if a < b {
    fmt.Printf("第二行 - a 小于 b\n")
} else {
    fmt.Printf("第二行 - a 不小于 b\n")
}

if a > b {
    fmt.Printf("第三行 - a 大于 b\n")
} else {
    fmt.Printf("第三行 - a 不大于 b\n")
}
/* Lets change value of a and b */
a = 5
b = 20
if a <= b {
    fmt.Printf("第四行 - a 小于等于 b\n")
}
if b >= a {
    fmt.Printf("第五行 - b 大于等于 a\n")
}
```

#### Logical operator

| operator | describe                                                     | example            |
| :------- | :----------------------------------------------------------- | :----------------- |
| &&       | 逻辑 AND 运算符。 如果两边的操作数都是 True，则条件 True，否则为 False。 | (A && B) 为 False  |
| \|\|     | 逻辑 OR 运算符。 如果两边的操作数有一个 True，则条件 True，否则为 False。 | (A \|\| B) 为 True |
| !        | 逻辑 NOT 运算符。 如果条件为 True，则逻辑 NOT 条件 False，否则为 True。 | !(A && B) 为 True  |

```golang
var a bool = true
var b bool = false
if a && b {
    fmt.Printf("第一行 - 条件为 true\n")
}
if a || b {
    fmt.Printf("第二行 - 条件为 true\n")
}
/* 修改 a 和 b 的值 */
a = false
b = true
if a && b {
    fmt.Printf("第三行 - 条件为 true\n")
} else {
    fmt.Printf("第三行 - 条件为 false\n")
}
if !(a && b) {
    fmt.Printf("第四行 - 条件为 true\n")
}
```

#### Bitwise operator

| operator | describe                                                     | example                                |
| :------- | :----------------------------------------------------------- | :------------------------------------- |
| &        | 按位与运算符"&"是双目运算符。 其功能是参与运算的两数各对应的二进位相与。 | (A & B) 结果为 12, 二进制为 0000 1100  |
| \|       | 按位或运算符"\|"是双目运算符。 其功能是参与运算的两数各对应的二进位相或 | (A \| B) 结果为 61, 二进制为 0011 1101 |
| ^        | 按位异或运算符"^"是双目运算符。 其功能是参与运算的两数各对应的二进位相异或，当两对应的二进位相异时，结果为1。 | (A ^ B) 结果为 49, 二进制为 0011 0001  |
| <<       | 左移运算符"<<"是双目运算符。左移n位就是乘以2的n次方。 其功能把"<<"左边的运算数的各二进位全部左移若干位，由"<<"右边的数指定移动的位数，高位丢弃，低位补0。 | A << 2 结果为 240 ，二进制为 1111 0000 |
| >>       | 右移运算符">>"是双目运算符。右移n位就是除以2的n次方。 其功能是把">>"左边的运算数的各二进位全部右移若干位，">>"右边的数指定移动的位数。 | A >> 2 结果为                          |

```golang
var a uint = 60 /* 60 = 0011 1100 */
var b uint = 13 /* 13 = 0000 1101 */
var c uint = 0

c = a & b /* 12 = 0000 1100 */
fmt.Printf("第一行 - c 的值为 %d\n", c)

c = a | b /* 61 = 0011 1101 */
fmt.Printf("第二行 - c 的值为 %d\n", c)

c = a ^ b /* 49 = 0011 0001 */
fmt.Printf("第三行 - c 的值为 %d\n", c)

c = a << 2 /* 240 = 1111 0000 */
fmt.Printf("第四行 - c 的值为 %d\n", c)

c = a >> 2 /* 15 = 0000 1111 */
fmt.Printf("第五行 - c 的值为 %d\n", c)
```

### Text Types

#### String

* UTF-8
* Immutable
* can be concatenated with plus(+) operator
* can be converted to []byte
* seq, can be accessed by index

```golang
var str string
str="hello world"
```

#### Rune

rune类型实质其实就是int32，他是go语言内在处理字符串及其便捷的字符单位。它会自动按照字符独立的单位去处理方便我们在遍历过程中按照我们想要的方式去遍历。
我们在处理字符串的时候可以通过map[rune]int类型方便的判断字符串是否存在，其中 rune表示字符的UTF-8的编码值，int表示字符在字符串中的位置(按照字节的位置)

## Array&Slice

## Map&Struct

## JSON & Text & HTML

