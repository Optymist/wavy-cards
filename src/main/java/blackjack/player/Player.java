package blackjack.player;

import blackjack.Play;
import blackjack.PlayerConnection;
import blackjack.actions.*;
import blackjack.deck.Card;
import blackjack.player.state.Bust;
import blackjack.player.state.Normal;
import blackjack.player.state.Surrender;
import blackjack.protocol.DecryptJson;
import blackjack.protocol.Exceptions.InvalidBet;
import blackjack.protocol.GenerateJson;
import blackjack.protocol.Exceptions.InvalidAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Player class:
 * Holds all the necessary properties and methods for a player in the game.
 */
public class Player {
    private final PlayerConnection playerManager;
    private final Hand cardsInHand;
    private ArrayList<Hand> splitPlay = new ArrayList<>();
    private double money;
    private double bet;
    private List<BlackJackAction> actions;
    private boolean surrendered;
    private boolean standing;
    private boolean bust;
    private boolean isTurn;
    private final String name;
    private String turnResponse = null;
    private boolean isSplit;
    private String betResponse = null;
    private boolean isChoosingBet;


    /**
     * Initialize the necessary properties of the player.
     * @param name the name chosen by the client.
     * @param playerManager responsible for this player.
     */
    public Player(String name, PlayerConnection playerManager) {
        this.name = name;
        this.playerManager = playerManager;
        this.cardsInHand = new Hand();
        this.standing = false;
        this.surrendered = false;
        this.bust = false;
        this.isTurn = false;
        this.isSplit = false;
        this.isChoosingBet = false;
        this.money = 2500;

        this.actions = new ArrayList<>();
        actions.add(new HitAction());
        actions.add(new StandAction());
        actions.add(new SplitAction());
        actions.add(new DoubleAction());
        actions.add(new SurrenderAction());

        Play.addPlayer(this);
    }

    /**
     * Handle's the player's turn based on their current hand's state.
     * @param game The game that the player is connected to so that 
     *             we can call `action.execute(Play)`
     */
    public void manageTurn(Hand playerHand, Play game) {
        while (playerHand.getState() instanceof Normal && game.getPlayers().contains(this)) {
            this.setTurn(true);

            this.actions = playerHand.getState().getActions(playerHand);

            String turnRequest = GenerateJson.generateTurnRequest(this, playerHand);
            playerManager.sendMessage(turnRequest);

            boolean continueTurn = true;
            int elapsed = 0;
            while (continueTurn) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("Sleep interrupted.");
                }
                elapsed++;
                if (turnResponse != null) {
                    BlackJackAction action;
                    try {
                        action = DecryptJson.getChosenAction(turnResponse, this.actions);
                        action.execute(playerHand,this, game);
                        game.broadcastToAllPlayers(GenerateJson.generateUpdate(game, true));
                        continueTurn = false;
                        turnResponse = null;
                        if (this.isSplit) {
                            return;
                        }
                    } catch (InvalidAction e) {
                        playerManager.sendMessage(GenerateJson.generateGeneralMessage("Invalid action. Please choose from the available actions!"));
                        playerManager.sendMessage(turnRequest);
                        turnResponse = null;
                    } catch (JsonProcessingException e) {
                        playerManager.sendMessage(turnRequest);
                        turnResponse = null;
                        e.printStackTrace();
                    }
                } else if (elapsed >= 30) {
                    game.kickPlayer(this, "You were removed for taking too long to act.");
                    this.setTurn(false);
                    return;
                }
            }
            bustOrSurrendered(playerHand);

            this.setTurn(false);
            game.incrementPlayerIndex();
        }
    }

    /**
     * Checks whether the player has busted or surrendered and acts accordingly.
     * @param playerHand --> the hand the turn is currently on.
     */
    private void bustOrSurrendered(Hand playerHand) {
        if (playerHand.getState() instanceof Bust) {
            playerManager.sendMessage(GenerateJson.generateGeneralMessage("Bust! You lost your bet of $" + (int) bet + "\n" +
                    "Balance: $" + (int) money));
        }
        if (playerHand.getState() instanceof Surrender) {
            this.surrenderPayout();
            playerManager.sendMessage(GenerateJson.generateGeneralMessage("Surrendered — half bet returned: $" + (int) bet + "\n" +
                    "Balance: $" + (int) money));
        }
    }

    /**
     * Split the player's current hand into two hands and add them to the splitPlay list.
     * @param game the current game the player is a part of.
     * @return secondHand created from the original hand.
     */
    public Hand splitHand(Play game, Hand handToSplit) {
        isSplit = true;
        Hand secondHand = new Hand();

        List<Card> cards = handToSplit.getCards();
        Card newDeckCard = cards.remove(cards.size() - 1);

        secondHand.addCard(newDeckCard);

        handToSplit.addCard(game.getDeck().deal());
        secondHand.addCard(game.getDeck().deal());

        if (splitPlay.isEmpty()) {
            this.splitPlay.add(handToSplit);
        }
        this.splitPlay.add(secondHand);

        return secondHand;
    }

    /**
     * Sends the bet request to this player. Polling and timeout are handled by Play.getBets().
     */
    public void manageBet() {
        this.setIsChoosingBet(true);
        playerManager.sendMessage(GenerateJson.generateBetRequest(name,
                "Balance: $" + (int) money + " — You have 45 seconds to place your bet."));
    }


    /**
     * Set the response sent from the client (the chosen action).
     * @param response from the client.
     */
    public void setTurnResponse(String response) {
        this.turnResponse = response;
        System.out.println("Setting response: " + response);
    }

    public void setBetResponse(String response) {
        this.betResponse = response;
        System.out.println("Setting response: " + response);
    }

    public String getBetResponse() { return betResponse; }

    public void clearBetResponse() { betResponse = null; }

    public boolean getIsChoosingBet() {
        return isChoosingBet;
    }

    public void setIsChoosingBet(boolean isChoosingBet) {
        this.isChoosingBet = isChoosingBet;
    }

    public void setIsSplit(boolean bool) {
        this.isSplit = bool;
    }

    public boolean getIsSplit() {
        return this.isSplit;
    }

    public boolean isTurn() {
        return isTurn;
    }

    public void setTurn(boolean turn) {
        this.isTurn = turn;
    }

    public boolean isBust() { return this.bust; }

    public void setBust(boolean isBust) {
        this.bust = isBust;
    }

    public String getName() {
        return this.name;
    }

    public double getBet() {
        return this.bet;
    }

    public double getMoney() { return this.money; }

    public void setBet(double bet) { this.bet = bet; }

    public void surrender() {
        this.bet /= 2;
    }

    public void doubleBet() {
        removeBet();
        this.bet *= 2;
    }

    public void removeBet() {
        this.money -= bet;
        playerManager.sendMessage(GenerateJson.generateGeneralMessage("Bet placed: $" + (int) bet + "  |  Balance: $" + (int) money));
    }

    public void surrenderPayout() {
        this.money += bet;
    }

    public void winBet() {
        this.money += 2 * bet;
        playerManager.sendMessage(GenerateJson.generateGeneralMessage("You win — $" + (int)(2 * bet) + " returned  |  Balance: $" + (int) money));
    }

    public void pushBet() {
        this.money += bet;
        playerManager.sendMessage(GenerateJson.generateGeneralMessage("Push — bet returned: $" + (int) bet + "  |  Balance: $" + (int) money));
    }

    public void loseBet() {
        playerManager.sendMessage(GenerateJson.generateGeneralMessage("You lost $" + (int) bet + ".  |  Balance: $" + (int) money));
    }

    public void blackJackPayout() {
        double payout = bet + (1.5 * bet);
        this.money += payout;
        playerManager.sendMessage(GenerateJson.generateGeneralMessage("Blackjack! Payout: $" + (int) payout + "  |  Balance: $" + (int) money));
    }

    public Hand getCardsInHand() {
        return this.cardsInHand;
    }

    public int getHandValue() {
        return cardsInHand.getValue();
    }

    public void reset() {
        this.cardsInHand.clearCards();
        this.splitPlay.clear();
        this.standing = false;
        this.surrendered = false;
        this.bust = false;
        this.isTurn = false;
        this.isSplit = false;
        this.bet = 0.0;
    }

    public PlayerConnection getPlayerManager() {
        return playerManager;
    }

    public List<Hand> getSplitPlay() {
        return this.splitPlay;
    }

    public void performAction(BlackJackAction action, Play game) {
        action.execute(this.cardsInHand,this, game);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Double.compare(money, player.money) == 0 &&
                Double.compare(bet, player.bet) == 0 &&
                surrendered == player.surrendered &&
                standing == player.standing &&
                bust == player.bust &&
                isTurn == player.isTurn &&
                Objects.equals(playerManager, player.playerManager) &&
                Objects.equals(cardsInHand, player.cardsInHand) &&
                Objects.equals(actions, player.actions) &&
                Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerManager, cardsInHand, money,
                bet, actions, surrendered, standing, bust, name, isTurn);
    }

    @Override
    public String toString() {
        if (splitPlay.isEmpty()) {
            return name + " {" +
                    "cardsInHand=" + cardsInHand.getCards() +
                    ", handValue=" + cardsInHand.getValue() +
                    ", moneyLeft=" + money +
                    '}';
        } else {
            Hand splitHand = splitPlay.get(1);
            return name + " {" +
                    "cardsInHand=" + cardsInHand.getCards() +
                    ", handValue=" + cardsInHand.getValue() +
                    ", moneyLeft=" + money +
                    '}' + '\n' +
                    name + " {" +
                    "cardsInHand=" + splitHand.getCards() +
                    ", handValue=" + splitHand.getValue() +
                    ", moneyLeft=" + money +
                    '}';
        }

    }
}
