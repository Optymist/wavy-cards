package blackjack.actions;

import blackjack.Play;
import blackjack.player.Player;
import blackjack.player.state.Surrender;

public class SurrenderAction extends BlackJackAction {
    public SurrenderAction() {
        super("surrender");
    }

    @Override
    public void execute(Player player, Play game) {
        player.surrender();
        player.setState(new Surrender());
        System.out.println(player.getName() + " surrenders.");
    }

    @Override
    public String toString() {
        return getActionName();
    }
}
