package blackjack;

import blackjack.deck.Deck;

import blackjack.helperClasses.mockedPlayerManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * PlayTest
 */
public class PlayTest {
    private PlayerManager mockPlayerManager;

    @BeforeEach
    public void setUp() {
        mockedPlayerManager.setUp();
        this.mockPlayerManager = mockedPlayerManager.mockPlayerManager;
    }

    @Test
    public void testStartGameWithCorrectAmountOfDecks() {
        Play game = new Play(2);
        Deck gameDeck = game.getDeck();
        assertEquals(52, gameDeck.getPlayDeck().size());

        Play game2 = new Play(5);
        Deck game2Deck = game2.getDeck();
        assertEquals(104, game2Deck.getPlayDeck().size());
    }





}
