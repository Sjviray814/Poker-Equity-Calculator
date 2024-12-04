import java.util.ArrayList;
import java.util.HashMap;

public class Driver {
    public static void main(String[] args) {
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

        int totalHands = 1000000;
        for (int i = 0; i < totalHands; i++) {
            d.shuffle();
            Card[] hand = new Card[5];
            for (int j = 0; j < hand.length; j++) {
                hand[j] = d.draw();
                // System.out.print(hand[j] + " ");
            }
            // System.out.println(HandComparer.getHandType(hand));
            String handrep = HandComparer.getHandType(hand);
            frequencyMap.put(handrep.split(" ")[0], frequencyMap.getOrDefault(handrep.split(" ")[0], 0) + 1);
            d.reorder();
        }
        frequencyMap.forEach((key, value) -> System.out.println(key + ": " + 100.0*value/totalHands));

        long endTime = System.currentTimeMillis();
        System.out.println((endTime - startTime)/1000.0);

        // Card[] hand = {new Card("5", "c"),new Card("2", "h"), new Card("4", "h"), new Card("3", "h"), new Card("A", "h")};
        // System.out.println(HandComparer.getHandType(hand));
    }
}
