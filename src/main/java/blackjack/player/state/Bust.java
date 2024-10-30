package blackjack.player.state;

import blackjack.Play;
import blackjack.actions.BlackJackAction;
import blackjack.player.Hand;
import blackjack.player.Player;

import java.util.List;

public class Bust implements handState {
    @Override
    public void doRound(Player player, Play game) {

    }

    @Override
    public List<BlackJackAction> getActions(Hand hand) {
        return List.of();
    }

    @Override
    public String toString() {
        return "bust";
    }
}
