package blackjack.Client.protocol;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Generate {
    public static String generateTurnResponse(String name, String action) {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode rootNode = new ObjectNode(factory);

        rootNode.put("protocolType", "turnResponse");
        rootNode.put("playerName", name);
        rootNode.put("action", action);

        return rootNode.toString();
    }

}