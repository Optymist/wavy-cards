package blackjack.actions;

import blackjack.Play;
import blackjack.player.Hand;
import blackjack.player.Player;
import blackjack.player.state.Surrender;

public class SurrenderAction extends BlackJackAction {
    public SurrenderAction() {
        super("surrender");
    }

    @Override
    public void execute(Hand playingHand, Player player, Play game) {
        player.surrender();
        playingHand.setState(new Surrender());
        System.out.println(player.getName() + " surrenders.");
    }

    @Override
    public String toString() {
        return getActionName();
    }
}
