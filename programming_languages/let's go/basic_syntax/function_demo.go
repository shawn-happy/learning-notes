package main

import "fmt"

func sum(a, b float64) float64 {
	return a + b
}

func main() {
	s := sum(3, 4)
	fmt.Println(s)
}
