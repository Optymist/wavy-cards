package blackjack.actions;

import blackjack.Play;
import blackjack.deck.Card;
import blackjack.helperClasses.mockedPlayerManager;
import blackjack.player.Hand;
import blackjack.player.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class DoubleTests {

    @BeforeEach
    public void setUp() {
        mockedPlayerManager.setUp();
    }

    @Test
    public void doubleSuccessfulTest() {
        Play game = new Play(1);
        Player player = new Player("fern", mockedPlayerManager.mockPlayerManager);
        player.setBet(10);
        Hand hand = player.getCardsInHand();
        hand.addCard(new Card("♥", "2"));
        hand.addCard(new Card("♥", "2"));
        player.performAction("double", game);
        assertEquals(20, player.getBet());
        assertEquals(3, hand.getCards().size());
        assertTrue(player.isStanding());
    }

}
