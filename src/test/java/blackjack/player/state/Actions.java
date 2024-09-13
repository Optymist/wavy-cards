package blackjack.player.state;

import static org.junit.jupiter.api.Assertions.*;

import blackjack.actions.BlackJackAction;
import blackjack.player.Hand;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class Actions {

    @Test
    public void testBust() {
        playerState state = new Bust();
        List<BlackJackAction> actions = state.getActions(new Hand());
        assertEquals(0, actions.size());
    }

    @Test
    public void testBlackJack() {
        playerState state = new BlackJack();
        List<BlackJackAction> actions = state.getActions(new Hand());
        assertEquals(0, actions.size());
    }



}
