package blackjack;

import java.util.ArrayList;
import java.util.List;

import blackjack.deck.Deck;
import blackjack.player.Dealer;
import blackjack.player.Hand;
import blackjack.player.Player;
import blackjack.player.state.BlackJack;
import blackjack.player.state.Bust;
import blackjack.player.state.Stand;
import blackjack.protocol.GenerateJson;

/**
 * Play class that manages the game play.
 */
public class Play implements Runnable {
    public static List<Player> players = new ArrayList<>();
    private volatile boolean running = true;
    private static Dealer dealer;
    private static int numPlayers;
    private static Deck deck;
    private int currentPlayerIndex;

    /**
     * Constructor that sets up the dealer and the deck.
     * @param maxPlayers --> the number of players in the game.
     */
    public Play(int maxPlayers) {
        dealer = new Dealer();
        numPlayers = maxPlayers;
        setupDeck();
        currentPlayerIndex = 0;
    }

    /**
     * Setting up the deck based on the amount of players.
     */
    private void setupDeck() {
        int numDecks = 1;
        if (numPlayers > 3) {
            numDecks = 2;
        }
        deck = new Deck(numDecks);
    }

    /**
     * Run method that checks whether deck needs to be setup again.
     * Calls the getBets method before starting the game.
     */
    @Override
    public void run() {
        if (deck.getPlayDeck().size() < numPlayers*5) {
            setupDeck();
        }
        broadcastToAllPlayers(GenerateJson.generateGeneralMessage("\nRound Starting...\n"));
        running = true;
        getBets();
        try {
            startGame();
        } catch (Exception e) {
            System.out.println("Game encountered an error:\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles a round for the blackjack game.
     */
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
        this.run();
    }

    /**
     * Loops through players in the game and calls the method to manage the bet chosen by each player.
     */
    private void getBets() {
        for (Player player : players) {
            player.manageBet();
        }
    }

    /**
     * Adds a player to the game.
     * @param player --> the player to add to the game.
     */
    public static void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * Removes a player from the game.
     * @param player --> the player to remove from the game.
     */
    public void removePlayer(Player player) {
        players.remove(player);
    }

    /**
     * Deals the starting cards to the players and the dealer.
     */
    public static void dealInitialCards() {
        for (int i = 0; i < 2; i++) {
            for (Player player : players) {
                if (player.getCardsInHand().getCards().size() < 2) {
                    player.getCardsInHand().addCard(deck.deal());
                }
            }
            if (dealer.getCardsInHand().getCards().size() < 2) {
                dealer.getCardsInHand().addCard(deck.deal());
            }
        }
    }

    /**
     * Handles the dealers turn.
     */
    public void dealerTurn() {
        broadcastToAllPlayers(GenerateJson.generateGeneralMessage("Dealer's turn."));
        broadcastToAllPlayers(GenerateJson.generateGeneralMessage("Initial cards: " + dealer.toString()));
        while (dealer.getCardsInHand().getValue() < 17) {
            dealer.getCardsInHand().addCard(deck.deal());
            broadcastToAllPlayers(GenerateJson.generateGeneralMessage(dealer.toString()));
        }
        if (dealer.getCardsInHand().getValue() > 21) {
            dealer.setBust(true);
            broadcastToAllPlayers(GenerateJson.generateGeneralMessage("Dealer busts with " + dealer.getCardsInHand().getValue() + "!"));
        } else {
            broadcastToAllPlayers(GenerateJson.generateGeneralMessage("Dealer stands on " + dealer.getCardsInHand().getValue() + "."));
        }
    }

    /**
     * Handles the payout to the players.
     */
    private void payout() {
        for (Player player : players) {
            if (!(player.getSplitPlay().isEmpty())) {
                handleSplitPlayPayout(player);
            } else {
                determinePayout(player, player.getCardsInHand());
            }
        }
    }

    /**
     * Determines whether player instantly wins or whether cards need to be compared to the dealers.
     * @param player --> player we are checking.
     * @param hand --> their current hand.
     */
    private void determinePayout(Player player, Hand hand) {
        if (dealer.getBust() && hand.getState() instanceof Stand) {
            player.winBet();
        } else if (hand.getState() instanceof Bust) {
            player.loseBet();
        } else {
            handleCardAnalysis(player, hand);
        }
    }

    /**
     * Calls the payout determination of a split hand.
     * @param player --> player whose hands we are checking.
     */
    private void handleSplitPlayPayout(Player player) {
        for (Hand hand : player.getSplitPlay()) {
            determinePayout(player, hand);
        }
    }

    /**
     * Handles the payout after card analysis.
     * @param player --> current player who's cards we are analysing.
     * @param hand --> hand to analyse.
     */
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

    /**
     * Calling for the players and dealers to be reset so a new round can start.
     * Sets running to false so that the loop stops.
     */
    public void stopGame() {
        running = false;
        broadcastToAllPlayers(GenerateJson.generateGeneralMessage("\nStopping round..."));
        for (Player player : players) {
            player.reset();
        }
        dealer.reset();
    }

    /**
     * Send a broadcast message to all players.
     * @param message --> the json String we would like to send.
     */
    public void broadcastToAllPlayers(String message) {
        System.out.println(message);
        for (Player player : players) {
            player.getPlayerManager().sendMessage(message);
        }
    }

    /**
     * Send a broadcast message to all players except one.
     * @param message --> the json String we would like to send.
     * @param toExclude --> the player to exclude from being sent the message.
     */
    public void broadcastExcludingCurrent(String message, Player toExclude) {
        for (Player player : players) {
            if (!player.equals(toExclude)) {
                player.getPlayerManager().sendMessage(message);
            }
        }
    }

    /**
     * Remove all the players currently in the game.
     */
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

    /**
     * Add to the currentPlayerIndex so that it moves to the next player in the list.
     */
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
