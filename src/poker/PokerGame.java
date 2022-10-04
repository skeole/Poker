package poker;

import java.util.Arrays;

public class PokerGame {
    public static boolean randomGame(int[] playerHand, int[] revealedCards, int numPlayers) {
        Deck deck = new Deck();
        int[] gameCards = new int[5];
        int[][] hands = new int[numPlayers + 1][2];
        hands[0] = playerHand;
        for (int i : playerHand) {
            deck.removeCard(i);
        }
        for (int i = 0; i < revealedCards.length; i++) {
            deck.removeCard(revealedCards[i]);
            gameCards[i] = revealedCards[i];
        }
        for (int i = revealedCards.length; i < 5; i++) {
            gameCards[i] = deck.randomCard();
        }
        for (int i = 0; i < numPlayers; i++) {
            for (int j = 0; j < 2; j++) {
                hands[i+1][j] = deck.randomCard();
            }
        }
        Util.init();
        int[] scores = Util.scores(gameCards, hands);
        int score = scores[0];
        Arrays.sort(scores);
        return (scores[numPlayers] == score);
    }
}
