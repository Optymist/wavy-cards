package blackjack.player;

import blackjack.deck.Card;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private final List<Card> cards;
    private int handValue;

    public Hand() {
        this.cards = new ArrayList<>();
        this.handValue = 0;
    }

    public void addCard(Card cardToAdd) {
        this.cards.add(cardToAdd);
        calculateCards();
    }

    public List<Card> getCards() {
        return cards;
    }

    public int getValue() {
        return handValue;
    }

    public void calculateCards() {
        this.handValue = 0;
        for (Card card : cards) {
            this.handValue += card.rankValue(handValue);
        }
    }
}
