package blackjack.actions;

import blackjack.Play;
import blackjack.player.Player;

public class SurrenderAction extends BlackJackAction {
    public SurrenderAction() {
        super("surrender");
    }

    @Override
    public void execute(Player player, Play game) {
        player.surrender();
        System.out.println(player.getName() + " surrenders.");
    }

    @Override
    public String toString() {
        return getActionName();
    }
}
