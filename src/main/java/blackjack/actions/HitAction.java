package blackjack.actions;

import blackjack.Play;
import blackjack.player.Player;
import blackjack.protocol.GenerateJson;

public class HitAction extends BlackJackAction {
    public HitAction() {
        super("hit");
    }

    @Override
    public void execute(Player player, Play game) {
        player.addCardToHand(game.getDeck().deal());
        System.out.println(player.getName() + " hits.");
        player.getPlayerManager().sendMessage(GenerateJson.generateGeneralMessage(player.toString()));
    }

    @Override
    public String toString() {
        return getActionName();
    }
}
