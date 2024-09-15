package blackjack.protocol;

import blackjack.PlayerManager;
import blackjack.deck.Card;
import blackjack.deck.Deck;
import blackjack.player.Player;
import blackjack.player.state.Normal;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class DecryptJsonTests {
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
    public void testDecryptAvailableActions() throws JsonProcessingException {
        Player fred = new Player("fred", mockPlayerManager);
        fred.setState(new Normal());
        fred.addCardToHand(new Card(Deck.SUITS[0], Deck.RANKS[1]));
        fred.addCardToHand(new Card(Deck.SUITS[0], Deck.RANKS[2]));

        String jsonStringRequest = GenerateJson.generateTurnRequest(fred);
        List<String> actionsAvailable = DecryptJson.getAvailableActions(jsonStringRequest);

        assertTrue(actionsAvailable.contains("hit"));
        assertTrue(actionsAvailable.contains("stand"));
        assertTrue(actionsAvailable.contains("double"));
        assertTrue(actionsAvailable.contains("surrender"));
        assertFalse(actionsAvailable.contains("split"));
    }
}
