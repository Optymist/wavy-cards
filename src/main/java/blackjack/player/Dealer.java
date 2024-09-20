package blackjack.player;

import blackjack.deck.Card;


public class Dealer {
    private Hand cardsInHand;
    private boolean isBust = false;

    public Dealer() {
        cardsInHand = new Hand();
    }

    public void addCardToHand(Card dealtCard) {
        cardsInHand.addCard(dealtCard);
    }

    public Hand getCardsInHand() {
        return this.cardsInHand;
    }

//    public void setState(playerState state) {
//        this.state = state;
//    }
//
//    public playerState getState() {
//        return this.state;
//    }

    public void setBust(boolean isBust) {
        this.isBust = isBust;
    }

    public int getHandValue() {
        return cardsInHand.getValue();
    }

    public boolean getBust() {
        return isBust;
    }

    @Override
    public String toString() {
        return "Dealer {" +
                "cardsInHand=" + cardsInHand.getCards() +
                "handValue=" + cardsInHand.getValue() +
                '}';
    }
}
