package blackjack.Client.Gui.GameState;

import java.awt.Graphics;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * BetRequestState
 */
public class UpdateState extends GameState {

    private final JsonNode playersNode;
    private final ArrayList<PlayerState> playerStates;

    public UpdateState(JsonNode playersNode) {
        super();
        this.playersNode = playersNode;
        this.playerStates = getPlayerStateList(playersNode);
    }

    private static ArrayList<PlayerState> getPlayerStateList(JsonNode playersNode) {
        ArrayList<PlayerState> players = new ArrayList<>();
        for (JsonNode playerNode : playersNode) {
            players.add(new PlayerState(playerNode));
        }
        return players;
    }



	@Override
	public void draw(Graphics g) {

	}

    public ArrayList<PlayerState> getPlayerStates() {
        return this.playerStates;
    }
    
}
