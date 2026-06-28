package blackjack.Client.protocol;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Iterator;
import java.util.Map;

public class Decrypt {

    public static JsonNode decryptServerMessage(String serverMessage) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(serverMessage);
    }

    public static String getAvailableActions(JsonNode protocolNode) {
        JsonNode actionsNode = protocolNode.get("actions");
        StringBuilder sb = new StringBuilder();
        sb.append("\n*** YOUR TURN ***\n");
        sb.append("Actions: ");
        boolean first = true;
        for (JsonNode action : actionsNode) {
            if (!first) sb.append(", ");
            sb.append(action.asText());
            first = false;
        }
        sb.append("\nEnter action: ");
        return sb.toString();
    }

    public static String getPlayerCardInfo(JsonNode protocolNode, String myName) {
        StringBuilder sb = new StringBuilder();
        String currentPlayer = protocolNode.get("currentPlayer").asText();

        sb.append("\n─────────────────────────────────────\n");

        JsonNode dealer = protocolNode.get("dealer");
        String dealerHand = formatHand(dealer.get("hand"));
        int dealerValue = dealer.get("handValue").asInt();
        sb.append(String.format("  Dealer: %-20s [%d]%n", dealerHand, dealerValue));

        sb.append("  ─────────────────────────────────\n");

        Iterator<Map.Entry<String, JsonNode>> players = protocolNode.get("players").fields();
        while (players.hasNext()) {
            Map.Entry<String, JsonNode> entry = players.next();
            String pName = entry.getKey();
            JsonNode p = entry.getValue();
            String hand = formatHand(p.get("hand"));
            int value = p.get("handValue").asInt();
            int money = (int) p.get("money").asDouble();
            int bet = (int) p.get("bet").asDouble();
            String state = p.get("state").asText();

            String label = pName.equals(myName) ? pName + " (you)" : pName;
            String turnMark = pName.equals(currentPlayer) ? " <" : "  ";
            String stateMark = state.equals("normal") ? "" : " [" + state.toUpperCase() + "]";
            sb.append(String.format("  %-18s %s  %-18s [%d]  $%d  bet: $%d%s%n",
                    label, turnMark, hand, value, money, bet, stateMark));
        }

        sb.append("─────────────────────────────────────");
        return sb.toString();
    }

    public static String getTurn(JsonNode protocolNode) {
        return protocolNode.get("currentPlayer").asText() + "'s turn.";
    }

    private static String formatHand(JsonNode handNode) {
        StringBuilder sb = new StringBuilder();
        for (JsonNode card : handNode) {
            if (sb.length() > 0) sb.append("  ");
            sb.append(card.asText());
        }
        return sb.toString();
    }
}