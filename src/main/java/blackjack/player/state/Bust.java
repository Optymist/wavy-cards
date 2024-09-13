package blackjack.player.state;

import blackjack.actions.BlackJackAction;
import blackjack.deck.Card;

import java.util.List;

public class Bust implements playerState {
    @Override
    public void doRound() {

    }

    @Override
    public List<BlackJackAction> getActions(List<Card> hand) {
        return List.of();
    }
}
