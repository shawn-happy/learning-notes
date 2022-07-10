package main

import (
	"fmt"

	. "./bar"
)

func main() {
	var c int = F()
	fmt.Printf("result = %v", c)
}
