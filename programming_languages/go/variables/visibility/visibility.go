package visibility

import (
	"fmt"
)

var Name = "Shawn"
var cardId = "123456789"

func init() {
	fmt.Println("Inner Original:")
	fmt.Printf("	%v, %T\n", Name, Name)
	fmt.Printf("	%v, %T\n", cardId, cardId)
}
