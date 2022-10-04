package poker;

import java.util.ArrayList;
import java.util.Random;

public class Deck {
    public ArrayList<Integer> cardsRemaining = new ArrayList<Integer>();

    public Deck() {
        reset();
    }

    public void reset() {
        cardsRemaining.clear();
        for (int i = 0; i < 52; i++) cardsRemaining.add(i);
    }

    public void removeCard(int cardNumber) {
        if (!cardsRemaining.contains(cardNumber)) throw new IllegalArgumentException("That card is not currently in the deck");
        cardsRemaining.remove(cardsRemaining.indexOf(cardNumber));
    }

    public int randomCard() {
        Random r = new Random();
        int c = (int) (r.nextDouble() * cardsRemaining.size());
        int temp = cardsRemaining.get(c);
        cardsRemaining.remove(c);
        return temp;
    }

    public String toString() {
        String s = "";
        for (int i = 0; i < cardsRemaining.size(); i++) {
            s += Util.parseCard(cardsRemaining.get(i)) + "\n";
        }
        return s;
    }
}
