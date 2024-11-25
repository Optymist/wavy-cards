package blackjack.Client.Gui.GameState;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * PlayerState
 */
public class PlayerState {

    private String name;
    private String state;
    private String[] hand;
    private int money;
    private int bet;

    public PlayerState(JsonNode playerNode) {
        this.name = playerNode.get("name").asText();
        this.state = playerNode.get("state").asText();
        this.hand = getHand(playerNode.get("hand"));
        this.money = getMoney(playerNode.get("money"));
        this.bet = getBet(playerNode.get("bet"));
    }

    private String[] getHand(JsonNode playerHand) {
        ArrayNode handNode = (ArrayNode) playerHand;
        ArrayList<String> handList = new ArrayList<>();

        handNode.forEach(each -> handList.add(each.asText()));

        String[] handArray = new String[handList.size()];
        for (int i = 0; i < handArray.length; i++) {
            handArray[i] = handList.get(i);
        }

        return handArray;
    }

    private int getMoney(JsonNode moneyNode) {
        return moneyNode.asInt();
    }

    private int getBet(JsonNode betNode) {
        return betNode.asInt();
    }

    public String getName() {
        return this.name;
    }

    public String getState() {
        return this.state;
    }

    public String[] getHand() {
        return this.hand;
    }

    public int getMoney() {
        return this.money;
    }

    public int getBet() {
        return this.bet;
    }
}
