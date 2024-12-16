package Utils;
import Structures.Card;
import Structures.Deck;

public class EquityCalculator {
    public static void getEquity(Card[] hand1, Card[] hand2, int trials) {
        Deck d = new Deck();
        for (Card c : hand1) {
            d.remove(c);
        }
        for (Card c : hand2) {
            d.remove(c);
        }

        for (int i = 0; i < trials; i++) {
            d.shuffle();
            Card[] communityCards = new Card[5];
        }
    }
}
