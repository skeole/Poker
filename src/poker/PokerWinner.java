package poker;

import java.util.Arrays;
import java.util.Scanner;

public class PokerWinner { //should I have it extend util I'm not sure
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Util.init();
        boolean run = true;
        while (run) {
            int[] board = new int[5];
            for (int i = 0; i < 5; i++) {
                System.out.print("What is the next card on the board: ");
                board[i] = Util.card(sc.nextLine());
            }
            System.out.print("How many players: ");
            int numPlayers = sc.nextInt();
            
            int[][] playerHands = new int[numPlayers][2];
            sc.nextLine(); //THIS IS SO FUCKING STUPID
            for (int i = 0; i < numPlayers; i++) {
                System.out.println("Next Player (Number " + (i + 1) + ")");
                for (int j = 0; j < 2; j++) {
                    System.out.print("What is the next card in their deck: ");
                    playerHands[i][j] = Util.card(sc.nextLine());
                }
            }
            int winner = Util.bestDeck(board, playerHands) + 1;
            System.out.println("Player " + winner + " won the game. His deck was " + Arrays.toString(playerHands[winner]) + ". ");
            run = false;
        }
        sc.close();
    }
}
