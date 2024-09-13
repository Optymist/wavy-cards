package blackjack.protocol;

import blackjack.Play;
import blackjack.PlayerManager;
import blackjack.deck.Card;
import blackjack.deck.Deck;
import blackjack.player.Player;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

public class GenerateJsonTests {
    @Test
    public void testGenerateTurnRequestNoSplit() throws JsonProcessingException {
        Play game = new Play(1);
        Player fred = new Player("fred", new PlayerManager(new Socket(), 1));
        Play.addPlayer(fred);
        fred.getCardsInHand().addCard(new Card(Deck.RANKS[0], Deck.RANKS[4]));
        fred.getCardsInHand().addCard(new Card(Deck.RANKS[0], Deck.RANKS[2]));
        String jsonString = GenerateJson.generateTurnRequest(game, fred);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(jsonString);

        assertEquals("turnRequest", node.get("protocolType").asText());
        assertEquals("turnRequest", node.get("protocolType").asText());

    }
}
