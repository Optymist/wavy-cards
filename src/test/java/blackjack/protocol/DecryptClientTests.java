package blackjack.protocol;

import blackjack.Client.protocol.Decrypt;
import blackjack.Play;
import blackjack.PlayerManager;
import blackjack.deck.Card;
import blackjack.deck.Deck;
import blackjack.helperClasses.mockedPlayerManager;
import blackjack.player.Player;
import blackjack.player.state.Normal;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DecryptClientTests {
    private PlayerManager mockPlayerManager;

    @BeforeEach
    public void setUp() {
        mockedPlayerManager.setUp();
        this.mockPlayerManager = mockedPlayerManager.mockPlayerManager;
    }

    @Test
    public void testGetAvailableActionsWithoutSplit() throws JsonProcessingException {
        Player gertrude = new Player("gertrude", mockPlayerManager);
        gertrude.setState(new Normal());
        gertrude.addCardToHand(new Card(Deck.SUITS[0], Deck.RANKS[0]));
        gertrude.addCardToHand(new Card(Deck.SUITS[0], Deck.RANKS[2]));

        String turnRequest = GenerateJson.generateTurnRequest(gertrude);
        JsonNode turnRequestNode = Decrypt.decryptServerMessage(turnRequest);
        String requestMessage = Decrypt.getAvailableActions(turnRequestNode);

        assertTrue(requestMessage.contains("stand"));
        assertTrue(requestMessage.contains("hit"));
        assertTrue(requestMessage.contains("double"));
        assertTrue(requestMessage.contains("surrender"));
        assertFalse(requestMessage.contains("split"));
    }

    @Test
    public void testGetAvailableActionsWithSplit() throws JsonProcessingException {
        Player minnie = new Player("minnie", mockPlayerManager);
        minnie.setState(new Normal());
        minnie.addCardToHand(new Card(Deck.SUITS[0], Deck.RANKS[0]));
        minnie.addCardToHand(new Card(Deck.SUITS[1], Deck.RANKS[0]));

        String turnRequest = GenerateJson.generateTurnRequest(minnie);
        JsonNode turnRequestNode = Decrypt.decryptServerMessage(turnRequest);
        String requestMessage = Decrypt.getAvailableActions(turnRequestNode);

        assertTrue(requestMessage.contains("stand"));
        assertTrue(requestMessage.contains("hit"));
        assertTrue(requestMessage.contains("double"));
        assertTrue(requestMessage.contains("surrender"));
        assertTrue(requestMessage.contains("split"));
    }

    @Test
    public void testGetInfoFromUpdate() throws JsonProcessingException {
        Play game = new Play(2);
        game.clearAllPlayers();
        Player fred = new Player("fred", mockPlayerManager);
        Player greg = new Player("greg", mockPlayerManager);
        fred.setState(new Normal());
        greg.setState(new Normal());

        fred.addCardToHand(new Card(Deck.SUITS[0], Deck.RANKS[0]));
        greg.addCardToHand(new Card(Deck.SUITS[0], Deck.RANKS[2]));

        String updateRequest = GenerateJson.generateUpdate(game);
        JsonNode updateRequestNode = Decrypt.decryptServerMessage(updateRequest);
        String turnMessage = Decrypt.getTurn(updateRequestNode);
        String fredCards = Decrypt.getPlayerCardInfo(updateRequestNode, "fred");
        String gregCards = Decrypt.getPlayerCardInfo(updateRequestNode, "greg");

        assertEquals("fred's turn.", turnMessage);
        assertTrue(fredCards.contains("2♠"));
        assertTrue(gregCards.contains("4♠"));
    }
}