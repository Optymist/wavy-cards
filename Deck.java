import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Deck {
    // Unicode characters for the four suits {spades, hearts, diamonds, clubs}
    private static final String[] SUITS = {"\u2660", "\u2665", "\u2666", "\u2663"};
    private static final String[] RANKS = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};

    static class Card {
        private String suit;
        private String rank;

        public Card(String suit, String rank) {
            this.suit = suit;
            this.rank = rank;
        }

        @Override
        public String toString() {
            return rank + suit;
        }
    }

    private static List<Card> createDeck(int numDecks) {
        List<Card> deck = new ArrayList<>();

        for (int i = 0; i < numDecks; i++) {
            for (String suit : SUITS) {
                for (String rank : RANKS) {
                    deck.add(new Card(suit, rank));
                }
            }
        }
        return deck;
    }

    private static void printDeck(List<Card> deck) {
        for (Card card : deck) {
            System.out.println(card);
        }
    }

    public static void main(String[] args) {
        List<Card> createdDeck = createDeck(2);
        // Shuffle the deck
        Collections.shuffle(createdDeck);
    }
}
