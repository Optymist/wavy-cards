package blackjack.actions;

import blackjack.Play;
import blackjack.player.Player;

public class HitAction extends BlackJackAction {
    public HitAction() {
        super("hit");
    }

    @Override
    public void execute(Player player, Play game) {
        game.dealCardToPlayer(player);
        System.out.println(player.getName() + " hits.");
        player.getPlayerManager().sendMessage(player.toString());
    }
}
