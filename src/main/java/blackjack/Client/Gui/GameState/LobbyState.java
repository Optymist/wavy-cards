package blackjack.Client.Gui.GameState;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Map.Entry;

import javax.swing.text.html.HTMLDocument.Iterator;

import com.fasterxml.jackson.databind.JsonNode;

import blackjack.Client.Gui.Panels.MenuPanel;

/**
 * LobbyState
 */
public class LobbyState extends GameState {

    private ArrayList<String> playerNames;

    public LobbyState(JsonNode playersNode) {
        super();
        playerNames = new ArrayList<>();
        getPlayersFromJson(playersNode);
    }

    private void getPlayersFromJson(JsonNode playersNode) {
        playersNode.fields().forEachRemaining(each -> playerNames.add(each.getValue().asText()));
    }

	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'draw'");
	}

    public ArrayList<String> getPlayerNames() {
        return playerNames;
    }

	@Override
	public void initComponents(MenuPanel panel) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'initComponents'");
	}
    
}
