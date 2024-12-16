package Scripts;

import java.util.HashMap;
import Structures.*;
import Utils.*;

public class testScript {
    public static void test() {
        // Deck d = new Deck();
        // System.out.println(d);
        // d.shuffle();
        // System.out.println(d);

        // ArrayList<String> kickers = new ArrayList<String>();
        // kickers.add("8");
        // kickers.add("A");
        // kickers.add("Q");
        // kickers.add("3");
        // String[] testKickers = HandComparer.getKickers(kickers);
        // for (String k : testKickers) {
        //     System.out.println(k);
        // }


        Deck d = new Deck();
        HashMap<String, Integer> frequencyMap = new HashMap<>();

        // Puts the ranks of the cards in a frequency hashmap:
            
        long startTime = System.currentTimeMillis();

        int totalHands = 5;
        for (int i = 0; i < totalHands; i++) {
            d.shuffle();
            Card[] hand = new Card[7];
            for (int j = 0; j < hand.length; j++) {
                hand[j] = d.draw();
                System.out.print(hand[j] + " ");
            }
            System.out.println();
            Card[] best = HandComparer.getBestHand(hand);
            System.out.print("Best hand: ");
            for (Card c : best) {
                System.out.print(c + " ");
            }
            System.out.println();
            System.out.println(HandComparer.getHandType(best));
        }
        System.out.println();

        long endTime = System.currentTimeMillis();
        System.out.println((endTime - startTime)/1000.0);
    }
}
