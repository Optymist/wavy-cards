package blackjack.protocol;

import blackjack.PlayerManager;
import blackjack.deck.Card;
import blackjack.player.Dealer;
import blackjack.player.Hand;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import blackjack.Play;
import blackjack.actions.BlackJackAction;
import blackjack.player.Player;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * Class that converts messages from the server into json format
 */
public class GenerateJson {
    /**
     * Generates a general message to send to the user.
     * @param message --> to send
     * @return the string json form
     */
    public static String generateGeneralMessage(String message) {
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        ObjectNode rootNode = nodeFactory.objectNode();

        rootNode.put("protocolType", "general");
        rootNode.put("message", message);

        return rootNode.toString();
    }

    /**
     * Generates a connection update to send to the user.
     * @param player --> that has been connected.
     * @return the string json form.
     */
    public static String generateConnectedUpdate(Player player) {
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        ObjectNode rootNode = nodeFactory.objectNode();
        String message = "Welcome " + player.getName() + "!";

        rootNode.put("protocolType", "connectedUpdate");
        rootNode.put("message", message);
        rootNode.put("playerName", player.getName());
        rootNode.put("money", player.getMoney());

        return rootNode.toString();
    }

    /**
     * Generates a turn turnRequest to send to the user.
     * @param player --> to whom the request is being sent.
     * @param currentHand --> the hand that is being played.
     * @return the string json form.
     */
    public static String generateTurnRequest(Player player, Hand currentHand) {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode rootNode = new ObjectNode(factory);
        List<BlackJackAction> actions = currentHand.getState().getActions(currentHand);
        ArrayNode actionArrayNode = new ArrayNode(factory);
        for (BlackJackAction blackJackAction : actions) {
            actionArrayNode.add(blackJackAction.toString());
        }
        rootNode.put("protocolType", "turnRequest");
        rootNode.put("currentPlayer", player.getName());
        rootNode.set("actions", actionArrayNode);

        return rootNode.toString();
    }

    /**
     * Generates a betRequest to send to the user.
     * @param name --> of the player who is choosing a bet.
     * @param message --> the message to add.
     * @return the string json form.
     */
    public static String generateBetRequest(String name, String message) {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode rootNode = new ObjectNode(factory);
        rootNode.put("protocolType", "betRequest");
        rootNode.put("playerName", name);
        rootNode.put("message", message);

        return rootNode.toString();
    }

    /**
     * Generates an update of the entire state of the game.
     * @param game --> the game to generate an update of.
     * @return the string json form
     */
    public static String generateUpdate(Play game) {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode rootNode = new ObjectNode(factory);
        List<Player> playersInGame = game.getPlayers();
        ObjectNode playerObjectNode = new ObjectNode(factory);
        for (Player player : playersInGame) {
            playerObjectNode.set(player.getName(), playerInformation(player));
        }

        rootNode.put("protocolType", "update");
        rootNode.put("currentPlayer", game.getCurrentPlayer().getName());
        rootNode.set("players", playerObjectNode);
        rootNode.set("dealer", dealerInformation(game));

        return rootNode.toString();
    }

    /**
     * Generates lobby update for the gui client to use
     * @param players --> list of playerHandlers
     * @return lobby update in string json form
     */
    public static String generateLobbyUpdate(ArrayList<PlayerManager> players) {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode rootNode = new ObjectNode(factory);
        ObjectNode playerNamesNode = new ObjectNode(factory);
        int counter = 1;
        for (PlayerManager player : players) {
            playerNamesNode.put(String.valueOf(counter), player.getName());
            counter++;
        }

        rootNode.put("protocolType", "lobbyUpdate");
        rootNode.set("players", playerNamesNode);

        return rootNode.toString();
    }

    /**
     * Generate an ObjectNode with all the information of a player's state.
     * @param player --> to generate an objectNode of.
     * @return the object node.
     */
    private static ObjectNode playerInformation(Player player) {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode rootNode = new ObjectNode(factory);
        List<Card> cardList = player.getCardsInHand().getCards();
        ArrayNode cardArrayNode = new ArrayNode(factory);
        for (Card card : cardList) {
            cardArrayNode.add(card.toString());
        }
        rootNode.put("name", player.getName());
        rootNode.put("state", player.getCardsInHand().getState().toString());
        rootNode.set("hand", cardArrayNode);
        rootNode.put("handValue", player.getHandValue());
        rootNode.put("money", player.getMoney());
        rootNode.put("bet", player.getBet());

        return rootNode;
    }

    /**
     * Generate an ObjectNode that represents the dealer's current state in the game.
     * @param game --> the game that the dealer is currently in.
     * @return the object node.
     */
    private static ObjectNode dealerInformation(Play game) {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode rootNode = new ObjectNode(factory);
        Dealer dealer = game.getDealer();
        List<Card> cardList = dealer.getCardsInHand().getCards();
        ArrayNode cardArrayNode = new ArrayNode(factory);
        for (Card card : cardList) {
            cardArrayNode.add(card.toString());
        }
        rootNode.put("state", dealer.getCardsInHand().getState().toString());
        rootNode.set("hand", cardArrayNode);
        rootNode.put("handValue", dealer.getHandValue());

        return rootNode;
    }


}
