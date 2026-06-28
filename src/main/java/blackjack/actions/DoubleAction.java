package blackjack.actions;

import blackjack.Play;
import blackjack.deck.Card;
import blackjack.player.Hand;
import blackjack.player.Player;
import blackjack.player.state.Bust;
import blackjack.player.state.Stand;
import blackjack.protocol.GenerateJson;

public class DoubleAction extends BlackJackAction {
    public DoubleAction() {
        super("double");
    }

    @Override
    public void execute(Hand playingHand, Player player, Play game) {
        player.doubleBet();
        Card drawn = game.getDeck().deal();
        playingHand.setState(playingHand.addCard(drawn));
        System.out.println(player.getName() + " doubles.");
        player.getPlayerManager().sendMessage(GenerateJson.generateGeneralMessage(
            "Doubled! Drew " + drawn + "  →  " + formatHand(playingHand) + "  [" + playingHand.getValue() + "]"));

        switch (playingHand.getState().toString()) {
            case "normal":
                playingHand.setState(new Stand());
                break;
            default:
                playingHand.setState(new Bust());
        }
    }

    @Override
    public String toString() {
        return getActionName();
    }
}
