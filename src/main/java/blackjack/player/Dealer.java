package blackjack.player;

import blackjack.deck.Card;

/**
 * Dealer class:
 * Holds all the necessary properties and methods for a dealer in the game.
 */
public class Dealer {
    private Hand cardsInHand;
    private boolean isBust = false;

    /**
     * Initialize the dealer's hand.
     */
    public Dealer() {
        cardsInHand = new Hand();
    }

    public Hand getCardsInHand() {
        return this.cardsInHand;
    }

    public void setBust(boolean isBust) {
        this.isBust = isBust;
    }

    public int getHandValue() {
        return cardsInHand.getValue();
    }

    public boolean getBust() {
        return isBust;
    }

    /**
     * Reset the dealer's properties so that it is set for a new round.
     */
    public void reset() {
        this.cardsInHand.clearCards();
        this.isBust = false;
    }

    @Override
    public String toString() {
        return "Dealer {" +
                "cardsInHand=" + cardsInHand.getCards() +
                "handValue=" + cardsInHand.getValue() +
                '}';
    }
}
