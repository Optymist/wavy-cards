package blackjack.protocol;

import blackjack.Play;
import blackjack.PlayerManager;
import blackjack.deck.Card;
import blackjack.deck.Deck;
import blackjack.player.Dealer;
import blackjack.player.Player;
import blackjack.player.state.Bust;
import blackjack.player.state.Normal;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class GenerateJsonTests {
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
    public void testGenerateTurnRequestNoSplit() throws JsonProcessingException {
        Player fred = new Player("fred", mockPlayerManager);
        fred.setState(new Normal());

        fred.getCardsInHand().addCard(new Card(Deck.RANKS[0], Deck.RANKS[4]));
        fred.getCardsInHand().addCard(new Card(Deck.RANKS[0], Deck.RANKS[2]));
        String jsonString = GenerateJson.generateTurnRequest(fred);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(jsonString);

        assertEquals("turnRequest", node.get("protocolType").asText());
        assertEquals("fred", node.get("currentPlayer").asText());
        assertFalse(node.get("actions").toString().contains("split"));
        assertTrue(node.get("actions").toString().contains("double"));
        assertTrue(node.get("actions").toString().contains("hit"));
        assertTrue(node.get("actions").toString().contains("stand"));
        assertTrue(node.get("actions").toString().contains("surrender"));
    }

    @Test
    public void testGenerateTurnRequestWithSplit() throws JsonProcessingException {
        Player fred = new Player("fred", mockPlayerManager);
        fred.setState(new Normal());

        fred.getCardsInHand().addCard(new Card(Deck.RANKS[0], Deck.RANKS[4]));
        fred.getCardsInHand().addCard(new Card(Deck.RANKS[0], Deck.RANKS[4]));
        String jsonString = GenerateJson.generateTurnRequest(fred);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(jsonString);

        assertEquals("turnRequest", node.get("protocolType").asText());
        assertEquals("fred", node.get("currentPlayer").asText());
        assertTrue(node.get("actions").toString().contains("split"));
        assertTrue(node.get("actions").toString().contains("double"));
        assertTrue(node.get("actions").toString().contains("hit"));
        assertTrue(node.get("actions").toString().contains("stand"));
        assertTrue(node.get("actions").toString().contains("surrender"));
    }

    @Test
    public void testGenerateUpdateResponse() throws JsonProcessingException {
        Play game = new Play(2);
        Player sal = new Player("sal", mockPlayerManager);
        Player romeo = new Player("romeo", mockPlayerManager);
        Dealer dealer = game.getDealer();
        game.addPlayer(sal);
        game.addPlayer(romeo);
        sal.setState(new Normal());
        romeo.setState(new Bust());

        sal.addCardToHand(new Card(Deck.RANKS[0], Deck.RANKS[4]));
        romeo.addCardToHand(new Card(Deck.RANKS[0], Deck.RANKS[2]));
        dealer.addCardToHand(new Card(Deck.RANKS[0], Deck.RANKS[10]));

        String jsonString = GenerateJson.generateUpdateRequest(game);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(jsonString);
        System.out.println(node);

        assertEquals("update", node.get("protocolType").asText());
        assertEquals("sal", node.get("currentPlayer").asText());
        assertTrue(node.toString().contains("players"));
        assertTrue(node.get("players").toString().contains("sal"));
        assertTrue(node.get("players").toString().contains("normal"));

        assertTrue(node.get("players").toString().contains("romeo"));

        assertTrue(node.toString().contains("dealer"));
        assertEquals("normal", node.get("dealer").get("state").toString());
        assertTrue(node.get("dealer").get("hand").toString().contains("Q♠"));
        assertEquals("10", node.get("dealer").get("handValue").toString());

    }
    
}
