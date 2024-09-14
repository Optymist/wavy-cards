// todo --> Convert all game state to json
// nothing but static methods that shit out the json string
package blackjack.protocol;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import blackjack.Play;
import blackjack.actions.BlackJackAction;
import blackjack.player.Player;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * GenerateJson
 */
public class GenerateJson {

    public static String generateTurnRequest(Play game, Player player) {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode rootNode = new ObjectNode(factory);
        List<BlackJackAction> actions = player.getState().getActions(player.getCardsInHand());
        ArrayNode actionArrayNode = new ArrayNode(factory);
        for (BlackJackAction blackJackAction : actions) {
            actionArrayNode.add(blackJackAction.toString());
            // System.out.println(blackJackAction.toString());
        }
        rootNode.put("protocolType", "turnRequest");
        rootNode.set("actions", actionArrayNode);
        return rootNode.toString();
    }
}
