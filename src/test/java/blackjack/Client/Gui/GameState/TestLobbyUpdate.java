package blackjack.Client.Gui.GameState;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import blackjack.Client.Exceptions.InvalidGameStateException;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * TestLobbyUpdate
 */
public class TestLobbyUpdate {

    private String lobbyUpdate = "{\"protocolType\":\"lobbyUpdate\",\"players\":{\"1\":\"bob\",\"2\":\"bill\"}}"; 
    private JsonNode lobbyNode;


    @BeforeEach
    public void setUpTests() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            lobbyNode = mapper.readTree(lobbyUpdate);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDecryptDoesNotThrow() {
        assertDoesNotThrow(() -> generateLobby());
    }

	private void generateLobby() throws InvalidGameStateException {
		GameState updateState;
		updateState = GameState.decrypt(lobbyNode);
	}

    @Test
    public void testGenerateLobbyUpdate() {
        try {
            GameState state =  GameState.decrypt(lobbyNode);
            assertEquals(state.getClass(), LobbyState.class);
		} catch (InvalidGameStateException e) {
		}
    }

    @Test
    public void testPlayerListLength() {
        try {
            LobbyState state = (LobbyState) GameState.decrypt(lobbyNode);
            assertEquals(state.getPlayerNames().size(), 2);
        } catch (InvalidGameStateException e) {
        }
    }
    
    @Test
    public void testPlayerListItems() {
        try {
            LobbyState state = (LobbyState) GameState.decrypt(lobbyNode);
            ArrayList<String> players = state.getPlayerNames();
            assertTrue(players.contains("bob"));
            assertTrue(players.contains("bill"));
        } catch (InvalidGameStateException e) {
        }
    }



}
