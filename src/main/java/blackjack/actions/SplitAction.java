package blackjack.actions;

import blackjack.Play;
import blackjack.player.Player;

public class SplitAction extends BlackJackAction {
    public SplitAction() {
        super("split");
    }

    @Override
    public void execute(Player player, Play game) {
            player.splitHand();
            System.out.println(player.getName() + " splits.");
            // todo --> find way to print the split decks
//            player.getPlayerManager().sendMessage(player.getCardsInHand().toString());
    }

    @Override
    public String toString() {
        return getActionName();
    }
}
