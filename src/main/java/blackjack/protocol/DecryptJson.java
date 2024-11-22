package blackjack.protocol;

import blackjack.actions.BlackJackAction;
import blackjack.protocol.Exceptions.InvalidAction;
import blackjack.protocol.Exceptions.InvalidBet;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * DecryptJson - get the required information from a json string
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

    /**
     * This method takes a client's betResponse and gets the bet.
     * It compares it to money to determine whether it is valid.
     *
     * @param money --> the amount of money the player has.
     * @param betResponse --> the response string that the client sent.
     * @return int bet value.
     * @throws InvalidBet if the bet is zero or greater than the client's remaining money.
     * @throws JsonProcessingException if it cannot convert the string into a json node.
     */
    public static int getBet(double money, String betResponse) throws InvalidBet, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(betResponse);
        int bet = rootNode.get("bet").asInt();

        if (bet == 0.0 || bet > money) {
            throw new InvalidBet("Not a valid bet.");
        } else {
            return bet;
        }
    }

    /**
     * Validates whether an update is a connected updated
     *
     * @param update -> The response from the server
     * @return True if a connectedUpdate, False if not
     * @throws JsonProcessingException 
     * @throws JsonMappingException 
     */
    public static boolean isConnectedToGame(String update) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(update);
        String protocol = rootNode.get("protocolType").asText();
        return (protocol.equals("connectedUpdate"));
    }
}
