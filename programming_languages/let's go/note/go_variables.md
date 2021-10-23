## variables

### Variables Declaration
`var`声明创建一个具体类型的变量，然后给这个变量命名，并设置它的初始值。基础语法是：

```golang
var name type = expression
```

`type`和`expression`可以省略一个，但是不能都省略：

* 如果类型省略，它的类型将由初始表达式决定。
* 如果表达式省略，则为默认值

示例代码：

```golang
var i int = 8
fmt.Printf("%v, %T\n", i, i) // 8, int

var j int
fmt.Printf("%v, %T\n", j, j) // 初始值省略的情况，默认值是0: 0, int

var m = 5.5
fmt.Printf("%v, %T\n", m, m) // 类型省略的情况，go会自动判断类型，此案例类型为float64,5.5, float64
```

在go中还有一种特殊的声明方式，就是使用`:=`进行短变量声明，值得注意的是，需要在`=`前面加上`:`，如果只有`=`则只是一个赋值运算符。这种方式，就是类型省略的简写。

```golang
n := 6.7
fmt.Printf("%v, %T\n", n, n) // 6.7, float64
```

在go中还可以进行同时声明多个变量，并选择使用对应的表达式列表对其初始化。

```golang
var a, b, c = 1, 2.5, "GO"
var d, e, f int
var g, h = false, "GoLang"
fmt.Printf("%v, %T\n", a, a)
fmt.Printf("%v, %T\n", b, b)
fmt.Printf("%v, %T\n", c, c)
fmt.Printf("%v, %T\n", d, d)
fmt.Printf("%v, %T\n", e, e)
fmt.Printf("%v, %T\n", f, f)
fmt.Printf("%v, %T\n", g, g)
fmt.Printf("%v, %T\n", h, h)

// short form declaration
p, q := 3, 4
fmt.Printf("%v, %T\n", p, p)
fmt.Printf("%v, %T\n", q, q)

i, j := 3, 4
i, v := 5, 6
fmt.Printf("%v, %T\n", i, i)
fmt.Printf("%v, %T\n", j, j)
fmt.Printf("%v, %T\n", v, v)
/*
output: 
1, int
2.5, float64
GO, string
0, int
0, int
0, int
false, bool
GoLang, string
3, int
4, int
5, int
4, int
6, int
*/
```

### Variables Declaration Error Case

1. 声明了变量，如果这个变量没有被使用，则会报错。
```golang
n := 42
fmt.Println("Hello, playground")
// ./prog.go:8:2: n declared but not used
```
2. 变量不能被重复声明。如果是短变量声明则最少需要声明一个新的变量

```golang
var i int = 8
fmt.Printf("%v, %T\n", i, i)
var i int = 7 //  i redeclared in this block

// short form declaration： no new variables on left side of :=
i := 13 
a, b := 3, 4
a, b := 5, 6
```

3. 变量列表如果需要指定类型，则只能在最后指定，这样变量列表的变量元素类型都是相同的，不能每个变量名都跟着类型

```golang
var a int, b int // syntax error: unexpected comma at end of statement
var a int, b float64 // error: syntax error: unexpected comma at end of statement
```

4. 变量列表如果有一个赋值了，其余的也需要赋值，否则报错

```golang
var a, b = 1 // assignment mismatch: 2 variables but 1 value
```

5. 变量重新赋值，类型不对

```golang
var i int = 8
i = 6.5 // constant 6.5 truncated to integer
```

### Variables Scope

函数内定义的变量称为局部变量

函数外定义的变量称为全局变量

函数定义中的变量称为形式参数

1. 在函数体内声明的变量称之为局部变量，它们的作用域只在函数体内，参数和返回值变量也是局部变量。

```golang
func f(){
	/* 声明局部变量 */
	var a, b, c int // 只能在f()函数体内被使用
}
```

2. 在函数体外声明的变量称之为全局变量，全局变量可以在整个包甚至外部包（被导出后）使用。

* 如果变量名是大写字母开头，则可以被其他包引入该变量。
* 如果是小写字母开头，则只能在该报下被引用。

示例代码：

包内源文件

```golang
package visibility

import (
	"fmt"
)

var Name = "Shawn"
var cardId = "123456789"

func init() {
	fmt.Println("Inner Original:")
	fmt.Printf("	%v, %T\n", Name, Name)
	fmt.Printf("	%v, %T\n", cardId, cardId)
}
```

包内其他文件

```golang
package visibility

import (
	"fmt"
)

func F() {
	fmt.Println("Inner Package:")
	fmt.Printf("	%v, %T\n", Name, Name)
	fmt.Printf("	%v, %T\n", cardId, cardId)
}

```

其他包：

```golang
package main

import (
	"fmt"

	"./visibility"
)

func main() {
	fmt.Println("outer:")
	var name = visibility.Name
	fmt.Printf("	%v, %T\n", name, name)
	// var cardId = visibility.cardId // cannot refer to unexported name visibility.cardId
	// fmt.Printf("%v, %T\n", cardId, cardId) 
	visibility.F()
}

```

3. 没有私有域

### Type Convertion
类型转换用于将一种数据类型的变量转换为另外一种类型的变量。Go 语言类型转换基本格式：
`type_name(expression)`

代码示例：

```golang
var i int = 2
var j float64 = float64(i)
fmt.Printf("%v, %T\n", i, i)
fmt.Printf("%v, %T\n", j, j)

var p float32 = 6.7
var q int32 = int32(p)
fmt.Printf("%v, %T\n", p, p)
fmt.Printf("%v, %T\n", q, q) // 6, int32  精度丢失，直接去掉小数位

t := 42
s := string(t)
fmt.Printf("%v, %T\n", t, t)
fmt.Printf("%v, %T\n", s, s) // *, string unicode

v := strconv.Itoa(t)
fmt.Printf("%v, %T\n", v, v)

/*
output: 
	2, int
	2, float64
	6.5, float32
	6, int32
	42, int
	*, string
	42, string
 */
```