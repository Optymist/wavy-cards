package blackjack.player.state;

import static org.junit.jupiter.api.Assertions.*;

import blackjack.actions.BlackJackAction;
import blackjack.actions.DoubleAction;
import blackjack.actions.HitAction;
import blackjack.actions.SplitAction;
import blackjack.actions.StandAction;
import blackjack.actions.SurrenderAction;
import blackjack.deck.Card;
import blackjack.deck.Deck;
import blackjack.player.Hand;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ActionsTests {

    @Test
    public void testBust() {
        handState state = new Bust();
        Hand hand = new Hand();
        Card card0 = new Card(Deck.SUITS[0], Deck.RANKS[0]);
        Card card1 = new Card(Deck.SUITS[1], Deck.RANKS[1]);
        hand.addCard(card0);
        hand.addCard(card1);

        List<BlackJackAction> actions = state.getActions(hand);
        assertEquals(0, actions.size());
    }

    @Test
    public void testBlackJack() {
        handState state = new BlackJack();
        Hand hand = new Hand();
        Card card0 = new Card(Deck.SUITS[0], Deck.RANKS[0]);
        Card card1 = new Card(Deck.SUITS[1], Deck.RANKS[1]);
        hand.addCard(card0);
        hand.addCard(card1);

        List<BlackJackAction> actions = state.getActions(hand);
        assertEquals(0, actions.size());
    }

    @Test
    public void testStand() {
        handState state = new Stand();
        Hand hand = new Hand();
        Card card0 = new Card(Deck.SUITS[0], Deck.RANKS[0]);
        Card card1 = new Card(Deck.SUITS[1], Deck.RANKS[1]);
        hand.addCard(card0);
        hand.addCard(card1);

        List<BlackJackAction> actions = state.getActions(hand);
        assertEquals(0, actions.size());
    }

    @Test
    public void testSurrender() {
        handState state = new Stand();
        Hand hand = new Hand();
        Card card0 = new Card(Deck.SUITS[0], Deck.RANKS[0]);
        Card card1 = new Card(Deck.SUITS[1], Deck.RANKS[1]);
        hand.addCard(card0);
        hand.addCard(card1);

        List<BlackJackAction> actions = state.getActions(hand);
        assertEquals(0, actions.size());
    }

    @Test
    public void testNormalNoSplit() {
        handState state = new Normal();
        Hand hand = new Hand();
        Card card0 = new Card(Deck.SUITS[0], Deck.RANKS[0]);
        Card card1 = new Card(Deck.SUITS[1], Deck.RANKS[1]);
        hand.addCard(card0);
        hand.addCard(card1);

        List<BlackJackAction> actions = state.getActions(hand);
        ArrayList<BlackJackAction> expectedActions = new ArrayList<>(List.of(
                new DoubleAction(), new HitAction(), new StandAction(), new SurrenderAction()));
        assertEquals(4, actions.size());
        aseertContainsActions(expectedActions, actions);
    }

    @Test
    public void testNormalWithSplit() {
        handState state = new Normal();
        Hand hand = new Hand();
        Card card0 = new Card(Deck.SUITS[0], Deck.RANKS[0]);
        Card card1 = new Card(Deck.SUITS[1], Deck.RANKS[0]);
        hand.addCard(card0);
        hand.addCard(card1);

        List<BlackJackAction> actions = state.getActions(hand);
        ArrayList<BlackJackAction> expectedActions = new ArrayList<>(List.of(
                new DoubleAction(), new HitAction(), new StandAction(),
                new SurrenderAction(), new SplitAction()));
        assertEquals(5, actions.size());
        aseertContainsActions(expectedActions, actions);

    }

    public static void aseertContainsActions(List<BlackJackAction> expected, List<BlackJackAction> actual) {
        for (BlackJackAction blackJackAction : expected) {
            assertActionExists(blackJackAction, actual);
        }
    }

    public static void assertActionExists(BlackJackAction expected, List<BlackJackAction> actual) {
        for (BlackJackAction blackJackAction : actual) {
            if (blackJackAction.getClass() == expected.getClass()) {
                return;
            }
        }

        throw new AssertionError(String.format("%s not in actions", expected));
    }
}
