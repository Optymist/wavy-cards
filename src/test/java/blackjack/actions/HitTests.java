package blackjack.actions;

import blackjack.Play;
import blackjack.PlayerManager;
import blackjack.deck.Card;
import blackjack.deck.Deck;
import blackjack.helperClasses.mockedPlayerManager;
import blackjack.player.Hand;
import blackjack.player.Player;
import blackjack.player.state.Bust;
import blackjack.player.state.Normal;
import blackjack.player.state.Stand;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HitTests {

    private PlayerManager mockPlayerManager;

    @BeforeEach
    public void setUp() {
        mockedPlayerManager.setUp();
        this.mockPlayerManager = mockedPlayerManager.mockPlayerManager;
    }

    @Test
    public void testHitSuccessfulNormalState() {
        Play game = new Play(1);
        Card newCard = new Card(Deck.SUITS[1], Deck.RANKS[3]);
        game.getDeck().addCard(newCard);
        Player player = new Player("fern", mockPlayerManager);
        player.setBet(10);
        Hand hand = player.getCardsInHand();
        hand.addCard(new Card("♥", "2"));
        hand.addCard(new Card("♥", "3"));
        player.performAction(new HitAction(), game);
        assertEquals(10, player.getBet());
        assertEquals(3, hand.getCards().size());
        assertInstanceOf(Normal.class, hand.getState());
    }

    @Test
    public void testHitSuccessfulBustState() {
        Play game = new Play(1);
        Card newCard = new Card(Deck.SUITS[1], Deck.RANKS[3]);
        game.getDeck().addCard(newCard);
        Player player = new Player("fern", mockPlayerManager);
        player.setBet(10);
        Hand hand = player.getCardsInHand();
        hand.addCard(new Card("♥", "10"));
        hand.addCard(new Card("♥", "J"));
        player.performAction(new HitAction(), game);
        assertEquals(10, player.getBet());
        assertEquals(3, hand.getCards().size());
        assertInstanceOf(Bust.class, hand.getState());
    }

}
