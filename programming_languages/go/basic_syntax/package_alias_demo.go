package main

import (
	"fmt"

	foo "./bar"
)

func main() {
	var c int = foo.F()
	fmt.Printf("result = %v", c)
}
