package blackjack.actions;

import blackjack.Play;
import blackjack.PlayerManager;
import blackjack.deck.Card;
import blackjack.deck.Deck;
import blackjack.player.Hand;
import blackjack.player.Player;
import blackjack.player.state.Bust;
import blackjack.player.state.Normal;
import blackjack.player.state.Stand;
import blackjack.player.state.Surrender;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class SurrenderTests {

    private PlayerManager mockPlayerManager;

    @BeforeEach
    public void setUp() {
        // Create a mock Socket
        Socket mockSocket = Mockito.mock(Socket.class);

        // Create mocks for BufferedReader and BufferedWriter
        BufferedReader mockBufferedReader = Mockito.mock(BufferedReader.class);
        BufferedWriter mockBufferedWriter = Mockito.mock(BufferedWriter.class);

        // Initialize the mock PlayerManager
        mockPlayerManager = Mockito.mock(PlayerManager.class);

        // Mock behaviors
        when(mockPlayerManager.getPlayers()).thenReturn(new ArrayList<>());
        doNothing().when(mockPlayerManager).sendMessage(anyString());
        doNothing().when(mockPlayerManager).closeEverything(mockSocket, mockBufferedReader, mockBufferedWriter);
    }


    @Test
    public void testSurrenderAction() {

        Play game = new Play(1);
        Player player = new Player("fern", mockPlayerManager);
        player.setBet(10);

        Hand hand = player.getCardsInHand();
        hand.addCard(new Card("♥", "2"));
        hand.addCard(new Card("♥", "2"));

        player.performAction(new SurrenderAction(), game);

        assertEquals(2, hand.getCards().size());
        assertTrue(player.getState() instanceof Surrender);
    }



}
