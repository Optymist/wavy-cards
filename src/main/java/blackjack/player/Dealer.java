package blackjack.player;

import blackjack.deck.Card;

import java.util.ArrayList;
import java.util.List;

public class Dealer {
    private List<Card> cardsInHand;

    public Dealer() {
        cardsInHand = new ArrayList<>();
    }

    public void addCardToHand(Card dealtCard) {
        cardsInHand.add(dealtCard);
    }

    @Override
    public String toString() {
        return "Dealer {" +
                "cardsInHand=" + cardsInHand +
                '}';
    }
}
