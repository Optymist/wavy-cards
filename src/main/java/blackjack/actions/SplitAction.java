package blackjack.actions;

import blackjack.Play;
import blackjack.player.Hand;
import blackjack.player.Player;
import blackjack.protocol.GenerateJson;

public class SplitAction extends BlackJackAction {
    public SplitAction() {
        super("split");
    }

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
