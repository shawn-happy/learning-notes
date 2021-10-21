package main

import (
	"fmt"

	"./bar"
)

func main() {
	var c int = bar.F()
	fmt.Printf("result = %v", c)
}
