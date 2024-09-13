package blackjack.player.state;

import blackjack.actions.BlackJackAction;
import blackjack.actions.*;
import blackjack.deck.Card;
import blackjack.player.Hand;

import java.util.ArrayList;
import java.util.List;

public class Normal implements playerState {
    @Override
    public void doRound() {

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
        Card one = cards.get(0);
        Card two = cards.get(1);
        boolean aces = one.toString().contains("A") && two.toString().contains("A");
        return cards.size() == 2 &&
                (one.rankValue(cardsInHand.getValue()) == two.rankValue(cardsInHand.getValue()) || aces);
    }
}
