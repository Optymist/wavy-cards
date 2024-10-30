package blackjack.player.state;

import blackjack.Play;
import blackjack.actions.BlackJackAction;
import blackjack.deck.Card;
import blackjack.player.Hand;
import blackjack.player.Player;

import java.util.List;

public interface handState {
    void doRound(Player player, Play game);

    List<BlackJackAction> getActions(Hand hand);
}
