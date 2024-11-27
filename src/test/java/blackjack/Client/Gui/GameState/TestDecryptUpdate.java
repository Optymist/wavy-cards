package blackjack.Client.Gui.GameState;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import blackjack.Client.Gui.GameState.PlayerState;


/**
 * DecryptUpdate
 */
public class TestDecryptUpdate {

    private String updateMessage = "{\"protocolType\":\"update\",\"currentPlayer\":\"Sal\",\"players\":{\"Sal\":{\"name\":\"Sal\",\"state\":\"normal\",\"hand\":[\"J♣\",\"3♣\"],\"handValue\":13,\"money\":2500.0,\"bet\":20.0},\"hal\":{\"name\":\"hal\",\"state\":\"normal\",\"hand\":[\"4♠\",\"8♠\"],\"handValue\":12,\"money\":2500.0,\"bet\":10.0}},\"dealer\":{\"state\":\"normal\",\"hand\":[\"3♠\",\"5♦\"],\"handValue\":8}}";

    private JsonNode messageNode;
    private JsonNode playersNode;
    private JsonNode salNode;
    private JsonNode halNode;
    
    @BeforeEach
    public void setUpMessage() {
        ObjectMapper mapper = new ObjectMapper();
        try {
			messageNode = mapper.readTree(updateMessage);
            playersNode = messageNode.get("players");
            salNode = playersNode.get("Sal");
            halNode = playersNode.get("hal");
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    }

    @Test
    public void getPlayerStateName() {
        PlayerState playerState = new PlayerState(salNode);

        assertTrue(playerState.getName().equals("Sal"));
    }

    @Test
    public void getPlayerStateState() {
        PlayerState playerState = new PlayerState(salNode);

        assertTrue(playerState.getState().equals("normal"));
    }

    @Test
    public void getPlayerStateHand() {
        PlayerState playerState = new PlayerState(salNode);

        assertArrayEquals(playerState.getHand(), new String[]{"J♣","3♣"});
    }

    @Test
    public void getPlayerStateMoney() {
        PlayerState playerState = new PlayerState(salNode);

        assertEquals(playerState.getMoney(), 2500);
    }


    @Test
    public void getPlayerStateBet() {
        PlayerState playerState = new PlayerState(salNode);

        assertEquals(playerState.getBet(), 20);
    }

    @Test
    public void createPlayerState() {

        PlayerState playerState = new PlayerState(salNode);

        assertTrue(playerState.getName().equals("Sal"));
        assertTrue(playerState.getState().equals("normal"));
        assertArrayEquals(playerState.getHand(), new String[]{"J♣","3♣"});
        assertEquals(playerState.getMoney(), 2500);
        assertEquals(playerState.getBet(), 20);

    }

}
