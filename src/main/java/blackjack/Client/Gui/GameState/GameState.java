package blackjack.Client.Gui.GameState;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import blackjack.Client.Gui.GameState.UpdateState;
import blackjack.Client.Exceptions.InvalidGameStateException;

/**
 * GameState
 */
public abstract class GameState {

    public static GameState decrypt(JsonNode serverUpdate) throws InvalidGameStateException {

        switch (serverUpdate.get("protocolType").asText()) {
            case ("betRequest"):
                return new BetRequestState(serverUpdate.get("message").asText());

            case ("turnRequest"):
                String[] actions = new String[] {};
                actions = getActions(serverUpdate.get("actions"));

                return new TurnRequestState(actions);

            case ("update"):
                return new UpdateState(serverUpdate.get("players"));

            case ("lobbyUpdate"):
                return new LobbyState(serverUpdate.get("players"));

            case ("connectedUpdate"):
            case ("general"):
            default:
                throw new InvalidGameStateException(serverUpdate.asText());
        }

    }

    public abstract void draw(Graphics g);

    private static String[] getActions(JsonNode protocolNode) {
        JsonNode actionsNode = protocolNode.get("actions");
        ArrayList<String> actions = new ArrayList<String>();

        for (JsonNode action : actionsNode) {
            actions.add(action.asText());
        }

        return (String[]) actions.toArray();
    }
}
