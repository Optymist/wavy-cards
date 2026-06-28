package blackjack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import blackjack.protocol.DecryptJson;
import blackjack.protocol.Exceptions.InvalidBet;

import blackjack.deck.Card;
import blackjack.deck.Deck;
import blackjack.player.Dealer;
import blackjack.player.Hand;
import blackjack.player.Player;
import blackjack.player.state.BlackJack;
import blackjack.player.state.Bust;
import blackjack.player.state.Stand;
import blackjack.player.state.Surrender;
import blackjack.protocol.GenerateJson;

/**
 * Play class that manages the game play.
 */
public class Play implements Runnable {
    public static List<Player> players = new CopyOnWriteArrayList<>();
    private static final List<Player> pendingPlayers = new ArrayList<>();
    private static volatile boolean roundInProgress = false;
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
        synchronized (Play.class) {
            for (Player p : pendingPlayers) {
                p.getPlayerManager().sendMessage(GenerateJson.generateGeneralMessage("Round starting — you're in!"));
                players.add(p);
            }
            pendingPlayers.clear();
        }
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
        for (Player player : players) {
            if (player.getCardsInHand().getState() instanceof BlackJack) {
                player.getPlayerManager().sendMessage(GenerateJson.generateGeneralMessage("You got blackjack! Waiting for dealer..."));
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
                    for (int i = 0; i < player.getSplitPlay().size(); i++) {
                        Hand hand = player.getSplitPlay().get(i);
                        hand.setBeanSplit(true);
                        pause(500);
                        player.getPlayerManager().sendMessage(GenerateJson.generateGeneralMessage(
                            "Playing split hand " + (i + 1) + ": " + formatCards(hand.getCards()) + "  [" + hand.getValue() + "]"));
                        player.manageTurn(hand, this);
                        if (player.getIsSplit()) {
                            player.removeBet();
                            player.setIsSplit(false);
                        }
                    }
                }
            }
            broadcastToAllPlayers(GenerateJson.generateUpdate(this, true));
            dealerTurn();
            pause(600);
            payout();
            pause(800);
            stopGame();
        }
        this.run();
    }

    private static final int BET_TIMEOUT_SECONDS = 45;

    /**
     * Sends bet requests to all players simultaneously, then collects responses for up to 45 seconds.
     * Players who do not bet in time are kicked.
     */
    private void getBets() {
        roundInProgress = true;
        for (Player player : players) {
            player.manageBet();
        }

        long deadline = System.currentTimeMillis() + BET_TIMEOUT_SECONDS * 1000L;
        while (System.currentTimeMillis() < deadline && players.stream().anyMatch(Player::getIsChoosingBet)) {
            for (Player player : players) {
                if (!player.getIsChoosingBet()) continue;
                String response = player.getBetResponse();
                if (response == null) continue;
                try {
                    int bet = DecryptJson.getBet(player.getMoney(), response);
                    player.setBet(bet);
                    player.clearBetResponse();
                    player.setIsChoosingBet(false);
                    player.removeBet();
                } catch (InvalidBet | JsonProcessingException e) {
                    player.clearBetResponse();
                    player.getPlayerManager().sendMessage(
                            GenerateJson.generateGeneralMessage("Invalid bet. Please enter a valid amount."));
                }
            }
            sleep(500);
        }

        for (Player player : players) {
            if (player.getIsChoosingBet()) {
                player.setIsChoosingBet(false);
                kickPlayer(player, "You were removed for not placing a bet within " + BET_TIMEOUT_SECONDS + " seconds.");
            }
        }
    }

    /**
     * Sends a reason message, removes the player from the game, notifies others, then disconnects them.
     */
    public void kickPlayer(Player player, String reason) {
        player.getPlayerManager().sendMessage(GenerateJson.generateGeneralMessage(reason));
        removePlayer(player);
        broadcastToAllPlayers(GenerateJson.generateGeneralMessage(player.getName() + " was removed for being AFK."));
        player.getPlayerManager().disconnect();
    }

    /**
     * Adds a player to the game. Routes to pendingPlayers during an active round.
     * @param player --> the player to add to the game.
     */
    public static synchronized void addPlayer(Player player) {
        if (roundInProgress) {
            pendingPlayers.add(player);
            player.getPlayerManager().sendMessage(
                GenerateJson.generateGeneralMessage("A round is already in progress. You'll join at the start of the next round."));
        } else {
            players.add(player);
        }
    }

    /**
     * Removes a player from the game (active or pending).
     * @param player --> the player to remove from the game.
     */
    public void removePlayer(Player player) {
        players.remove(player);
        synchronized (Play.class) {
            pendingPlayers.remove(player);
        }
    }

    /**
     * Deals the starting cards one at a time, broadcasting after each card so the
     * frontend can animate them arriving.
     */
    private void dealInitialCards() {
        for (int i = 0; i < 2; i++) {
            for (Player player : players) {
                if (player.getCardsInHand().getCards().size() < 2) {
                    player.getCardsInHand().addCard(deck.deal());
                    broadcastToAllPlayers(GenerateJson.generateUpdate(this, true));
                    sleep(400);
                }
            }
            if (dealer.getCardsInHand().getCards().size() < 2) {
                dealer.getCardsInHand().addCard(deck.deal());
                broadcastToAllPlayers(GenerateJson.generateUpdate(this, true));
                sleep(400);
            }
        }
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Handles the dealers turn.
     */
    public void dealerTurn() {
        sleep(600);
        broadcastToAllPlayers(GenerateJson.generateUpdate(this, false));
        broadcastToAllPlayers(GenerateJson.generateGeneralMessage("Dealer's turn."));
        broadcastToAllPlayers(GenerateJson.generateGeneralMessage("Initial cards: " + dealer.toString()));
        while (dealer.getCardsInHand().getValue() < 17) {
            sleep(1000);
            dealer.getCardsInHand().addCard(deck.deal());
            broadcastToAllPlayers(GenerateJson.generateUpdate(this, false));
            broadcastToAllPlayers(GenerateJson.generateGeneralMessage(dealer.toString()));
        }
        int finalValue = dealer.getCardsInHand().getValue();
        String finalHand = formatCards(dealer.getCardsInHand().getCards());
        if (finalValue > 21) {
            dealer.setBust(true);
            broadcastToAllPlayers(GenerateJson.generateGeneralMessage(
                "Dealer busts!  " + finalHand + "  [" + finalValue + "]"));
        } else {
            broadcastToAllPlayers(GenerateJson.generateGeneralMessage(
                "Dealer stands on " + finalValue + ".  " + finalHand));
        }
    }

    private void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private String formatCards(List<Card> cards) {
        StringBuilder sb = new StringBuilder();
        for (Card c : cards) {
            if (sb.length() > 0) sb.append("  ");
            sb.append(c);
        }
        return sb.toString();
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
        // Blackjack, bust, and surrender are already resolved during the player's turn.
        if (hand.getState() instanceof BlackJack
                || hand.getState() instanceof Bust
                || hand.getState() instanceof Surrender) {
            return;
        }
        if (dealer.getBust()) {
            player.winBet();
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
        roundInProgress = false;
        broadcastToAllPlayers(GenerateJson.generateGeneralMessage("\n═══════════════════════════════════════\n           Round complete!\n═══════════════════════════════════════"));
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
