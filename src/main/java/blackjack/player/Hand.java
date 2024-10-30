package blackjack.player;

import blackjack.deck.Card;
import blackjack.player.state.BlackJack;
import blackjack.player.state.Bust;
import blackjack.player.state.Normal;
import blackjack.player.state.handState;

import java.util.ArrayList;
import java.util.List;

/**
 * Hand class that handles the cards in a player's hand.
 */
public class Hand {
    private final List<Card> cards;
    private int handValue;
    private handState state;
    private boolean beanSplit;

    /**
     * Initialize the hand with a handValue of 0 and state of Normal.
     */
    public Hand() {
        this.cards = new ArrayList<>();
        this.handValue = 0;
        this.state = new Normal();
        this.beanSplit = false;
    }

    /**
     * Adds a card to the hand and calls calculateCards and returns the state of the hand.
     * @param cardToAdd --> pretty self-explanatory.
     * @return the hand's state after determination.
     */
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

    /**
     * Gets the handValue of the hand by counting all the card values.
     */
    public void calculateCards() {
        List<Card> sortedCards = sortCards();
        this.handValue = 0;
        for (Card card : sortedCards) {
            this.handValue += card.rankValue(this);
        }
        if (sortedCards.size() == 2 && handValue == 21) {
            this.state = new BlackJack();
        }
    }

    /**
     * Determine what state the hand is currently.
     * Normal, Bust or BlackJack.
     * @return the state.
     */
    private handState determineState() {
        if (cards.size() == 2 && handValue == 21) {
            return new BlackJack();
        } else if (handValue > 21) {
            return new Bust();
        } else {
            return new Normal();
        }
    }

    /**
     * Sorts the cards in rank order.
     * @return the sorted cards.
     */
    private List<Card> sortCards() {
        List<Card> sortedCards = new ArrayList<>(this.cards);
        sortedCards.sort(Card::compareTo);
        return sortedCards;
    }

    /**
     * Removes all the cards from the hand and sets the state to normal
     */
    public void clearCards() {
        this.cards.clear();
        this.state = new Normal();
    }

    public void setState(handState state) {
        this.state = state;
    }

    public handState getState() {
        return state;
    }

    public boolean isBeanSplit() {
        return beanSplit;
    }

    public void setBeanSplit(boolean beanSplit) {
        this.beanSplit = beanSplit;
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
