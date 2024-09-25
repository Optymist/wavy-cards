package blackjack.protocol;

import blackjack.Play;
import blackjack.PlayerManager;
import blackjack.deck.Card;
import blackjack.deck.Deck;
import blackjack.helperClasses.mockedPlayerManager;
import blackjack.player.Dealer;
import blackjack.player.Player;
import blackjack.player.state.Bust;
import blackjack.player.state.Normal;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GenerateJsonServerTests {
    private PlayerManager mockPlayerManager;

    @BeforeEach
    public void setUp() {
       mockedPlayerManager.setUp();
       this.mockPlayerManager = mockedPlayerManager.mockPlayerManager;
    }

    @Test
    public void testGenerateTurnRequestNoSplit() throws JsonProcessingException {
        Player fred = new Player("fred", mockPlayerManager);
        fred.getCardsInHand().setState(new Normal());

        fred.getCardsInHand().addCard(new Card(Deck.SUITS[0], Deck.RANKS[4]));
        fred.getCardsInHand().addCard(new Card(Deck.SUITS[0], Deck.RANKS[2]));
        String jsonString = GenerateJson.generateTurnRequest(fred, fred.getCardsInHand());
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
        fred.getCardsInHand().setState(new Normal());

        fred.getCardsInHand().addCard(new Card(Deck.SUITS[0], Deck.RANKS[4]));
        fred.getCardsInHand().addCard(new Card(Deck.SUITS[0], Deck.RANKS[4]));
        String jsonString = GenerateJson.generateTurnRequest(fred, fred.getCardsInHand());
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
    public void testGenerateUpdateRequest() throws JsonProcessingException {
        Play game = new Play(2);
        game.clearAllPlayers();

        Player sal = new Player("sal", mockPlayerManager);
        Player romeo = new Player("romeo", mockPlayerManager);
        Dealer dealer = game.getDealer();

        sal.getCardsInHand().setState(new Normal());
        sal.setBet(20);
        romeo.getCardsInHand().setState(new Bust());
        romeo.setBet(10);

        sal.getCardsInHand().addCard(new Card(Deck.SUITS[0], Deck.RANKS[4]));
        romeo.getCardsInHand().addCard(new Card(Deck.SUITS[0], Deck.RANKS[2]));
        dealer.getCardsInHand().addCard(new Card(Deck.SUITS[0], Deck.RANKS[10]));

        String jsonString = GenerateJson.generateUpdate(game);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(jsonString);

        assertEquals("update", node.get("protocolType").asText());
        assertEquals("sal", node.get("currentPlayer").asText());
        assertTrue(node.toString().contains("players"));

        assertTrue(node.get("players").toString().contains("sal"));
        assertEquals("normal", node.get("players").get("sal").get("state").asText());
        assertEquals("[\"6♠\"]", node.get("players").get("sal").get("hand").toString());
        assertEquals("6", node.get("players").get("sal").get("handValue").asText());
        assertEquals("20.0", node.get("players").get("sal").get("bet").asText());
        assertEquals("2500.0", node.get("players").get("sal").get("money").asText());

        assertTrue(node.get("players").toString().contains("romeo"));
        assertEquals("bust", node.get("players").get("romeo").get("state").asText());
        assertEquals("[\"4♠\"]", node.get("players").get("romeo").get("hand").toString());
        assertEquals("4", node.get("players").get("romeo").get("handValue").asText());
        assertEquals("10.0", node.get("players").get("romeo").get("bet").asText());
        assertEquals("2500.0", node.get("players").get("romeo").get("money").asText());

        assertTrue(node.toString().contains("dealer"));
        assertEquals("normal", node.get("dealer").get("state").asText());
        assertTrue(node.get("dealer").get("hand").toString().contains("Q♠"));
        assertEquals("10", node.get("dealer").get("handValue").toString());
    }
    
}
