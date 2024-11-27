package blackjack.Client.Gui.GameState;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import blackjack.Client.Exceptions.InvalidGameStateException;

/**
 * UpdateState
 */
public class TestUpdateState {

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
    public void testUpdateStateDoesNotThrowError() {
        assertDoesNotThrow(() -> extracted());
    }

	private void extracted() throws InvalidGameStateException {
		GameState updateState;
		updateState = GameState.decrypt(messageNode);
	}

    @Test
    public void testDecryptGeneratesUpdateState() {
        try {
			GameState state = GameState.decrypt(messageNode);
            assertTrue(state.getClass().equals(UpdateState.class));
		} catch (InvalidGameStateException e) {
		}
    }

    @Test
    public void testPlayersListSize() {
        try {
			UpdateState state = (UpdateState) GameState.decrypt(messageNode);
            assertEquals(state.getPlayerStates().size(), 2);
		} catch (InvalidGameStateException e) {
		}
    }

    @Test
    public void testPlayerOneState() {
        try {
			UpdateState state = (UpdateState) GameState.decrypt(messageNode);
            PlayerState player = state.getPlayerStates().get(0);
            assertTrue(player.getName().equals("Sal"));
            assertTrue(player.getState().equals("normal"));
            assertArrayEquals(player.getHand(), new String[]{"J♣","3♣"});
            assertEquals(player.getMoney(), 2500);
            assertEquals(player.getBet(), 20);
		} catch (InvalidGameStateException e) {
		}
    }


    @Test
    public void testPlayerTwoState() {
        try {
			UpdateState state = (UpdateState) GameState.decrypt(messageNode);
            PlayerState player = state.getPlayerStates().get(1);
            assertTrue(player.getName().equals("hal"));
            assertTrue(player.getState().equals("normal"));
            assertArrayEquals(player.getHand(), new String[]{"4♠","8♠"});
            assertEquals(player.getMoney(), 2500);
            assertEquals(player.getBet(), 10);
		} catch (InvalidGameStateException e) {
		}
    }



}
