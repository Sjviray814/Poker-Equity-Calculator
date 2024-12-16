package Utils;
import java.util.*;

import Structures.Card;

public class HandComparer {

    private static final String[] RANKS = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    private static final String[] REVERSE_RANKS = {"A", "K", "Q", "J", "10", "9", "8", "7", "6", "5", "4", "3", "2"};
    private static final String[] REVERSE_RANKS2 = {"A", "5", "4", "3", "2"};
    private static final String[] HAND_CLASSES = {"SF", "Q", "FH", "F", "S", "TR", "TP", "P", "H"};

    private static final Map<String, Integer> RANK_MAP = new HashMap<>();

    static {
        for (int i = 0; i < RANKS.length; i++) {
            RANK_MAP.put(RANKS[i], i);
        }
    }

    private static final Map<String, Integer> CLASS_MAP = new HashMap<>();

    static {
        for (int i = 0; i < HAND_CLASSES.length; i++) {
            CLASS_MAP.put(HAND_CLASSES[i], i);
        }
    }

    /**
     * Finds the strongest combination of cards.
     *
     * @param cards An array of Card objects (must contain 5 or more cards).
     * @return An array representing the strongest hand of 5 cards.
     */
    public static Card[] getBestHand(Card[] cards) {
        if (cards.length < 5) {
            throw new IllegalArgumentException("The input array must contain at least 5 cards.");
        }

        Card[] strongestHand = null;
        List<Card[]> combinations = new ArrayList<>();
        generateCombinations(cards, 5, 0, new Card[5], 0, combinations);

        for (Card[] combination : combinations) {
            if (strongestHand == null || getWinner(combination, strongestHand) == 1) {
                strongestHand = combination;
            }
        }

        return strongestHand;
    }

    /**
     * Generates all combinations of a given size from the input array.
     *
     * @param cards The input array of cards.
     * @param size The size of each combination.
     * @param start The starting index for combination generation.
     * @param temp Temporary array to hold the current combination.
     * @param index The current index in the temporary array.
     * @param combinations The list to store all generated combinations.
     */
    private static void generateCombinations(Card[] cards, int size, int start, Card[] temp, int index, List<Card[]> combinations) {
        if (index == size) {
            combinations.add(temp.clone());
            return;
        }

        for (int i = start; i <= cards.length - (size - index); i++) {
            temp[index] = cards[i];
            generateCombinations(cards, size, i + 1, temp, index + 1, combinations);
        }
    }

    public static int getWinner(Card[] hand1, Card[] hand2) {
        if (hand1.length < 5 || hand2.length < 5) {
            throw new IllegalArgumentException("Hand size cannot be less than 5.");
        }
        String handType1 = getHandType(hand1);
        String handType2 = getHandType(hand2);

        String[] handSplit1 = handType1.split(" ");
        String[] handSplit2 = handType2.split(" ");

        if (classToInt(handSplit1[0]) < classToInt(handSplit2[0])) {
            return 1;
        } else if (classToInt(handSplit2[0]) < classToInt(handSplit1[0])) {
            return 2;
        } else {
            for (int i = 1; i < handSplit1.length; i++) {
                if (rankToInt(handSplit1[i]) > rankToInt(handSplit2[i])) {
                    return 1;
                } else if (rankToInt(handSplit1[i]) < rankToInt(handSplit2[i])) {
                    return 2;
                }
            }
        }

        return 0;
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

    public static String getHandType(Card[] hand) {
        if (hand.length < 5) {
            throw new IllegalArgumentException("Hand size must be at least 5 for getHandType.");
        }

        Map<String, Integer> frequencyMap = new HashMap<>();
        Map<String, Integer> suitMap = new HashMap<>();

        for (Card card : hand) {
            frequencyMap.put(card.getRank(), frequencyMap.getOrDefault(card.getRank(), 0) + 1);
            suitMap.put(card.getSuit(), suitMap.getOrDefault(card.getSuit(), 0) + 1);
        }

        List<String> ones = new ArrayList<>();
        List<String> pairs = new ArrayList<>();
        List<String> trips = new ArrayList<>();
        List<String> quads = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            switch (entry.getValue()) {
                case 1: 
                    ones.add(entry.getKey());
                    break;
                case 2: 
                    pairs.add(entry.getKey());
                    break;
                case 3: 
                    trips.add(entry.getKey());
                    break;
                case 4: 
                    quads.add(entry.getKey());
                    break;
            }
        }

        ones.sort(Comparator.comparingInt(RANK_MAP::get).reversed());
        pairs.sort(Comparator.comparingInt(RANK_MAP::get).reversed());
        trips.sort(Comparator.comparingInt(RANK_MAP::get).reversed());
        quads.sort(Comparator.comparingInt(RANK_MAP::get).reversed());

        if (!quads.isEmpty()) {
            return "Q " + quads.get(0) + " " + ones.get(0);
        }

        if (!trips.isEmpty()) {
            if (!pairs.isEmpty() || trips.size() > 1) {
                String pair = (!pairs.isEmpty()) ? pairs.get(0) : trips.get(1);
                return "FH " + trips.get(0) + " " + pair;
            }
            return "TR " + trips.get(0) + " " + ones.get(0) + " " + ones.get(1);
        }

        if (pairs.size() >= 2) {
            return "TP " + pairs.get(0) + " " + pairs.get(1) + " " + ones.get(0);
        }

        if (!pairs.isEmpty()) {
            return "P " + pairs.get(0) + " " + ones.get(0) + " " + ones.get(1) + " " + ones.get(2);
        }

        for (Map.Entry<String, Integer> entry : suitMap.entrySet()) {
            if (entry.getValue() >= 5) {
                List<String> flushCards = new ArrayList<>();
                for (Card card : hand) {
                    if (card.getSuit().equals(entry.getKey())) {
                        flushCards.add(card.getRank());
                    }
                }
                flushCards.sort(Comparator.comparingInt(RANK_MAP::get).reversed());

                if (containsStraight(flushCards, REVERSE_RANKS)) {
                    return "SF " + flushCards.get(0);
                }

                if (containsStraight(flushCards, REVERSE_RANKS2)) {
                    return "SF " + flushCards.get(0);
                }

                return "F " + String.join(" ", flushCards.subList(0, 5));
            }
        }

        if (containsStraight(ones, REVERSE_RANKS)) {
            return "S " + ones.get(0);
        }

        if (containsStraight(ones, REVERSE_RANKS2)) {
            return "S " + ones.get(0);
        }

        return "H " + String.join(" ", ones.subList(0, 5));
    }

    private static boolean containsStraight(List<String> ranks, String[] sequence) {
        Set<String> rankSet = new HashSet<>(ranks);
        for (int i = 0; i <= sequence.length - 5; i++) {
            boolean isStraight = true;
            for (int j = 0; j < 5; j++) {
                if (!rankSet.contains(sequence[i + j])) {
                    isStraight = false;
                    break;
                }
            }
            if (isStraight) return true;
        }
        return false;
    }

    public static int rankToInt(String rank) {
        Integer value = RANK_MAP.get(rank);
        if (value == null) {
            throw new IllegalArgumentException("Invalid rank: " + rank);
        }
        return value;
    }

    public static int classToInt(String handClass) {
        Integer value = CLASS_MAP.get(handClass);
        if (value == null) {
            throw new IllegalArgumentException("Invalid rank: " + handClass);
        }
        return value;
    }
}
