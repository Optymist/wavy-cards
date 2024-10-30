package blackjack.protocol;

import blackjack.Client.protocol.Generate;
import blackjack.actions.BlackJackAction;
import blackjack.actions.HitAction;
import blackjack.protocol.Exceptions.InvalidAction;
import blackjack.protocol.Exceptions.InvalidBet;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DecryptJsonServerTests {
    @Test
    public void testGetChosenActionValid() throws InvalidAction, JsonProcessingException {
        List<BlackJackAction> actions = new ArrayList<>();
        actions.add(new HitAction());

        String turnResponse = Generate.generateTurnResponse("Sal", "hit");
        BlackJackAction chosenAction = DecryptJson.getChosenAction(turnResponse, actions);

        assertEquals(new HitAction(), chosenAction);
    }

    @Test
    public void testGetChosenActionInvalid() {
        List<BlackJackAction> actions = new ArrayList<>();
        actions.add(new HitAction());

        String turnResponse = Generate.generateTurnResponse("Fred", "split");
        assertThrows(InvalidAction.class, () -> DecryptJson.getChosenAction(turnResponse, actions));
    }

    @Test
    public void testGetBetValid() throws InvalidBet, JsonProcessingException {
        String betResponse = Generate.generateBetResponse("sal", 100);
        int bet = DecryptJson.getBet(2500, betResponse);

        assertEquals(100, bet);
    }

    @Test
    public void testGetBetInvalid() {
        String betResponse = Generate.generateBetResponse("sal", 2000);
        assertThrows(InvalidBet.class, () -> DecryptJson.getBet(1800, betResponse));
    }
}