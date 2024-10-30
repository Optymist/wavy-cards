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

public class StandTests {
    private PlayerManager mockPlayerManager;

    @BeforeEach
    public void setUp() {
        mockedPlayerManager.setUp();
        this.mockPlayerManager = mockedPlayerManager.mockPlayerManager;
    }

    @Test
    public void testStandAction() {

        Play game = new Play(1);
        Player player = new Player("fern", mockPlayerManager);
        player.setBet(10);

        Hand hand = player.getCardsInHand();
        hand.addCard(new Card("♥", "2"));
        hand.addCard(new Card("♥", "2"));

        Card newCard = new Card(Deck.SUITS[0], Deck.RANKS[2]);
        game.getDeck().addCard(newCard);
        player.performAction(new StandAction(), game);

        assertEquals(2, hand.getCards().size());
        assertInstanceOf(Stand.class, hand.getState());
    }



}
