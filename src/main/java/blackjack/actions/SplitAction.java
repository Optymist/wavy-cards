package blackjack.actions;

import blackjack.Play;
import blackjack.player.Hand;
import blackjack.player.Player;
import blackjack.player.state.Split;
import blackjack.protocol.GenerateJson;

import java.util.ArrayList;
import java.util.List;

public class SplitAction extends BlackJackAction {
    public SplitAction() {
        super("split");
    }

    @Override
    public void execute(Hand playingHand, Player player, Play game) {
        // playingHand.setState(new Split());
        Hand splitPlayerHand = player.splitHand(game);
//        Hand otherHand = player.getCardsInHand();
        System.out.println(player.getName() + " splits.");
        player.getPlayerManager().sendMessage(GenerateJson.generateGeneralMessage(player.getCardsInHand().toString()));
        player.getPlayerManager().sendMessage(GenerateJson.generateGeneralMessage(splitPlayerHand.toString()));

//        handleSplitPlay(splitPlayerHand, otherHand, player, game);
//        player.getPlayerManager().sendMessage("Playing on this hand first: ");
//        player.getPlayerManager().sendMessage(GenerateJson.generateGeneralMessage(player.getCardsInHand().toString()));

    }



    @Override
    public String toString() {
        return getActionName();
    }
}
