package blackjack.player;

import blackjack.Play;
import blackjack.PlayerManager;
import blackjack.actions.*;
import blackjack.player.state.playerState;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Player {
    private final PlayerManager playerManager;
    private final Hand cardsInHand;
//    private int handValue;
    private double money;
    private double bet;
    private final List<BlackJackAction> actions;
    private boolean surrendered;
    private boolean standing;
    private boolean bust;
    private boolean isTurn;
    private boolean hasBlackJack;
    private final String name;
    private playerState state;


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

        this.actions = new ArrayList<>();
        actions.add(new HitAction());
        actions.add(new StandAction());
        actions.add(new SplitAction());
        actions.add(new DoubleAction());
        actions.add(new SurrenderAction());

        Play.addPlayer(this);
    }

    public void manageTurn() {
        state.getActions(cardsInHand);
        state.doRound();
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
        this.surrendered = true;
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

    public boolean turnOver() {
        if (this.isStanding() || this.isSurrendered() || this.isBust()) {
            this.setTurn(false);
            return true;
        }
        return false;
    }



    public void performAction(String actionName, Play game) {
        for (BlackJackAction action : actions) {
            if (action.getActionName().equalsIgnoreCase(actionName)) {
                action.execute(this, game);
                return;
            }
        }
        playerManager.sendMessage("Invalid action: " + actionName);
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
