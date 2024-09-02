package blackjack.player;

import blackjack.Play;
import blackjack.PlayerManager;
import blackjack.actions.*;
import blackjack.deck.Card;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final PlayerManager playerManager;
    private final List<Card> cardsInHand;
    private int handValue;
    private double bet = 10;
    private final List<BlackJackAction> actions;
    private boolean surrendered;
    private boolean standing;
    private boolean bust;
    private final String name;


    public Player(String name, PlayerManager playerManager) {
        this.name = name;
        this.playerManager = playerManager;
        this.handValue = 0;
        this.cardsInHand = new ArrayList<>();
        this.standing = false;
        this.surrendered = false;
        this.bust = false;

        this.actions = new ArrayList<>();
        actions.add(new HitAction());
        actions.add(new StandAction());
        actions.add(new SplitAction());
        actions.add(new DoubleAction());
        actions.add(new SurrenderAction());

        Play.addPlayer(this);
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

    public List<Card> getCardsInHand() {
        return this.cardsInHand;
    }

    public void addCardToHand(Card dealtCard) {
        cardsInHand.add(dealtCard);
    }

    public int getHandValue() {
        return this.handValue;
    }

    public void setHandValue(int value) {
        this.handValue = value;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public boolean canSplit() {
        Card one = cardsInHand.get(0);
        Card two = cardsInHand.get(1);
        if (one.toString().contains("A") && two.toString().contains("A")) {
            return true;
        }
        return cardsInHand.size() == 2 && one.rankValue(handValue) == two.rankValue(handValue);
    }

    public void splitHand() {
        // todo --> add logic
    }

    public void calculateCards() {
        this.handValue = 0;
        for (Card card : cardsInHand) {
            this.handValue += card.rankValue(handValue);
        }
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
    public String toString() {
        return name + " {" +
                "cardsInHand=" + cardsInHand +
                ", handValue=" + handValue +
                '}';
    }
}
