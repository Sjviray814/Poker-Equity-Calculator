import java.util.ArrayList;
import java.util.HashMap;

public class HandComparer {

    private static String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    /**
     * @param hand1
     * @param hand2
     * @return 1 if hand 1 wins, 2 if hand 2 wins
     */
    public static int getWinner(Card[] hand1, Card[] hand2) throws IllegalArgumentException {
        if (hand1.length < 5 || hand2.length < 5) {
            throw new IllegalArgumentException("Hand size cannot be less than 5.");
        }
        return 1;
    }

    /**
     * @param hand hand of 5 cards to determine type
     * @return the string representation of the hand type
     * @throws IllegalArgumentException if hand is not size 5
     * 
     * Straight Flush: SF r
     * Quads: Q r k
     * Boat: FH r1 r2
     * Flush: F r1 r2 r3 r4 r5
     * Straight: S r (highest)
     * Trips: TR r k1 k2
     * Two Pair: TP r1 r2 k
     * Pair: P r k1 k2 k3
     * High Card: H /k1 /k2 /k3 /k4 /k5
     */
    public static String getHandType(Card[] hand) throws IllegalArgumentException {

         HashMap<String, Integer> frequencyMap = new HashMap<>();

        // Puts the ranks of the cards in a frequency hashmap:
        for (Card card : hand) {
            frequencyMap.put(card.getRank(), frequencyMap.getOrDefault(card.getRank(), 0) + 1);
        }

        // Checks the number of high cards, pairs, trips and quads:
        ArrayList<String> ones = new ArrayList<String>();
        ArrayList<String> pairs = new ArrayList<String>();
        ArrayList<String> trips = new ArrayList<String>();
        ArrayList<String> quads = new ArrayList<String>();

        for (String key : frequencyMap.keySet()) {
            if (frequencyMap.get(key) == 1) { 
                ones.add(key);
            } else if (frequencyMap.get(key) == 2) {
                pairs.add(key);
            } else if (frequencyMap.get(key) == 3) {
                trips.add(key);
            } else if (frequencyMap.get(key) == 4) {
                quads.add(key);
            }
        }

        if (quads.size() >= 1) { // Code for quads
            String quad = quads.get(0);
            String highestKicker = getKickers(ones)[0];
            return "Q " + quad + " " + highestKicker;
        } else if (trips.size() >= 1) { 

            String three = getKickers(trips)[0];

            // if there is just 1 three of a kind but its a boat:
            if (trips.size() == 1 && pairs.size() >= 1) { // Code for trips or boat
                String pair = getKickers(pairs)[0];
                return "FH " + three + " " + pair;
            } else if (trips.size() >= 1) { // If there are multiple trips
                String pair;
                if (pairs.size() == 0) { // multiple trips but no pairs
                    pair = getKickers(trips)[1];
                } else { // multiple trips and multiple pairs
                    pair = rankToInt(getKickers(trips)[1]) > rankToInt(getKickers(pairs)[0]) ? // The pair is whichever is greater: the second greatest trips or the greatest pair
                        getKickers(trips)[1] :
                        getKickers(pairs)[0];
                }
                return "FH " + three + " " + pair;
            } else { // Just trips
                String kicker1 = getKickers(ones)[0];
                String kicker2 = getKickers(ones)[1];
                return "TR " + three + " " + kicker1 + " " + kicker2;
            }
        }

        return null;
    }

    /**
     * @param rank string representation of rank
     * @return integer representation for comparison purposes
     */
    public static int rankToInt(String rank) throws IllegalArgumentException {
        for (int i = 0; i < ranks.length; i++) {
            if (ranks[i].equals(rank)) {
                return i;
            }
        }
        throw new IllegalArgumentException("The rank put into rankToInt cannot be found");
    }

    /**
     * 
     * @param kickers an arraylist of string ranks
     * @return the top *num* ranks in the kickers arraylist
     * @throws IllegalArgumentException
     */
    public static String[] getKickers(ArrayList<String> kickers) throws IllegalArgumentException {
        String[] highestKickers = new String[kickers.size()];

        kickers.sort((s1, s2) -> Integer.compare(rankToInt(s2), rankToInt(s1)));


        for (int i = 0; i < kickers.size(); i++) {
            highestKickers[i] = kickers.get(i);
        }

        return highestKickers;
    }

}
