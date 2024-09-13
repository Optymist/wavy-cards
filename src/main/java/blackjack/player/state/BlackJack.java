package blackjack.player.state;

import blackjack.actions.BlackJackAction;
import blackjack.player.Hand;

import java.util.List;

public class BlackJack implements playerState {
    @Override
    public void doRound() {
        return;
    }

    @Override
    public List<BlackJackAction> getActions(Hand hand) {
        return List.of();
    }
}
