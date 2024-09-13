package blackjack.player.state;

import blackjack.actions.BlackJackAction;
import blackjack.deck.Card;

import java.util.List;

public interface playerState {
    void doRound();
    List<BlackJackAction> getActions(List<Card> hand);
}
