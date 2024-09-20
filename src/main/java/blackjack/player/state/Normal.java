package blackjack.player.state;

import blackjack.Play;
import blackjack.actions.BlackJackAction;
import blackjack.actions.*;
import blackjack.deck.Card;
import blackjack.player.Hand;
import blackjack.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Normal implements handState {
    @Override
    public void doRound(Player player, Play game) {

    }

    @Override
    public List<BlackJackAction> getActions(Hand hand) {
        List<BlackJackAction> availableActions =
                new ArrayList<>(List.of(new DoubleAction(), new HitAction(), new StandAction(), new SurrenderAction()));

        if (canSplit(hand)) {
            availableActions.add(new SplitAction());
        }

        return availableActions;
    }

    public boolean canSplit(Hand cardsInHand) {
        List<Card> cards = cardsInHand.getCards();
        if (cards.size() == 2) {
            Card one = cards.get(0);
            Card two = cards.get(1);

            return one.getValue() == two.getValue();
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "normal";
    }
}
