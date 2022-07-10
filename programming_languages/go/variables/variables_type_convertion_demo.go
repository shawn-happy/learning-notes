package main

import (
	"fmt"
	"strconv"
)

func main() {
	fmt.Println("outer:")
	var i int = 2
	var j float64 = float64(i)
	fmt.Printf("%v, %T\n", i, i)
	fmt.Printf("%v, %T\n", j, j)

	var p float32 = 6.7
	var q int32 = int32(p)
	fmt.Printf("%v, %T\n", p, p)
	fmt.Printf("%v, %T\n", q, q)

	t := 42
	s := string(t) // 转成Unicode
	fmt.Printf("%v, %T\n", t, t)
	fmt.Printf("%v, %T\n", s, s)

	v := strconv.Itoa(t) // 转String
	fmt.Printf("%v, %T\n", v, v)
}
