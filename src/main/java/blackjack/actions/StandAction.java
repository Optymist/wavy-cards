package blackjack.actions;

import blackjack.Play;
import blackjack.player.Player;

public class StandAction extends BlackJackAction {
    public StandAction() {
        super("stand");
    }

    @Override
    public void execute(Player player, Play game) {
        System.out.println(player.getName() + " stands.");
        player.getPlayerManager().sendMessage(player.toString());
        player.setStanding(true);
    }
}
