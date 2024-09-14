package blackjack.card;

import blackjack.deck.Card;
import blackjack.deck.Deck;
import blackjack.player.Hand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CardTests {
    @Test
    public void testGetValue() {
        Card card1 = new Card(Deck.SUITS[0], Deck.RANKS[0]);
        assertEquals(2, card1.getValue());

        Card card2 = new Card(Deck.SUITS[1], Deck.RANKS[7]);
        assertEquals(9, card2.getValue());

        Card card3 = new Card(Deck.SUITS[2], Deck.RANKS[12]);
        assertEquals(11, card3.getValue());

        Card card4 = new Card(Deck.SUITS[3], Deck.RANKS[10]);
        assertEquals(10, card4.getValue());
    }

    @Test
    public void testCompareTo() {
        Card card1 = new Card(Deck.SUITS[0], Deck.RANKS[0]);
        Card card2 = new Card(Deck.SUITS[1], Deck.RANKS[0]);
        assertEquals(0, card1.compareTo(card2));

        Card card3 = new Card(Deck.SUITS[2], Deck.RANKS[7]);
        Card card4 =  new Card(Deck.SUITS[3], Deck.RANKS[12]);
        assertTrue(card3.compareTo(card4) < 0);
        assertTrue(card4.compareTo(card3) > 0);
    }

    @Test
    public void testToString() {
        Card card1 = new Card(Deck.SUITS[0], Deck.RANKS[0]);
        assertEquals("2♠", card1.toString());

        Card card2 = new Card(Deck.SUITS[1], Deck.RANKS[1]);
        assertEquals("3♥", card2.toString());

        Card card3 = new Card(Deck.SUITS[2], Deck.RANKS[12]);
        assertEquals("A♦", card3.toString());
    }

    @Test
    public void testRankValueOfNumberCards() {
        Hand hand = new Hand();
        Card four = new Card(Deck.SUITS[0], Deck.RANKS[2]);
        hand.addCard(four);
        assertEquals(4, four.rankValue(hand));

        Card ten = new Card(Deck.SUITS[0], Deck.RANKS[10]);
        hand.addCard(ten);
        assertEquals(10, ten.rankValue(hand));
    }

    @Test
    public void testRankValueOfAces() {
        Hand hand = new Hand();
        Card card1 = new Card(Deck.SUITS[0], Deck.RANKS[3]);
        Card card2 = new Card(Deck.SUITS[1], Deck.RANKS[12]);
        hand.addCard(card1);
        assertEquals(11, card2.rankValue(hand));

        Hand secondHand = new Hand();
        Card card3 = new Card(Deck.SUITS[0], Deck.RANKS[10]);
        Card card4 =  new Card(Deck.SUITS[1], Deck.RANKS[11]);
        secondHand.addCard(card3);
        secondHand.addCard(card4);
        Card ace = new Card(Deck.SUITS[0], Deck.RANKS[12]);
        secondHand.addCard(ace);

        assertEquals(1, ace.rankValue(secondHand));
    }
}
