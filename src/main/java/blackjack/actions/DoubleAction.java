package blackjack.actions;

import blackjack.Play;
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
        playingHand.addCard(game.getDeck().deal());
        System.out.println(player.getName() + " doubles.");
        // TODO idk what to do, you can choose \/
        player.getPlayerManager().sendMessage(GenerateJson.generateGeneralMessage(player.toString()));

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
