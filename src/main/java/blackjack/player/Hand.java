package blackjack.player;

import blackjack.deck.Card;
import blackjack.player.state.BlackJack;
import blackjack.player.state.Bust;
import blackjack.player.state.Normal;
import blackjack.player.state.playerState;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private final List<Card> cards;
    private int handValue;

    public Hand() {
        this.cards = new ArrayList<>();
        this.handValue = 0;
    }

    public playerState addCard(Card cardToAdd) {
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

    private playerState determineState() {
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

}
