package blackjack.Client.protocol;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Decrypt {

    public static JsonNode decryptServerMessage(String serverMessage) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode messageNode = mapper.readTree(serverMessage);

        return messageNode;
    }

    public static String getAvailableActions(JsonNode protocolNode) {
        JsonNode actionsNode = protocolNode.get("actions");
        String actionMessage = "Available actions:\n";

        for (JsonNode action : actionsNode) {
            actionMessage += action.toString() + "\n";
        }

        return actionMessage;
    }

    public static String getTurn(JsonNode protocolNode) {
        String currentPlayer = protocolNode.get("currentPlayer").asText();

        return currentPlayer + "'s turn.";
    }

    public static String getPlayerCardInfo(JsonNode protocolNode, String name) {
        String cards = protocolNode.get("players").get(name).get("hand").toString();

        return cards;
    }

}