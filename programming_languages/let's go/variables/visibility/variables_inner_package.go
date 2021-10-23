package visibility

import (
	"fmt"
)

func F() {
	fmt.Println("Inner Package:")
	fmt.Printf("	%v, %T\n", Name, Name)
	fmt.Printf("	%v, %T\n", cardId, cardId)
}
