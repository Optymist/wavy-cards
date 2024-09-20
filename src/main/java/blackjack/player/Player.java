package blackjack.player;

import blackjack.Play;
import blackjack.PlayerManager;
import blackjack.actions.*;
import blackjack.deck.Card;
import blackjack.player.state.Normal;
import blackjack.player.state.Split;
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
    private ArrayList<Hand> splitPlay = new ArrayList<>();
    private double money;
    private double bet;
    private List<BlackJackAction> actions;
    private boolean surrendered;
    private boolean standing;
    private boolean bust;
    private boolean isTurn;
    private boolean hasBlackJack;
    private final String name;
    private String turnResponse = null;
    private boolean isSplit;


    public Player(String name, PlayerManager playerManager) {
        this.name = name;
        this.playerManager = playerManager;
//        this.handValue = 0;
        this.cardsInHand = new Hand();
        this.standing = false;
        this.surrendered = false;
        this.bust = false;
        this.isTurn = false;
        this.isSplit = false;
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

    public void setIsSplit(boolean bool) {
        this.isSplit = bool;
    }

    public boolean getIsSplit() {
        return this.isSplit;
    }

    /**
     * Handle's the player's turn based on their current state.
     * @param game The game that the player is connected to so that 
     *             we can call `action.execute(Play)`
     */
    public void manageTurn(Hand playerHand, Play game) {
//        if (!(splitPlay.getCards().isEmpty())) {
//            System.out.println(splitPlay);
//            handleSplitPlay(game);
//        } else {
            while (playerHand.getState() instanceof Normal) {
                this.setTurn(true);

                this.actions = playerHand.getState().getActions(cardsInHand);

                String turnRequest = GenerateJson.generateTurnRequest(this, playerHand);
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
                            action.execute(playerHand,this, game);
                            continueTurn = false;
                            turnResponse = null;
                            if (this.isSplit) {
                                // this.getCardsInHand().getState().doRound(this, game);
                                // break;
                                return;
                            }
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

//    public void addCardToHand(Card dealtCard) {
//        this.setState(cardsInHand.addCard(dealtCard));
//    }
//
//    public void setState(playerState state) {
//        this.state = state;
//    }
//
//    public playerState getState() {
//        return state;
//    }

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


    public Hand splitHand(Play game) {
        isSplit = true;
        Hand secondHand = new Hand();

        List<Card> cards = getCardsInHand().getCards();
        Card newDeckCard = getCardsInHand().getCards().remove(cards.size() - 1);

        secondHand.addCard(newDeckCard);

        this.cardsInHand.addCard(game.getDeck().deal());
        secondHand.addCard(game.getDeck().deal());

        this.splitPlay.add(cardsInHand);
        this.splitPlay.add(secondHand);

        return secondHand;
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
        return name + " {" +
                "cardsInHand=" + cardsInHand.getCards() +
                ", handValue=" + cardsInHand.getValue() +
                ", moneyLeft=" + money +
                '}';
    }
}
