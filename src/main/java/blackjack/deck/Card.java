package blackjack.deck;

public class Card {
    private String suit;
    private String rank;

    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
    }

    @Override
    public String toString() {
        return rank + suit;
    }

    public int rankValue(int valueOfHand) {
        try {
            return Integer.parseInt(this.rank);
        } catch (NumberFormatException n) {
            return faceCardValue(this, valueOfHand);
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
        if (handValue > 20) {
            return 1;
        } else {
            return 11;
        }
    }


}
