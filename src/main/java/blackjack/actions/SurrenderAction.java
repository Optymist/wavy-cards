package blackjack.actions;

import blackjack.Play;
import blackjack.player.Player;
import blackjack.player.state.Surrender;
import blackjack.protocol.GenerateJson;

public class SurrenderAction extends BlackJackAction {
    public SurrenderAction() {
        super("surrender");
    }

    @Override
    public void execute(Player player, Play game) {
        player.surrender();
        player.setState(new Surrender());
        System.out.println(player.getName() + " surrenders.");
        player.getPlayerManager().sendMessage(GenerateJson.generateGeneralMessage(player.toString()));
    }

    @Override
    public String toString() {
        return getActionName();
    }
}
