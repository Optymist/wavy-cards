package blackjack.deck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    // Unicode characters for the four suits {spades, hearts, diamonds, clubs}
    public static final String[] SUITS = {"\u2660", "\u2665", "\u2666", "\u2663"};
    public static final String[] RANKS = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    private static List<Card> PLAY_DECK = new ArrayList<>();

    public Deck(int numDecks) {
        PLAY_DECK = createDeck(numDecks);
        Collections.shuffle(PLAY_DECK);
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

    public Card deal() {
        if (PLAY_DECK.isEmpty()) {
            return null;
        }
        return PLAY_DECK.remove(PLAY_DECK.size() - 1);
    }

    public void addCard(Card newCard) {
        PLAY_DECK.add(newCard);
    }

}
