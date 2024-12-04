import java.util.ArrayList;
import java.util.HashMap;

public class HandComparer {

    private static String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    private static String[] reverseRanks = {"A", "K", "Q", "J", "10", "9", "8", "7", "6", "5", "4", "3", "2"};
    private static String[] reverseRanks2 = {"A", "5", "4", "3", "2"};
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

        if (hand.length < 5) {
            throw new IllegalArgumentException("Hand size must be at least 5 for getHandType");
        }

         HashMap<String, Integer> frequencyMap = new HashMap<>();

        // Checks the number of high cards, pairs, trips and quads:
        ArrayList<String> ones = new ArrayList<String>();
        ArrayList<String> pairs = new ArrayList<String>();
        ArrayList<String> trips = new ArrayList<String>();
        ArrayList<String> quads = new ArrayList<String>();

        for (Card card : hand) {
            String rank = card.getRank();
            int count = frequencyMap.getOrDefault(rank, 0) + 1;
            frequencyMap.put(rank, count);
        
            // Update classification lists
            if (count == 1) {
                ones.add(rank);
            } else if (count == 2) {
                ones.remove(rank);
                pairs.add(rank);
            } else if (count == 3) {
                pairs.remove(rank);
                trips.add(rank);
            } else if (count == 4) {
                trips.remove(rank);
                quads.add(rank);
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
            } else if (trips.size() > 1) { // If there are multiple trips
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
                String[] onesKickers = getKickers(ones);
                String kicker1 = onesKickers[0];
                String kicker2 = onesKickers[1];
                return "TR " + three + " " + kicker1 + " " + kicker2;
            }
        } else if (pairs.size() >= 2) { // Two pair
            String pair1 = getKickers(pairs)[0];
            String pair2 = getKickers(pairs)[1];
            String kicker = getKickers(ones)[0];
            return "TP " + pair1 + " " + pair2 + " " + kicker;
        } else if (pairs.size() == 1) {
            String pair1 = getKickers(pairs)[0];
            String[] kickerS = getKickers(ones);
            String kicker1 = kickerS[0];
            String kicker2 = kickerS[1];
            String kicker3 = kickerS[2];
            return "P " + pair1 + " " + kicker1 + " " + kicker2 + " " + kicker3;
        } else if (pairs.size() == 0) { // No pair
            HashMap<String, Integer> flushMap = new HashMap<>();

            // Puts the ranks of the cards in a frequency hashmap:
            for (Card card : hand) {
                flushMap.put(card.getSuit(), flushMap.getOrDefault(card.getSuit(), 0) + 1);
            }

            for (String key : flushMap.keySet()) { // NOTE: THIS WILL ONLY WORK IF LESS THAN 10 CARDS ARE PASSED IN
                if (flushMap.get(key) >= 5) {
                    ArrayList<String> flushCards = new ArrayList<>();
                    for (Card c : hand) {
                        if (c.getSuit().equals(key)) {
                            flushCards.add(c.getRank());
                        }
                    }
                    String[] sortedFlushCards = getKickers(flushCards);
                    if (findFifthElementInLargestSequence(reverseRanks, sortedFlushCards) != null) { // if hand contains a straight
                        return "SF " +  findFifthElementInLargestSequence(reverseRanks, sortedFlushCards);
                    } else if (findFifthElementInLargestSequence(reverseRanks2, sortedFlushCards) != null) {
                        return "SF " +  findFifthElementInLargestSequence(reverseRanks2, sortedFlushCards);
                    }
                    String kicker1 = getKickers(flushCards)[0];
                    String kicker2 = getKickers(flushCards)[1];
                    String kicker3 = getKickers(flushCards)[2];
                    String kicker4 = getKickers(flushCards)[3];
                    String kicker5 = getKickers(flushCards)[4];
                    return "F " + kicker1 + " " + kicker2 + " " + kicker3 + " " + kicker4 + " " + kicker5;
                }
            }

            String[] sortedOnes = getKickers(ones);
            if (findFifthElementInLargestSequence(reverseRanks, sortedOnes) != null) { // if hand contains a straight
               return "S " +  findFifthElementInLargestSequence(reverseRanks, sortedOnes);
            } else if (findFifthElementInLargestSequence(reverseRanks2, sortedOnes) != null) {
                return "S " +  findFifthElementInLargestSequence(reverseRanks2, sortedOnes);
            }

            String kicker1 = sortedOnes[0];
            String kicker2 = sortedOnes[1];
            String kicker3 = sortedOnes[2];
            String kicker4 = sortedOnes[3];
            String kicker5 = sortedOnes[4];
            return "H " + kicker1 + " " + kicker2 + " " + kicker3 + " " + kicker4 + " " + kicker5;

        }

        return null;
    }

    /**
     * @param rank string representation of rank
     * @return integer representation for comparison purposes
     */
    private static final HashMap<String, Integer> rankMap = new HashMap<>();
    static {
        for (int i = 0; i < ranks.length; i++) {
            rankMap.put(ranks[i], i);
        }
    }

    public static int rankToInt(String rank) throws IllegalArgumentException {
        Integer value = rankMap.get(rank);
        if (value == null) {
            throw new IllegalArgumentException("Invalid rank: " + rank);
        }
        return value;
    }

    /**
     * 
     * @param kickers an arraylist of string ranks
     * @return the top *num* ranks in the kickers arraylist
     * @throws IllegalArgumentException
     */
    public static String[] getKickers(ArrayList<String> kickers) {
        kickers.sort((s1, s2) -> Integer.compare(rankToInt(s2), rankToInt(s1)));
        return kickers.toArray(new String[0]);
    }


    /** Taken from chatgpt: */
    public static String findFifthElementInLargestSequence(String[] largerArray, String[] smallerArray) {
        if (largerArray.length < 5 || smallerArray.length < 5) {
            return null; // Not enough elements to find a sequence
        }

        int largestIndex = -1;

        // Iterate over the smaller array to extract all possible 5-string sequences
        for (int i = 0; i <= smallerArray.length - 5; i++) {
            // Create a 5-string sequence from the smaller array
            String[] sequence = new String[5];
            System.arraycopy(smallerArray, i, sequence, 0, 5);

            // Check for the starting index of this sequence in the larger array
            int index = findSubsequenceIndex(largerArray, sequence);

            // Update the largest index found
            if (index > largestIndex) {
                largestIndex = index;
            }
        }

        // Return the element at the fifth index of the latest sequence, or null if no sequence found
        return largestIndex != -1 ? largerArray[largestIndex + 4] : null;
    }

    private static int findSubsequenceIndex(String[] largerArray, String[] sequence) {
        // Iterate through the larger array to find the starting point of the sequence
        for (int i = 0; i <= largerArray.length - sequence.length; i++) {
            boolean match = true;

            // Check if the sequence matches the elements in the larger array
            for (int j = 0; j < sequence.length; j++) {
                if (!largerArray[i + j].equals(sequence[j])) {
                    match = false;
                    break;
                }
            }

            if (match) {
                return i; // Return the starting index of the match
            }
        }

        return -1; // No match found
    }

}
