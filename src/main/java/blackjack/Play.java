package blackjack;

import java.util.ArrayList;
import java.util.List;

import blackjack.deck.Deck;
import blackjack.player.Dealer;
import blackjack.player.Hand;
import blackjack.player.Player;
import blackjack.player.state.BlackJack;
import blackjack.player.state.Normal;
import blackjack.player.state.Stand;
import blackjack.protocol.GenerateJson;

public class Play implements Runnable {
    public static List<Player> players = new ArrayList<>();
    private volatile boolean running = true;
    private static Dealer dealer;
    private static int numPlayers;
    private static Deck deck;
    private int currentPlayerIndex;

    public Play(int maxPlayers) {
        dealer = new Dealer();
        numPlayers = maxPlayers;
        setupDeck();
        currentPlayerIndex = 0;
    }

    private void setupDeck() {
        int numDecks = 1;
        if (numPlayers > 3) {
            numDecks = 2;
        }
        deck = new Deck(numDecks);
    }

    @Override
    public void run() {
        try {
            startGame();
        } catch (Exception e) {
            System.out.println("Game encountered an error:\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void startGame() {
        dealInitialCards();
        broadcastToAllPlayers(GenerateJson.generateUpdate(this));
        for (Player player : players) {
            player.removeBet();
            if (player.getCardsInHand().getState() instanceof BlackJack) {
                player.getPlayerManager().sendMessage(GenerateJson.generateGeneralMessage("You got blackjack!"));
                player.blackJackPayout();
            }
            System.out.println(player);
        }

        System.out.println(dealer);

        while (running) {
            for (Player player : players) {
                broadcastExcludingCurrent(GenerateJson.generateGeneralMessage(player.getName() + "'s turn."), player);
                player.manageTurn(player.getCardsInHand(), this);
                if (player.getIsSplit()) {
                    player.removeBet();
                    player.setIsSplit(false);
                    for (Hand hand : player.getSplitPlay()) {
                        hand.setBeanSplit(true);
                        player.getPlayerManager().sendMessage(GenerateJson.generateGeneralMessage("Current playing hand: " + hand));
                        player.manageTurn(hand, this);
                    }
                }
            }
            broadcastToAllPlayers(GenerateJson.generateUpdate(this));
            dealerTurn();
            payout();
            stopGame();
        }
    }

    public boolean allComplete() {
        for (Player player : players) {
            if (player.getCardsInHand().getState() instanceof Normal) {
                return false;
            }
        }
        return true;
    }

    public static void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public static void dealInitialCards() {
        for (int i = 0; i < 2; i++) {
            for (Player player : players) {
                if (player.getCardsInHand().getCards().size() < 2) {
                    player.getCardsInHand().addCard(deck.deal());
                }
            }
            if (dealer.getCardsInHand().getCards().size() < 2) {
                dealer.addCardToHand(deck.deal());
            }
        }
    }

    public void dealerTurn() {
        broadcastToAllPlayers(GenerateJson.generateGeneralMessage("Dealer's turn."));
        broadcastToAllPlayers(GenerateJson.generateGeneralMessage("Initial cards: " + dealer.toString()));
        while (dealer.getCardsInHand().getValue() < 17) {
            dealer.addCardToHand(deck.deal());
            broadcastToAllPlayers(GenerateJson.generateGeneralMessage(dealer.toString()));
        }
        if (dealer.getCardsInHand().getValue() > 21) {
            dealer.setBust(true);
            broadcastToAllPlayers(GenerateJson.generateGeneralMessage("Dealer busts with " + dealer.getCardsInHand().getValue() + "!"));
        } else {
            broadcastToAllPlayers(GenerateJson.generateGeneralMessage("Dealer stands on " + dealer.getCardsInHand().getValue() + "."));
        }
    }

    private void payout() {
        for (Player player : players) {
            if (!(player.getSplitPlay().isEmpty())) {
                handleSplitPlayPayout(player);
            } else {
                determinePayout(player, player.getCardsInHand());
            }
        }
    }

    private void determinePayout(Player player, Hand hand) {
        if (dealer.getBust() && hand.getState() instanceof Stand) {
            player.winBet();
        } else {
            handleCardAnalysis(player, hand);
        }
    }

    private void handleSplitPlayPayout(Player player) {
        for (Hand hand : player.getSplitPlay()) {
            determinePayout(player, hand);
        }
    }

    private void handleCardAnalysis(Player player, Hand hand) {
        int comparison = analyseCards(hand.getValue());
        if (comparison == 0) {
            player.pushBet();
        } else if (comparison == 1) {
            player.winBet();
        } else {
            player.loseBet();
        }

    }

    /**
     * Returns -1 if the playerHandValue is less than the dealers.
     * Returns 0 if they are equal.
     * Returns 1 if the playerHandValue is greater than the dealers.
     * @param playerHandValue --> final hand value for the player.
     * @return --> int comparison.
     */
    private int analyseCards(int playerHandValue) {
        return Integer.compare(playerHandValue, dealer.getHandValue());
    }

    public void stopGame() {
        running = false;
        System.out.println("Stopping");
    }


    public void broadcastToAllPlayers(String message) {
        System.out.println(message);
        for (Player player : players) {
            player.getPlayerManager().sendMessage(message);
        }
    }

    public void broadcastExcludingCurrent(String message, Player toExclude) {
        for (Player player : players) {
            if (!player.equals(toExclude)) {
                player.getPlayerManager().sendMessage(message);
            }
        }
    }

    public void clearAllPlayers() {
        players.clear();
    }

    public Dealer getDealer() {
        return dealer;
    }

    public Deck getDeck() {
        return deck;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void incrementPlayerIndex() {
        if (currentPlayerIndex < players.size() - 1) {
            currentPlayerIndex++;
        } else {
            currentPlayerIndex = 0;
        }

    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }
}
