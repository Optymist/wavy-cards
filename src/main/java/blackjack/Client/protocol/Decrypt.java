package blackjack.Client.protocol;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Decrypt class provides utility methods to parse and process server messages
 * in a Blackjack game client.
 */
public class Decrypt {
    /**
     * Decrypts a server message and converts it into a JsonNode object.
     *
     * @param serverMessage the raw message received from the server in JSON format.
     * @return a JsonNode representing the server message structure.
     * @throws JsonProcessingException if the message cannot be parsed as valid JSON.
     */
    public static JsonNode decryptServerMessage(String serverMessage) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode messageNode = mapper.readTree(serverMessage);

        return messageNode;
    }

    /**
     * Retrieves the available actions from a protocol node and formats them as a string.
     *
     * @param protocolNode the JSON node representing the server's protocol response.
     * @return a string listing all available actions.
     */
    public static String getAvailableActions(JsonNode protocolNode) {
        JsonNode actionsNode = protocolNode.get("actions");
        String actionMessage = "Available actions:\n";

        for (JsonNode action : actionsNode) {
            actionMessage += action.asText() + "\n";
        }

        return actionMessage;
    }

    /**
     * Extracts the name of the player whose turn it currently is.
     *
     * @param protocolNode the JSON node representing the server's protocol response.
     * @return a string indicating the current player's turn.
     */
    public static String getTurn(JsonNode protocolNode) {
        String currentPlayer = protocolNode.get("currentPlayer").asText();
        return currentPlayer + "'s turn.";
    }

    /**
     * Retrieves the hand of a specific player as a string.
     *
     * @param protocolNode the JSON node representing the server's protocol response.
     * @param name         the name of the player whose hand information is requested.
     * @return a string representation of the player's cards.
     */
    public static String getPlayerCardInfo(JsonNode protocolNode, String name) {
        return protocolNode.get("players").get(name).get("hand").toString();
    }

}