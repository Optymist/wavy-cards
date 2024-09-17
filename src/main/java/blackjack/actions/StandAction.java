package blackjack.actions;

import blackjack.Play;
import blackjack.player.Player;
import blackjack.player.state.Normal;
import blackjack.player.state.Stand;
import blackjack.protocol.GenerateJson;

public class StandAction extends BlackJackAction {
    public StandAction() {
        super("stand");
    }

    @Override
    public void execute(Player player, Play game) {
        System.out.println(player.getName() + " stands.");
        player.getPlayerManager().sendMessage(GenerateJson.generateGeneralMessage(player.toString()));
        if (player.getState() instanceof Normal) {
            player.setState(new Stand());
        }
    }

    @Override
    public String toString() {
        return getActionName();
    }
}
