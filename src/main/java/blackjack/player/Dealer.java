package blackjack.player;

import blackjack.deck.Card;

import java.util.ArrayList;
import java.util.List;

public class Dealer {
    private List<Card> cardsInHand;
    private int handValue;
    private boolean isBust = false;

    public Dealer() {
        cardsInHand = new ArrayList<>();
    }

    public void addCardToHand(Card dealtCard) {
        cardsInHand.add(dealtCard);
        calculateCards();
    }

    public void calculateCards() {
        this.handValue = 0;
        for (Card card : cardsInHand) {
            this.handValue += card.rankValue(handValue);
        }
    }

    public int getHandValue() {
        return handValue;
    }

    public List<Card> getCardsInHand() {
        return this.cardsInHand;
    }

    public void setBust(boolean isBust) {
        this.isBust = isBust;
    }

    public boolean getBust() {
        return isBust;
    }

    @Override
    public String toString() {
        return "Dealer {" +
                "cardsInHand=" + cardsInHand +
                "handValue=" + handValue +
                '}';
    }
}
