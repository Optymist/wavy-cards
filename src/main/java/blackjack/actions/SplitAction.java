package blackjack.actions;

import blackjack.Play;
import blackjack.player.Player;
import blackjack.player.state.Split;
import blackjack.protocol.GenerateJson;

public class SplitAction extends BlackJackAction {
    public SplitAction() {
        super("split");
    }

    @Override
    public void execute(Player player, Play game) {
        player.setState(new Split());
        Player splitPlayer = player.splitHand(game);
        System.out.println(player.getName() + " splits.");
        player.getPlayerManager().sendMessage(GenerateJson.generateGeneralMessage(player.toString()));
        player.getPlayerManager().sendMessage(GenerateJson.generateGeneralMessage(splitPlayer.toString()));
    }

    @Override
    public String toString() {
        return getActionName();
    }
}
