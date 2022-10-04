package poker;

import java.util.Arrays;

public class Util {

    static int size = 20;
    static int[][] combinations = new int[size][size];
    public static void init() {
        for (int i = 0; i < size; i++) {
            combinations[i][0] = 1;
        }
        for (int i = 1; i < size; i++) {
            for (int j = 1; j < size; j++) {
                combinations[i][j] = combinations[i - 1][j] + combinations[i - 1][j - 1];
            }
        }
    }

    /* 
    Suit: 0 = Diamonds, 1 = Hearts, 2 = Club, 3 = Spade
    Number: 0 = 2, ..., 8 = 10, 9 = Jack, 10 = Queen, 11 = King, 12 = Ace
    Order: 2 of diamonds, 2 of hearts, 2 of club, 2 of spade, 3 of diamonds, ...
    */

    public static int score(int[] deck) {
        //deck: the 5

        int[] suits = new int[5];
        int[] values = new int[5];

        for (int i = 0; i < 5; i++) {
            suits[i] = deck[i] % 4;
            values[i] = deck[i] / 4;
        }

        Arrays.sort(suits);
        boolean flush = (suits[0] == suits[4]);

        Arrays.sort(values); //in ascending order now
        int[] cardValues = new int[13];

        for (int val : values) {
            cardValues[val]++; //ex. cardValues[3] == # of threes
        }

        boolean two_of_a_kind = false;
        boolean three_of_a_kind = false;
        boolean four_of_a_kind = false;

        boolean two_pairs = false;

        int first_pair = -1;
        int second_pair = -1;

        for (int i = 0; i < 13; i++) {
            if ((cardValues[i] == 0) || (cardValues[i] == 1)) continue; //not sure if this is necessary
            if (cardValues[i] == 2) {
                if (two_of_a_kind) two_pairs = true;
                two_of_a_kind = true;
                if (first_pair != -1) second_pair = i;
                else first_pair = i;
            } else if (cardValues[i] == 3) {
                three_of_a_kind = true;
                if (first_pair != -1) {
                    second_pair = first_pair;
                    first_pair = i;
                }
            } else if (cardValues[i] == 4) {
                four_of_a_kind = true;
                first_pair = i;
            }
        }
        
        boolean full_house = (two_of_a_kind) && (three_of_a_kind);

        boolean straight = (((!two_of_a_kind) && (!three_of_a_kind)) && ((!four_of_a_kind) && (values[4] - values[0] == 4)));

        if (cardValues[0] * cardValues[1] * cardValues[2] * cardValues[3] * cardValues[12] == 1) {
            straight = true; 
            values[4] = 3; //technically now it becomes 0 1 2 3 3 but idgaf
        } /* Ace, 2, 3, 4, 5 exception */

        for (int i = 0; i < 5; i++) {
            if ((values[i] == first_pair) || (values[i] == second_pair)) {
                values[i] = -1;
            } else {
                if (values[i] > first_pair) {
                    values[i]--;
                }
                if (values[i] > second_pair) {
                    values[i]--;
                }
            }
        } 
        Arrays.sort(values); //now, smth like 1 4 4 7 10 becomes -1 -1 1 6 9
                            //2 2 8 8 9 becomes -1 -1 -1 -1 7
                            
        int index = 0;
        int tiebreaker = 0;
        if (straight) {
            tiebreaker = values[4];
        } else {
            for (int i : values) {
                if (i > -1) {
                    index++;
                    tiebreaker += combinations[i][index];
                }
            }
        }
        
        //tiebreaker = final tiebreaker

        if (flush) {
            if (straight) {
                return 7475 + tiebreaker;
            } else {
                return 5876 + tiebreaker;
            }
        } else {
            if (four_of_a_kind) {
                return 7319 + tiebreaker + 12 * first_pair;
            } else if (full_house) {
                if (second_pair > first_pair) second_pair--;
                return 7163 + 12 * first_pair + second_pair;
            } else if (straight) {
                return 5863 + tiebreaker;
            } else if (three_of_a_kind) {
                return 5005 + tiebreaker + 66 * first_pair;
            } else if (two_pairs) {
                if (first_pair < second_pair) {
                    int temp = second_pair;
                    second_pair = first_pair;
                    first_pair = temp;
                } //make it so first pair > second pair
                return 4147 + tiebreaker + 11 * (combinations[first_pair][2] + combinations[second_pair][1]);
            } else if (two_of_a_kind) {
                return 1287 + tiebreaker + 220 * first_pair;
            } else {
                return tiebreaker;
            }
        }

        /*
         * Nothing; 0
            Let's say the cards read (in decreasing order) k1, k2, k3, k4, k5
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
            * Maximum choose 2 + Minimum choose 1
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

    }
}
