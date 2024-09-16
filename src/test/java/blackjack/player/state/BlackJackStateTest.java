package blackjack.player.state;

import blackjack.Play;
import blackjack.PlayerManager;
import blackjack.deck.Card;
import blackjack.player.Player;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * BlackJackStateTest
 */
public class BlackJackStateTest {

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
    public void testBlackJackStateOnAddCard() {
        Play game = new Play(1);
        game.getDeck().addCard(new Card("♥", "Q"));
        game.getDeck().addCard(new Card("♥", "A"));
        Player player = new Player("fern", mockPlayerManager);
        player.setBet(10);
        for (int i = 0; i < 2; i++) {
            player.addCardToHand(game.getDeck().deal());
        }
        assertTrue(player.getState() instanceof BlackJack, 
            String.format("State expected: blackjack, but got: %s", player.getState().toString()));
    }
    
}
