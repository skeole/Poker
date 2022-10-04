package scoring

import "sort"

func Score(deck []int) int {

	//public int suit; //0 = Diamonds, 1 = Hearts, 2 = Club, 3 = Spade
	//public int number; //0 = 2, ..., 8 = 10, 9 = Jack, 10 = Queen, 11 = King, 12 = Ace
	//2 of diamonds, 2 of hearts, 2 of club, 2 of spade, 3 of diamonds, ...

	suits := make([]int, 5)
	values := make([]int, 5)

	for i := 0; i < 5; i++ {
		suits[i] = deck[i] % 4
		values[i] = deck[i] / 4
	}

	// sort the suits array
	sort.Slice(suits, func(i, j int) bool {
		return suits[i] < suits[j]
	})

	flush := suits[0] == suits[4]

	sort.Slice(values, func(i, j int) bool {
		return values[i] < values[j]
	})

	highCard := values[4]
	cardValues := make([]int, 13)

	for val := range values {
		cardValues[val]++
		//cardValues[3] == # of threes
	}

	two_of_a_kind, three_of_a_kind, four_of_a_kind, two_pairs := false, false, false, false

	first_pair, second_pair := 0, 0

	for i := 0; i < 13; i++ {
		if cardValues[i] == 0 || cardValues[i] == 1 {
			continue
		}
		switch cardValues[i] {
		case 2:
			if two_of_a_kind {
				two_pairs = true
			}
			two_of_a_kind = true
			if first_pair != 0 {
				second_pair = i
			} else {
				first_pair = i
			}
		case 3:
			three_of_a_kind = true
			if first_pair != 0 {
				second_pair = first_pair
				first_pair = i
			}

		case 4:
			four_of_a_kind = true
			first_pair = i
		}
	}

	full_house := two_of_a_kind && three_of_a_kind
	straight := (((!two_of_a_kind) && (!three_of_a_kind)) && ((!four_of_a_kind) && (values[4]-values[0] == 4)))

	if (cardValues[0]*cardValues[1]*cardValues[2]*cardValues[3]*cardValues[12] == 1)  {
		straight = true
	}
	/* Ace/2/3/4/5 exception */
	/*
         * Nothing; 0
            * Tiebreaker: High Card
            * 13 choose 5 = 1287

            * 0...12; 5 cards
            it's increasing

            we can make it 0...8 by subtracting (0, 1, 2, 3, 4) from the position
                this makes it nondecreasing
            now we want to order it

            order: 
            0 1 2 3 4
            0 1 2 3 5
            0 1 2 4 5
            0 1 3 4 5
            0 2 3 4 5
            1 2 3 4 5
            0 1 2 3 6

            how to convert this into strictly increasing
            reverse the order
            4 3 2 1 0; 4 choose 4
            5 3 2 1 0
            5 4 2 1 0
            5 4 3 1 0
            5 4 3 2 0
            5 4 3 2 1; 5 choose 4
            6 3 2 1 0; we get 6 + 0 + 0 + 0 + 0 yep
            note they're always strictly decreasing

            ok so basically
            # of ways for a number to start with digit k1: k choose 4
            up to k1: 
            4 choose 4 + 5 choose 4 + ... + (k-1) choose 4 = k choose 5
            # of ways for it to start with k1 and then k2: k2 choose 3
            etc. etc.
            so basically our order is
            k1 choose 5 + k2 choose 4 + k3 choose 3 + k4 choose 2 + k5 choose 1
            perhaps we could do differences

            12 11 10 9 8
            SHOULD equal 1286 because 1287 total possibilities

            12 choose 5 + 11 choose 4 + 10 choose 3 + 9 choose 2 + 8 choose 1

            TL; DR; let's say the cards read (in decreasing order) k1, k2, k3, k4, k5
            Then, the tiebreaker score is (k1 choose 5) + (k2 choose 4) + ... + (k5 choose 1)
            Where something "undefined" like 3 choose 4 = 0

         * Pair; 1287
            * Tiebreaker: Pair card then High Card
            * 13 * 12 choose 3 = 2860
            k1; k2, k3, k4
            220 * (k1 choose 1) + (k2 choose 3 + k3 choose 2 + k4 choose 1)
            NOTE: if k2, k3 or k4 > k1, then subtract 1 from them
            i.e. 5; 8, 7, 2 becomes 5; 7, 6, 2

         * Two Pairs; 4147
            * Tiebreaker: Pair cards
            * Ace and 7 beats King and Queen
            * Ace and King beats Ace and Queen
            * 13 choose 2 * 11 = 858
            if > k2 subtract 2 and if between k1 and k2 subtract 1

         * 3 of a Kind; 5005
            * 13 * 12 choose 2 = 858

         * Straight (numbers are ordered, not a flush); 5863
            * 13 (technically only 10 but whatever)

         * Flush (All 5 = same suit); 5876
            * Highest card, then 2nd highest, then etc., same as nothing
            * So K/Q/J/4/2 beats K/10/8/7/3
            * 13 choose 5 = 1287

         * Full House: Pair + 3 of a kind; 7163
            * 3 pair then 2 pair
            * 13 * 12 = 156

         * 4 of a kind; 7319
            * Value
            * 13 * 12 = 156

         * Straight Flush; 7475
            * High Card, except A/2/3/4/5 has 5, not A, as the high card
            * 13 (Again, technically 10)
        
         * TOTAL: 7488
         */

		 score := highCard
		 return score
}
