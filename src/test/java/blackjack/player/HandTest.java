package blackjack.player;

import blackjack.deck.Deck;
import blackjack.deck.Card;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HandTest {

    @Test
    public void testHandGettersAndSetters() {
        Hand hand = new Hand();
        Card card1 = new Card(Deck.SUITS[0], Deck.RANKS[0]);
        Card card2 =new Card(Deck.SUITS[1], Deck.RANKS[0]);
        hand.addCard(card1);
        hand.addCard(card2);

        List<Card> cards = hand.getCards();
        assertEquals(card1, cards.get(0));
        assertEquals(card2, cards.get(1));

        int value = hand.getValue();
        assertEquals(4, value);
    }
}
