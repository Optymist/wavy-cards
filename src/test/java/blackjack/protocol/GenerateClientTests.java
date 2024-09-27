package blackjack.protocol;

import blackjack.Client.protocol.Generate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenerateClientTests {
    @Test
    public void testGenerateTurnResponse() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        String response1 = Generate.generateTurnResponse("sal", "stand");
        JsonNode node1 = mapper.readTree(response1);
        String response2 = Generate.generateTurnResponse("fred", "double");
        JsonNode node2 = mapper.readTree(response2);

        assertEquals("turnResponse", node1.get("protocolType").asText());
        assertEquals(node1.get("protocolType").asText(), node2.get("protocolType").asText());

        assertEquals("sal", node1.get("playerName").asText());
        assertEquals("fred", node2.get("playerName").asText());

        assertEquals("stand", node1.get("action").asText());
        assertEquals("double", node2.get("action").asText());
    }

    @Test
    public void testGenerateBetResponse() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        String response = Generate.generateBetResponse("sal", 20);
        JsonNode node = mapper.readTree(response);

        assertEquals("betResponse", node.get("protocolType").asText());
        assertEquals("sal", node.get("playerName").asText());
        assertEquals(20, node.get("bet").asInt());
    }
}
