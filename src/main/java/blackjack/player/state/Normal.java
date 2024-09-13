package blackjack.player.state;

import blackjack.actions.BlackJackAction;
import blackjack.actions.*;
import blackjack.deck.Card;

import java.util.ArrayList;
import java.util.List;

public class Normal implements playerState {
    @Override
    public void doRound() {

    }

    @Override
    public List<BlackJackAction> getActions(List<Card> hand) {
        List<BlackJackAction> availableActions =
                new ArrayList<>(List.of(new DoubleAction(), new HitAction(), new StandAction(), new SurrenderAction()));

        if (canSplit(hand)) {
            availableActions.add(new SplitAction());
        }
        return null;
    }

    public boolean canSplit(List<Card> cardsInHand) {
        Card one = cardsInHand.get(0);
        Card two = cardsInHand.get(1);
        boolean aces = one.toString().contains("A") && two.toString().contains("A");
        return cardsInHand.size() == 2 && (one.rankValue(handValue) == two.rankValue(handValue) || aces);
    }
}
