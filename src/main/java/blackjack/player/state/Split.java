package blackjack.player.state;

import blackjack.Play;
import blackjack.actions.BlackJackAction;
import blackjack.player.Hand;
import blackjack.player.Player;
import blackjack.protocol.GenerateJson;

import java.util.ArrayList;
import java.util.List;

public class Split extends Normal{
    @Override
    public void doRound(Player player, Play game) {

    }

    @Override
    public List<BlackJackAction> getActions(Hand hand) {
        // TODO
        return List.of();
    }


    @Override
    public String toString() {
        return "split";
    }
}
