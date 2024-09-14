package blackjack.player.state;

import blackjack.actions.BlackJackAction;
import blackjack.player.Hand;

import java.util.List;

public class Bust implements playerState {
    @Override
    public void doRound() {

    }

    @Override
    public List<BlackJackAction> getActions(Hand hand) {
        return List.of();
    }
}
