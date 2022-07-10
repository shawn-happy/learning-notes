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
