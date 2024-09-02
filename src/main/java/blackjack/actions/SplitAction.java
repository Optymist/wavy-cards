package blackjack.actions;

import blackjack.Play;
import blackjack.player.Player;

public class SplitAction extends BlackJackAction {
    public SplitAction() {
        super("split");
    }

    @Override
    public void execute(Player player, Play game) {
        if (player.canSplit()) {
            player.splitHand();
            System.out.println(player.getName() + " splits.");
            // todo --> find way to print the split decks
//            player.getPlayerManager().sendMessage(player.getCardsInHand().toString());
        } else {
            System.out.println(player.getName() + " cannot split.");
            player.getPlayerManager().sendMessage(player.toString());
        }
    }
}
