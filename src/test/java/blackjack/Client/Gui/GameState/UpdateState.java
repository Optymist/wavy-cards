package blackjack.Client.Gui.GameState;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * UpdateState
 */
public class UpdateState {

    private String updateMessage = "{\"protocolType\":\"update\",\"currentPlayer\":\"Sal\",\"players\":{\"Sal\":{\"name\":\"Sal\",\"state\":\"normal\",\"hand\":[\"J♣\",\"3♣\"],\"handValue\":13,\"money\":2500.0,\"bet\":20.0},\"hal\":{\"name\":\"hal\",\"state\":\"normal\",\"hand\":[\"4♠\",\"8♠\"],\"handValue\":12,\"money\":2500.0,\"bet\":10.0}},\"dealer\":{\"state\":\"normal\",\"hand\":[\"3♠\",\"5♦\"],\"handValue\":8}}";

    private JsonNode messageNode;
    private JsonNode playersNode;

    @BeforeEach
    public void setUpTests() {
        ObjectMapper mapper = new ObjectMapper();
        try {
			messageNode = mapper.readTree(updateMessage);
            playersNode = messageNode.get("players");
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    }

    @Test
    public void testUpdateState() {

    }



    
}
