package blackjack.actions;

import blackjack.Play;
import blackjack.deck.Card;
import blackjack.player.Hand;
import blackjack.player.Player;
import blackjack.protocol.GenerateJson;

public class HitAction extends BlackJackAction {
    public HitAction() {
        super("hit");
    }

    @Override
    public void execute(Hand playingHand, Player player, Play game) {
        Card drawn = game.getDeck().deal();
        playingHand.setState(playingHand.addCard(drawn));
        System.out.println(player.getName() + " hits.");
        player.getPlayerManager().sendMessage(GenerateJson.generateGeneralMessage(
            "Drew " + drawn + "  →  " + formatHand(playingHand) + "  [" + playingHand.getValue() + "]"));
    }

    @Override
    public String toString() {
        return getActionName();
    }
}
