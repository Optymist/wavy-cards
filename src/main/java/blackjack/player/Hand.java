package blackjack.player;

import blackjack.deck.Card;
import blackjack.player.state.BlackJack;
import blackjack.player.state.Bust;
import blackjack.player.state.Normal;
import blackjack.player.state.handState;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private final List<Card> cards;
    private int handValue;
    private handState state;

    public Hand() {
        this.cards = new ArrayList<>();
        this.handValue = 0;
        this.state = new Normal();
    }

    public handState addCard(Card cardToAdd) {
        this.cards.add(cardToAdd);
        calculateCards();
        return determineState();
    }

    public List<Card> getCards() {
        return cards;
    }

    public int getValue() {
        return handValue;
    }

    public void calculateCards() {
        List<Card> sortedCards = sortCards();
        this.handValue = 0;
        for (Card card : sortedCards) {
            this.handValue += card.rankValue(this);
        }
    }

    private handState determineState() {
        if (cards.size() == 2 && handValue == 21) {
            return new BlackJack();
        } else if (handValue > 21) {
            return new Bust();
        } else {
            return new Normal();
        }
    }

    private List<Card> sortCards() {
        List<Card> sortedCards = new ArrayList<>(this.cards);
        sortedCards.sort(Card::compareTo);
        return sortedCards;
    }

    public void clearCards() {
        this.cards.clear();
    }

    public void setState(handState state) {
        this.state = state;
    }

    public handState getState() {
        return state;
    }

    @Override
    public String toString() {
        return "Hand{" +
                "cards=" + cards +
                ", handValue=" + handValue +
                ", state=" + state +
                '}';
    }
}
