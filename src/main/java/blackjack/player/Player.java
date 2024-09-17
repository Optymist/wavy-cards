package blackjack.player;

import blackjack.Play;
import blackjack.PlayerManager;
import blackjack.actions.*;
import blackjack.deck.Card;
import blackjack.player.state.Normal;
import blackjack.player.state.playerState;
import blackjack.protocol.DecryptJson;
import blackjack.protocol.GenerateJson;
import blackjack.protocol.Exceptions.InvalidAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;

public class Player {
    private final PlayerManager playerManager;
    private final Hand cardsInHand;
//    private int handValue;
    private double money;
    private double bet;
    private List<BlackJackAction> actions;
    private boolean surrendered;
    private boolean standing;
    private boolean bust;
    private boolean isTurn;
    private boolean hasBlackJack;
    private final String name;
    private playerState state;
    private String turnResponse = null;


    public Player(String name, PlayerManager playerManager) {
        this.name = name;
        this.playerManager = playerManager;
//        this.handValue = 0;
        this.cardsInHand = new Hand();
        this.standing = false;
        this.surrendered = false;
        this.bust = false;
        this.isTurn = false;
        this.hasBlackJack = false;
        this.money = 2500;
        this.bet = 10;
        this.state = new Normal();

        this.actions = new ArrayList<>();
        actions.add(new HitAction());
        actions.add(new StandAction());
        actions.add(new SplitAction());
        actions.add(new DoubleAction());
        actions.add(new SurrenderAction());

        Play.addPlayer(this);
    }

    /**
     * Handle's the player's turn based on their current state.
     * @param game The game that the player is connected to so that 
     *             we can call `action.execute(Play)`
     */
    public void manageTurn(Play game) {
        while (state instanceof Normal) {
            this.setTurn(true);

            this.actions = state.getActions(cardsInHand);

            String turnRequest = GenerateJson.generateTurnRequest(this);
            playerManager.sendMessage(turnRequest);
            // send turnRequest

            boolean continueTurn = true;
            while (continueTurn) {
                try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
                    System.out.println("Sleep inturuppted");
				}
                if (turnResponse != null) {
                    BlackJackAction action;
					try {
						action = DecryptJson.getChosenAction(turnResponse, this);
                        action.execute(this, game);
                        continueTurn = false;
                        turnResponse = null;
					} catch (InvalidAction e) {
                        // send invalid action message to client
                        // technically doing double work but rather have it
                        // and not need it than need it and not have it 
                        playerManager.sendMessage(turnRequest);
                        turnResponse = null;
					} catch (JsonProcessingException e) {
                        playerManager.sendMessage(turnRequest);
                        turnResponse = null;
						e.printStackTrace();
					}
                }
                // System.out.println(turnResponse);
            }

            this.setTurn(false);
        }
    }

    public void setTurnResponse(String response) {
        this.turnResponse = response;
        System.out.println("Setting response: " + response);
    }

    public String getTurnResponse() {
        return turnResponse;
    }

    public void addCardToHand(Card dealtCard) {
        this.setState(cardsInHand.addCard(dealtCard));
    }

    public void setState(playerState state) {
        this.state = state;
    }

    public playerState getState() {
        return state;
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

    public boolean isStanding() {
        return standing;
    }

    public void setStanding(boolean standing) {
        this.standing = standing;
    }

    public boolean isSurrendered() {
        return surrendered;
    }

    public void surrender() {
        this.bet /= 2;
    }

    public void doubleBet() {
        this.bet *= 2;
    }

    public void removeBet() {
        this.money -= bet;
    }

    public Hand getCardsInHand() {
        return this.cardsInHand;
    }

    public int getHandValue() {
        return cardsInHand.getValue();
    }

    public void setBlackJack(boolean isTrue) {
        this.hasBlackJack = isTrue;
    }

    public boolean getBlackJack() {
        return hasBlackJack;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }


    public void splitHand() {
        // todo --> add logic
    }


    public void performAction(BlackJackAction action, Play game) {
        action.execute(this, game);
        // playerManager.sendMessage("Invalid action: " + actionName);
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
        return name + " {" +
                "cardsInHand=" + cardsInHand.getCards() +
                ", handValue=" + cardsInHand.getValue() +
                ", moneyLeft=" + money +
                '}';
    }
}
