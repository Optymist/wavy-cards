package blackjack.actions;

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
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


public class DoubleTests {
    private PlayerManager mockPlayerManager;
    private Socket mockSocket;
    private BufferedReader mockBufferedReader;
    private BufferedWriter mockBufferedWriter;

    @BeforeEach
    public void setUp() throws IOException {
        // Create a mock Socket
        mockSocket = Mockito.mock(Socket.class);

        // Create mocks for BufferedReader and BufferedWriter
        mockBufferedReader = Mockito.mock(BufferedReader.class);
        mockBufferedWriter = Mockito.mock(BufferedWriter.class);

        // Initialize the mock PlayerManager
        mockPlayerManager = Mockito.mock(PlayerManager.class);

        // Mock behaviors
        when(mockPlayerManager.getPlayers()).thenReturn(new ArrayList<>());
        doNothing().when(mockPlayerManager).sendMessage(anyString());
        doNothing().when(mockPlayerManager).closeEverything(mockSocket, mockBufferedReader, mockBufferedWriter);
    }


    @Test
    public void doubleSuccessfulTest() {
        Play game = new Play(1);
        Player player = new Player("fern", mockPlayerManager);
        player.setBet(10);
        player.addCardToHand(new Card("♥", "2"));
        player.addCardToHand(new Card("♥", "2"));
        player.performAction("double", game);
        assertEquals(20, player.getBet());
        assertEquals(3, player.getCardsInHand().size());
        assertTrue(player.isStanding());
    }

}
