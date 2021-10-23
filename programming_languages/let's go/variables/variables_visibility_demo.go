package main

import (
	"fmt"

	"./visibility"
)

func main() {
	fmt.Println("outer:")
	var name = visibility.Name
	fmt.Printf("	%v, %T\n", name, name)
	var cardId = visibility.cardId
	// fmt.Printf("%v, %T\n", cardId, cardId)
	visibility.F()
}
