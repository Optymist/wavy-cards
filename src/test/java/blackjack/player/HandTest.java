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
        Card card2 = new Card(Deck.SUITS[1], Deck.RANKS[0]);
        hand.addCard(card1);
        hand.addCard(card2);

        List<Card> cards = hand.getCards();
        assertEquals(card1, cards.get(0));
        assertEquals(card2, cards.get(1));

        int value = hand.getValue();
        assertEquals(4, value);
    }

    @Test
    public void testAddCardCalculation() {
        Hand hand = new Hand();
        Card card1 = new Card(Deck.SUITS[0], Deck.RANKS[0]);
        Card card2 = new Card(Deck.SUITS[1], Deck.RANKS[0]);
        hand.addCard(card1);
        hand.addCard(card2);

        assertEquals(4, hand.getValue());
    }

    @Test
    public void testCalculateCardWithAceAndValue10() {
        Hand hand = new Hand();
        Card card1 = new Card(Deck.SUITS[0], Deck.RANKS[3]);
        Card card2 = new Card(Deck.SUITS[1], Deck.RANKS[3]);
        hand.addCard(card1);
        hand.addCard(card2);
        assertEquals(10, hand.getValue());

        Card aceCard = new Card(Deck.SUITS[0], Deck.RANKS[12]);
        hand.addCard(aceCard);

        assertEquals(21, hand.getValue());
    }

    @Test
    public void testCalculateCardsWithAceAndValue20() {
        Hand hand = new Hand();
        Card card1 = new Card(Deck.SUITS[0], Deck.RANKS[11]);
        Card card2 = new Card(Deck.SUITS[1], Deck.RANKS[10]);
        hand.addCard(card1);
        hand.addCard(card2);
        assertEquals(20, hand.getValue());

        Card aceCard = new Card(Deck.SUITS[0], Deck.RANKS[12]);
        hand.addCard(aceCard);

        assertEquals(21, hand.getValue());
    }

    @Test
    public void testCalculateCardsWithAceAndValue11() {
        Hand hand = new Hand();
        Card card1 = new Card(Deck.SUITS[0], Deck.RANKS[3]);
        Card card2 = new Card(Deck.SUITS[1], Deck.RANKS[4]);
        hand.addCard(card1);
        hand.addCard(card2);
        assertEquals(11, hand.getValue());

        Card aceCard = new Card(Deck.SUITS[0], Deck.RANKS[12]);
        hand.addCard(aceCard);

        assertEquals(12, hand.getValue());
    }

    @Test
    public void testAceAlreadyInHandAnd10Drawn() {
        Hand hand = new Hand();
        Card card1 = new Card(Deck.SUITS[0], Deck.RANKS[12]);
        Card card2 = new Card(Deck.SUITS[1], Deck.RANKS[7]);
        hand.addCard(card1);
        hand.addCard(card2);
        assertEquals(20, hand.getValue());

        Card tenCard = new Card(Deck.SUITS[0], Deck.RANKS[11]);
        hand.addCard(tenCard);
        assertEquals(20, hand.getValue());
    }

    @Test
    public void testAceAlreadyInHandAndADrawn() {
        Hand hand = new Hand();
        Card card1 = new Card(Deck.SUITS[0], Deck.RANKS[12]);
        Card card2 = new Card(Deck.SUITS[1], Deck.RANKS[7]);
        hand.addCard(card1);
        hand.addCard(card2);
        assertEquals(20, hand.getValue());

        Card aceCard = new Card(Deck.SUITS[0], Deck.RANKS[12]);
        hand.addCard(aceCard);
        assertEquals(21, hand.getValue());
    }
}
