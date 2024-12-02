import java.util.ArrayList;

public class Driver {
    public static void main(String[] args) {
        // Deck d = new Deck();
        // System.out.println(d);
        // d.shuffle();
        // System.out.println(d);

        ArrayList<String> kickers = new ArrayList<String>();
        kickers.add("8");
        kickers.add("A");
        kickers.add("Q");
        kickers.add("3");
        String[] testKickers = HandComparer.getKickers(kickers);
        for (String k : testKickers) {
            System.out.println(k);
        }
    }
}
