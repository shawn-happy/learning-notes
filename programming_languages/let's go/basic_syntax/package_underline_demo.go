package main

import (
	"fmt"

	_ "./bar"
)

func main() {
	fmt.Printf("result = %v", 1)
}
