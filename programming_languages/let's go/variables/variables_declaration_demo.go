package main

import (
	"fmt"
)

func main() {
	var i int = 8
	fmt.Printf("%v, %T\n", i, i)

	var j int
	fmt.Printf("%v, %T\n", j, j)

	var m = 5.5
	fmt.Printf("%v, %T\n", m, m)

	n := 6.7
	fmt.Printf("%v, %T\n", n, n)

	// var list
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

	p, q := 3, 4
	fmt.Printf("%v, %T\n", p, p)
	fmt.Printf("%v, %T\n", q, q)
}
