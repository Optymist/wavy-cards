package blackjack.actions;

import blackjack.Play;
import blackjack.player.Player;

public class DoubleAction extends BlackJackAction {
    public DoubleAction() {
        super("double");
    }

    @Override
    public void execute(Player player, Play game) {
        player.doubleBet();
        game.dealCardToPlayer(player);
        player.calculateCards();
        player.setStanding(true);
        System.out.println(player.getName() + " doubles.");
        player.getPlayerManager().sendMessage(player.toString());
    }
}
