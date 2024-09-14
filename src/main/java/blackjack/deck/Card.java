package blackjack.deck;

import blackjack.player.Hand;

import java.util.Objects;

public class Card implements Comparable<Card> {
    private final String suit;
    private final String rank;

    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
    }

    @Override
    public String toString() {
        return rank + suit;
    }

    public int getValue() {
        try {
            return Integer.parseInt(rank);
        } catch (NumberFormatException e) {
            if (rank.contains("A")) {
                return 11;
            } else {
                return 10;
            }
        }
    }

    public int rankValue(Hand hand) {
        try {
            return Integer.parseInt(this.rank);
        } catch (NumberFormatException n) {
            return faceCardValue(this, hand.getValue());
        }
    }

    private int faceCardValue(Card faceCard, int handValue) {
        if (faceCard.rank.equals("A")) {
            return aceCardValue(handValue);
        } else {
            return 10;
        }
    }

    private int aceCardValue(int handValue) {
        if (handValue > 10) {
            return 1;
        } else {
            return 11;
        }
    }

    @Override
    public int compareTo(Card o) {
        if (Objects.equals(this.rank, o.rank)) {
            return 0;
        } else {
            return Integer.compare(this.getValue(), o.getValue());
        }
    }
}
