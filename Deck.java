import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private String[] suits = {"s", "h", "c", "d"};
    private String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    private ArrayList<Card> deck = new ArrayList<Card>();

    public Deck() {
        for (String suit : suits) {
            for (String rank : ranks) {
                Card card = new Card(rank, suit);
                deck.add(card);
            }
        }
    }

    public void reorder() {
        deck = new ArrayList<Card>();
        for (String suit : suits) {
            for (String rank : ranks) {
                Card card = new Card(rank, suit);
                deck.add(card);
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(deck);
    }

    public Card draw() {
        Card top = deck.get(0);
        deck.remove(top);
        return top;
    }

    public String toString() {
        String deckString = String.format("Your current deck: \n");
        for (Card card : deck) {
            deckString += card.toString() + "\n";
        }
        return deckString;
    }
}
