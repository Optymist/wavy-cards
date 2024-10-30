package blackjack.actions;

import blackjack.Play;
import blackjack.deck.Card;
import blackjack.deck.Deck;
import blackjack.helperClasses.mockedPlayerManager;
import blackjack.player.Hand;
import blackjack.player.Player;
import blackjack.player.state.Bust;
import blackjack.player.state.Stand;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;


public class DoubleTests {

    @BeforeEach
    public void setUp() {
        mockedPlayerManager.setUp();
    }

    @Test
    public void doubleSuccessfulTest() {
        Play game = new Play(1);
        Player player = new Player("fern", mockedPlayerManager.mockPlayerManager);
        player.setBet(10);

        Hand hand = player.getCardsInHand();
        hand.addCard(new Card("♥", "2"));
        hand.addCard(new Card("♥", "2"));

        Card newCard = new Card(Deck.SUITS[0], Deck.RANKS[2]);
        game.getDeck().addCard(newCard);
        player.performAction(new DoubleAction(), game);

        assertEquals(20, player.getBet());
        assertEquals(3, hand.getCards().size());
        assertInstanceOf(Stand.class, hand.getState());
    }

    @Test
    public void doubleBustTest() {
        Play game = new Play(1);
        Player player = new Player("fern", mockedPlayerManager.mockPlayerManager);
        player.setBet(10);

        Hand hand = player.getCardsInHand();
        hand.addCard(new Card("♥", "Q"));
        hand.addCard(new Card("♥", "K"));

        Card newCard = new Card(Deck.SUITS[0], Deck.RANKS[9]);
        game.getDeck().addCard(newCard);
        player.performAction(new DoubleAction(), game);

        assertEquals(20, player.getBet());
        assertEquals(3, hand.getCards().size());
        assertInstanceOf(Bust.class, hand.getState());
    }

}
