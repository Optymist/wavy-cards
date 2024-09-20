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
        // todo --> add the split functionality here.
        player.getSplitPlay().setState(new Normal());
        player.getCardsInHand().setState(new Normal());
        Hand splitHand = player.getSplitPlay();
        System.out.println(splitHand);
        Hand ogHand = player.getCardsInHand();
        System.out.println(ogHand);
        handleSplitPlay(splitHand, ogHand, player, game);
    }

    @Override
    public List<BlackJackAction> getActions(Hand hand) {
        return List.of();
    }

    // todo --> allow player to play on both hands... Currently only allowing one
    public void handleSplitPlay(Hand splitHand, Hand playerHand, Player player, Play game) {
        List<Hand> playerHands = new ArrayList<>();
        playerHands.add(playerHand);
        playerHands.add(splitHand);
        for (Hand hand : playerHands) {
            player.getPlayerManager().sendMessage(GenerateJson.generateGeneralMessage("Playing on hand: " + hand));
            player.manageTurn(hand, game);
        }
    }

    @Override
    public String toString() {
        return "split";
    }
}
