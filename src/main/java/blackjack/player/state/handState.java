package blackjack.player.state;

import blackjack.actions.BlackJackAction;
import blackjack.deck.Card;
import blackjack.player.Hand;

import java.util.List;

public interface handState {
    void doRound();

    List<BlackJackAction> getActions(Hand hand);
}
