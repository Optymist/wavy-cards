package blackjack.protocol;

import blackjack.actions.BlackJackAction;
import blackjack.protocol.Exceptions.InvalidAction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * DecryptJson
 */
public class DecryptJson {

    /**
     * This method takes a client's turnResponse and gets the chosen action.
     *
     * @param turnResponse The response from the client
     * @param actionsAllowed the valid actions available for the player to choose from
     * @return The Chosen BlackJackAction.
     * @throws InvalidAction If a non-valid action was chosen.
     */
    public static BlackJackAction getChosenAction(
            String turnResponse, List<BlackJackAction> actionsAllowed) throws InvalidAction, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(turnResponse);
        String actionName = rootNode.get("action").asText();

        for (BlackJackAction action : actionsAllowed) {
            if (action.toString().equals(actionName)) {
                return BlackJackAction.create(actionName);
            }
        }

        throw new InvalidAction(actionName);
    }

}
