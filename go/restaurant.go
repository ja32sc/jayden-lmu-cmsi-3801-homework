package main

import (
	"log"
	"math/rand"
	"time"
	"sync"
	"sync/atomic"

)

func do(seconds int, action ...any) {
    log.Println(action...)
    randomMillis := 500 * seconds + rand.Intn(500 * seconds)
    time.Sleep(time.Duration(randomMillis) * time.Millisecond)
}

type Order struct {
	id uint64
	customer string
	reply chan *Order
	preparedBy string
}
var nextID atomic.Uint64
var waiter = make(chan *Order, 3)

func Cook(name string) {
	log.Println(name, "starting work")
	for {
		order := <-waiter
		do(10, name, "cooking order", order.id, "for", order.customer)
		order.preparedBy = name
		order.reply <- order
	}
}

func Customer(name string, wg *sync.WaitGroup) {
	defer wg.Done()
	for mealsEaten := 0; mealsEaten < 5; {
		order := &Order{
			id: nextID.Add(1),
			customer: name,
			reply: make(chan *Order, 1),
		}
		log.Println(name, "placed order", order.id)

		select {
		case waiter <- order:
			meal := <-order.reply
			do(2, name, "eating cooked order", meal.id, "prepared by", meal.preparedBy)
			mealsEaten++
		case <-time.After(7 * time.Second):
			do(5, name, "waiting too long, abandoning order", order.id)
		}
	}
	log.Println(name, "going home")
}

func main() {
	customers := []string{
		"Ani", "Bai", "Cat", "Dao", "Eve", "Fay", "Gus", "Hua", "Iza", "Jai",
	}

	var wg sync.WaitGroup

	go Cook("Remy")
	go Cook("Linguini")
	go Cook("Colette")

	for _, customer := range customers {
		wg.Add(1)
		go Customer(customer, &wg)
	}

	wg.Wait()

	log.Println("Restaurant closing")
}
