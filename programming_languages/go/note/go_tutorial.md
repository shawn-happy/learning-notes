## introduction
Go是Google开发的一种静态强类型，编译型，并发型，并具有GC功能的编程语言。 它于2009年由Robert Griesemer、Rob Pike和Ken Thompson作为开源项目发布。

GO是一种类似C语言的编程语言，不仅能够访问底层操作系统，还提供了强大的网络编程，并发编程支持。

GO与C语言有相似的语法，但是对变量的声明不同，GO还支持GC。并发模型采用的是CSP，通过管道传输。

与c++相比，GO并不包括枚举，异常处理，继承，泛型，断言，虚函数等功能。

GO的用途很多，可以进行网络编程、系统编程、并发编程、分布式编程。此外，很多重要的开源项目都是使用Go语言开发的，其中包括 Docker，Kubernetes。

## Hello World
```golang
package main

import "fmt"

func main() {
	fmt.Print("Let's GO!")
}
```

go 是编译型语言，可以通过go的命令将其源文件编译成可执行的二进制文件。最简单的命令`run`
```shell
go run helloworld.go
```
运行结果：
```shell
Let's GO!
```
可以使用`build`命令生成可执行的二进制文件
```shell
go build helloworld.go
```
随即直接执行
```shell
./helloworld.exe
```
注意：
* 如果是`windows os`下`build`出来的，二进制文件以`.exe`结尾
* 如果是`mac ox`或者`unix`下`build`出来的则没有结尾，直接就是文件名为`helloworld`的可执行的二进制文件。


## Go basic Syntax

go的基础语法由以下部分组成：
* package
* import
* function
* variable
* code(expression)
* comments

### package&import
`go`是使用包来组织代码的，和其他语言中的库和模块类似。一个包里可以包含一个或者多个`.go`结尾的源文件，放在一个文件夹中，该文件夹的名字描述了包的作用。

每个源文件的内容都是以`package`关键词声明为开头，表明这个文件是属于哪个包下的。如果其他的源文件里想要调用该文件的某个函数或者变量，需要使用`import`关键词来引用包。

`go`的标准库中包含100多个包，可以用来日常开发使用，比如`fmt`中的函数是用来格式化输出。

其中`package main`比较特殊，它用来定义一个独立的可执行程序，而不是库。在`main`包中`main`函数也是特殊的，总是程序开始执行的地方。

`package`初始化是从初始化包级别的变量开始，按照顺序自上而下依次初始化，在依赖已解析的情况下，按照依赖顺序执行

包如果由多个`.go`文件组成，初始化按照编译器收到的文件顺序执行： `go`会在调用编译器之前对源文件进行排序。

也可以使用`init`函数进行初始化。

包的初始化按照在程序中导入的顺序进行，如果a包导入了b包，需要先保证b包初始化完毕，再来初始化a包。

main包是在最后初始化的

例如：

新建一个名为bar的包(文件夹)

```golang
package bar

import "fmt"

var a int = 1
var b int = 2

func init() {
	fmt.Println("Let's Go into Bar Package!")
}

func F() int {
	return a + b
}

```
调用bar包里的F方法
```golang
package main

import (
	"fmt"

	"./bar"
)

func main() {
	var c int = bar.F()
	fmt.Printf("result = %v", c)
}

```

执行结果：
```
go run .\package_demo.go
Let's Go into Bar Package!
result = 3
```

`import`关键词必须是紧跟在`package`,如果有多个包需要被引入，可以使用`()`

例如：
```golang
import (
	"fmt"
	"os"
)
```
需要注意的是：
* 如果注意如果要包方法在其他包中可以调用，包方法需要首字母大写`fmt.Println() fmt.Printf()`
* 包需要精确导入，如果缺失导入或者存在不需要的包的情况下，会编译失败。

例如：

引入了没有被使用到的包
```golang
package main

import (
	"fmt"
	"os"
)

func main() {
	fmt.Print("Let's GO!")
}

```
运行结果：
```
go run import_package_error.go
import_package_error.go:5:2: imported and not used: "os"
```

缺失导入的情况：

```golang
package main

func main() {
	fmt.Print("Let's GO!")
}

```
运行结果：

```
go run import_package_not_exist.go
import_package_not_exist.go:4:2: undefined: fmt
```

import其余操作
* 别名： 如果两个包的包名存在冲突，或者包名太长需要简写时，我们可以使用别名导入来解决。
* 点： `.`导入可以让包内的方法注册到当前包的上下文中，直接调用方法名即可，不需要再加包前缀。
* 下划线: `_`是包引用操作，只会执行包下各模块中的 init 方法，并不会真正的导入包，所以不可以调用包内的其他方法。

示例代码：
1. 别名
```golang
package main

import (
	"fmt"

	foo "./bar" // 别名为foo
)

func main() {
	var c int = foo.F() // 使用别名调用
	fmt.Printf("result = %v", c)
}

```
运行结果：
```
go run .\package_alias_demo.go
Let's Go into Bar Package!
result = 3
```
2. 点
```golang
package main

import (
	"fmt"

	. "./bar"
)

func main() {
	var c int = F()
	fmt.Printf("result = %v", c)
}

```
运行结果：
```
go run .\package_point_demo.go
Let's Go into Bar Package!
result = 3
```
3. 下划线
```golang
package main

import (
	"fmt"

	_ "./bar"
)

func main() {
	// var c int = bar.F() 错误 _ 并没有导入包 只是引入并执行包模块的init方法
	fmt.Printf("result = %v", 1)
}

```
运行结果：
```
go run .\package_underline_demo.go
Let's Go into Bar Package!
result = 1
```

### variables

`var`声明创建一个具体类型的变量，然后给这个变量命名，并设置它的初始值。
```golang
var name type = expression
```
`type`和`expression`可以省略一个，但是不能都省略：
* 如果类型省略，它的类型将由初始表达式决定。
* 如果表达式省略，则为默认值
默认值列表：

| 类型                                           | 默认值 |
| ---------------------------------------------- | ------ |
| 数字                                           | 0      |
| bool                                           | false  |
| String                                         | ""     |
| 接口和引用类型（slice, 指针, map, 通道, 函数） | nil    |
| 数组，结构体，复合类型                         | 复合0  |

### function & code block(expression)

函数的声明表达式：

```golang
func name(parameter-list)(return-list){
    code block
}
```

函数的声明都是以`func`开头，随后跟着函数名，第一个小括号里的是形参列表，第二个小括号里的是可选的返回列表，使用大括号包裹着代码块（函数体）

形参列表指定了一组变量的参数名和参数类型，由调用者提供实参传递。

返回列表则指定了函数返回值的类型，当函数返回一个未命名的返回值或者没有返回值的时候，可以省略括号。

例如：

```golang
func sum(a, b float64) float64{
    return a + b
}
```

### comments

 `/*...*/` 是注释，在程序执行时将被忽略。单行注释是最常见的注释形式，你可以在任何地方使用以`//`开头的单行注释。多行注释也叫块注释，均已以`/*`开头，并以 `*/`结尾，且不可以嵌套使用，多行注释一般用于包的文档描述或注释成块的代码片段。
```golang
// 单行注释
/*
多行注释
块注释
*/
```

## References

[go程序设计语言](http://www.gopl.io/ch1.pdf)

[go wiki](https://en.wikipedia.org/wiki/Communicating_sequential_processes)