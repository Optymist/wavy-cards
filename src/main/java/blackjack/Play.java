package blackjack;

import java.util.ArrayList;
import java.util.List;

import blackjack.deck.Deck;
import blackjack.player.Dealer;
import blackjack.player.Player;
import blackjack.player.state.BlackJack;
import blackjack.player.state.Normal;
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
        }
    }

    public void startGame() {
        dealInitialCards();
        for (Player player : players) {
//            player.getPlayerManager().sendMessage(player.toString()); // TODO replace with `update` response
            if (player.getHandValue() == 21) {
                player.setState(new BlackJack());
//                player.getPlayerManager().sendMessage("BlackJack!"); // TODO replace with `update` response
            }
            System.out.println(player);
        }
//        sendTurnMessages();
        System.out.println(dealer);
        while (running) {
            for (Player player : players) {
                broadcastToAllPlayers(GenerateJson.generateUpdate(this));
                player.manageTurn(this);
            }
            dealerTurn();
            stopGame();
        }
    }


    public boolean allComplete() {
        for (Player player : players) {
            if (!( player.getState() instanceof Normal )) {
                return false;
            }
        }
        return true;
    }

    public synchronized void handlePlayerMessage(Player player, String message) {
        System.out.println("Received message from player " + player.getName() + ": " + message);
        player.getPlayerManager().sendMessage("Acknowledged: " + message);
        // TODO !!!!!  I think
        // player.performAction(message, this);
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
                    player.addCardToHand(deck.deal());
                }
            }
            if (dealer.getCardsInHand().getCards().size() < 2) {
                dealer.addCardToHand(deck.deal());
            }
        }
    }

//    public static void dealCardToPlayer(Player player) {
//        player.getCardsInHand().addCard(deck.deal());
//    }

    public void moveTurn() {
        currentPlayerIndex += 1;
        if (currentPlayerIndex > (players.size()) - 1) {
            dealerTurn();
            currentPlayerIndex = 0;
        }  else {
            sendTurnMessages();
        }
    }

    public void dealerTurn() {
        // TODO needs to send json 
        broadcastToAllPlayers("Dealer's turn.");
        broadcastToAllPlayers("Initial cards: " + dealer.toString());
        while (dealer.getCardsInHand().getValue() < 17) {
            dealer.addCardToHand(deck.deal());
            broadcastToAllPlayers(dealer.toString());
        }
        if (dealer.getCardsInHand().getValue() > 21) {
            dealer.setBust(true);
            broadcastToAllPlayers("Dealer busts with " + dealer.getCardsInHand().getValue() + "!");
        } else {
            broadcastToAllPlayers("Dealer stands on " + dealer.getCardsInHand().getValue() + ".");
        }
    }

    public void stopGame() {
        running = false;
    }

    public void sendTurnMessages() {
        Player currentPlayer = players.get(currentPlayerIndex);
        currentPlayer.setTurn(true);
        broadcastExcludingCurrent(currentPlayer.getName() + "'s turn.", currentPlayer);
        currentPlayer.getPlayerManager().sendMessage("Your turn.");
    }

    public void broadcastToAllPlayers(String message) {
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

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }
}
