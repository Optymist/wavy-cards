package blackjack.actions;

import blackjack.Play;
import blackjack.player.Hand;
import blackjack.player.Player;
import blackjack.player.state.Surrender;
import blackjack.protocol.GenerateJson;

public class SurrenderAction extends BlackJackAction {
    public SurrenderAction() {
        super("surrender");
    }

    @Override
    public void execute(Hand playingHand, Player player, Play game) {
        player.surrender();
        playingHand.setState(new Surrender());
        System.out.println(player.getName() + " surrenders.");
        player.getPlayerManager().sendMessage(GenerateJson.generateGeneralMessage(player.toString()));
    }

    @Override
    public String toString() {
        return getActionName();
    }
}
