package blackjack.actions;

import blackjack.Play;
import blackjack.player.Player;
import blackjack.player.state.Bust;
import blackjack.player.state.Normal;
import blackjack.player.state.Stand;

public class DoubleAction extends BlackJackAction {
    public DoubleAction() {
        super("double");
    }

    @Override
    public void execute(Player player, Play game) {
        player.doubleBet();
        player.addCardToHand(game.getDeck().deal());
        System.out.println(player.getName() + " doubles.");
        // TODO idk what to do, you can choose \/
        player.getPlayerManager().sendMessage(player.toString());

        switch (player.getState().toString()) {
            case "normal":
                player.setState(new Stand());
                break;
            default:
                player.setState(new Bust());
        }
    }

    @Override
    public String toString() {
        return getActionName();
    }
}
