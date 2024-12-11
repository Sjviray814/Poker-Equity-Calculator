import java.util.*;

public class HandComparer {

    private static final String[] RANKS = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    private static final String[] REVERSE_RANKS = {"A", "K", "Q", "J", "10", "9", "8", "7", "6", "5", "4", "3", "2"};
    private static final String[] REVERSE_RANKS2 = {"A", "5", "4", "3", "2"};

    private static final Map<String, Integer> RANK_MAP = new HashMap<>();

    static {
        for (int i = 0; i < RANKS.length; i++) {
            RANK_MAP.put(RANKS[i], i);
        }
    }

    public static int getWinner(Card[] hand1, Card[] hand2) {
        if (hand1.length < 5 || hand2.length < 5) {
            throw new IllegalArgumentException("Hand size cannot be less than 5.");
        }
        // Logic to compare hands (not implemented in original code)
        return 1;
    }

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
}
