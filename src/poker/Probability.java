package poker;

import java.util.Scanner;

public class Probability {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("How many cards have been revealed: ");
        int numCards = sc.nextInt();
        sc.nextLine();

        int[] board = new int[numCards];
        for (int i = 0; i < numCards; i++) {
            System.out.print("What is the next card on the board: ");
            board[i] = Util.card(sc.nextLine());
        }

        int[] hand = new int[2];
        for (int i = 0; i < 2; i++) {
            System.out.print("What is your next card: ");
            hand[i] = Util.card(sc.nextLine());
        }

        System.out.print("How many other players: ");
        int numPlayers = sc.nextInt();
        sc.nextLine();

        System.out.print("How many trials do you want to run (I recommend less than 1 million): ");
        int numAttempts = sc.nextInt();
        sc.nextLine();

        int numWins = 0;
        int numTies = 0;
        for (int i = 0; i < numAttempts; i++) {
            boolean[] stats = PokerGame.randomGame(hand, board, numPlayers);
            if (stats[0]) numWins++;
            else if (stats[1]) numTies++;
            if ((i % 100000 == 0) && (i > 0)) System.out.println(i + " passed");
        }
        System.out.println("You won " + numWins + " and tied" + numTies + "out of " + numAttempts + " total games. ");
        System.out.println("Your probability of winning was: " + 1.0 * numWins / numAttempts);
        System.out.println("Your probability of tying was: " + 1.0 * numTies / numAttempts);
        sc.close();
    }
}
