package main

import (
	"fmt"
)

func main() {
	var i int = 8
	fmt.Printf("%v, %T\n", i, i)

	// var a declared but not used
	// a := 42

	// i redeclared in this block
	// var i int = 7

	// no new variables on left side of :=
	// i := 13
	// a, b := 3, 4
	// a, b := 5, 6

	// syntax error: unexpected comma at end of statement
	// var a int, b int

	// assignment mismatch: 2 variables but 1 value
	// var a, b = 1
	// a, b := 3

	// constant 6.5 truncated to integer
	// i = 6.5
	fmt.Println("Let's GO")
}
