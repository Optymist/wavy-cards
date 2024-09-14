// todo --> Convert all game state to json
// nothing but static methods that shit out the json string
package blackjack.protocol;

import blackjack.deck.Card;
import blackjack.player.Dealer;
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

    public static String generateTurnRequest(Player player) {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode rootNode = new ObjectNode(factory);
        List<BlackJackAction> actions = player.getState().getActions(player.getCardsInHand());
        ArrayNode actionArrayNode = new ArrayNode(factory);
        for (BlackJackAction blackJackAction : actions) {
            actionArrayNode.add(blackJackAction.toString());
        }
        rootNode.put("protocolType", "turnRequest");
        rootNode.put("currentPlayer", player.getName());
        rootNode.set("actions", actionArrayNode);
        return rootNode.toString();
    }

    public static String generateUpdateRequest(Play game) {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode rootNode = new ObjectNode(factory);
        List<Player> playersInGame = game.getPlayers();
        ArrayNode playerArrayNode = new ArrayNode(factory);
        for (Player player : playersInGame) {
            playerArrayNode.add(playerInformation(player));
        }
        rootNode.put("protocolType", "update");
        rootNode.put("currentPlayer", game.getCurrentPlayer().getName());
        rootNode.set("players", playerArrayNode);
        rootNode.put("dealer", dealerInformation(game));

        return rootNode.toString();
    }

    private static String playerInformation(Player player) {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode rootNode = new ObjectNode(factory);
        List<Card> cardList = player.getCardsInHand().getCards();
        ArrayNode cardArrayNode = new ArrayNode(factory);
        for (Card card : cardList) {
            cardArrayNode.add(card.toString());
        }
        rootNode.put("name", player.getName());
        rootNode.put("state", player.getState().toString());
        rootNode.set("hand", cardArrayNode);
        rootNode.put("handValue", player.getHandValue());
        rootNode.put("money", player.getMoney());
        rootNode.put("bet", player.getBet());

        return rootNode.toString();
    }

    private static String dealerInformation(Play game) {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode rootNode = new ObjectNode(factory);
        Dealer dealer = game.getDealer();
        List<Card> cardList = dealer.getCardsInHand().getCards();
        ArrayNode cardArrayNode = new ArrayNode(factory);
        for (Card card : cardList) {
            cardArrayNode.add(card.toString());
        }
        rootNode.put("state", dealer.getState().toString());
        rootNode.set("hand", cardArrayNode);
        rootNode.put("handValue", dealer.getHandValue());

        return rootNode.toString();
    }


}
