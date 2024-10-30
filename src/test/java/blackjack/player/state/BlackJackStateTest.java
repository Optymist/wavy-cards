package blackjack.player.state;

import blackjack.Play;
import blackjack.PlayerManager;
import blackjack.deck.Card;
import blackjack.helperClasses.mockedPlayerManager;
import blackjack.player.Player;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * BlackJackStateTest
 */
public class BlackJackStateTest {

    private PlayerManager mockPlayerManager;

    @BeforeEach
    public void setUp() {
        mockedPlayerManager.setUp();
        this.mockPlayerManager = mockedPlayerManager.mockPlayerManager;
    }

    @Test
    public void testBlackJackStateOnAddCard() {
        Play game = new Play(1);
        game.getDeck().addCard(new Card("♥", "Q"));
        game.getDeck().addCard(new Card("♥", "A"));
        Player player = new Player("fern", mockPlayerManager);
        player.setBet(10);
        for (int i = 0; i < 2; i++) {
            player.getCardsInHand().addCard(game.getDeck().deal());
        }
        assertInstanceOf(BlackJack.class, player.getCardsInHand().getState(),
                String.format("State expected: blackjack, but got: %s", player.getCardsInHand().getState().toString()));
    }
    
}
