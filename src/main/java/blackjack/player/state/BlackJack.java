package blackjack.player.state;

import blackjack.actions.BlackJackAction;
import blackjack.deck.Card;

import java.util.List;

public class BlackJack implements playerState {
    @Override
    public void doRound() {
        return;
    }

    @Override
    public List<BlackJackAction> getActions(List<Card> hand) {
        return List.of();
    }
}
