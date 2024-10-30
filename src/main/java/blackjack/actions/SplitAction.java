package blackjack.actions;

import blackjack.Play;
import blackjack.player.Hand;
import blackjack.player.Player;
import blackjack.protocol.GenerateJson;

/**
 * SplitAction class
 */
public class SplitAction extends BlackJackAction {
    public SplitAction() {
        super("split");
    }

    /**
     * Calls the method to split the player's hand and sends output to the client.
     * @param playingHand --> hand being played
     * @param player --> player who's choosing the action
     * @param game --> game being played
     */
    @Override
    public void execute(Hand playingHand, Player player, Play game) {
        Hand splitPlayerHand = player.splitHand(game);
        System.out.println(player.getName() + " splits.");
        player.getPlayerManager().sendMessage(GenerateJson.generateGeneralMessage(player.getCardsInHand().toString()));
        player.getPlayerManager().sendMessage(GenerateJson.generateGeneralMessage(splitPlayerHand.toString()));
    }

    @Override
    public String toString() {
        return getActionName();
    }
}
