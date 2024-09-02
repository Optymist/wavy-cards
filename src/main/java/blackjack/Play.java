package blackjack;

import java.util.ArrayList;
import java.util.List;

import blackjack.deck.Deck;
import blackjack.player.Dealer;
import blackjack.player.Player;

public class Play implements Runnable {
    public static List<Player> players = new ArrayList<>();
    private volatile boolean running = true;
    private static Dealer dealer;
    private static int numPlayers;
    private static Deck deck;

    public Play(int maxPlayers) {
        dealer = new Dealer();
        numPlayers = maxPlayers;
        setupDeck();
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
            System.out.println("Game encountered an error: " + e.getMessage());
        }
    }

    public void startGame() {
        dealInitialCards();
        for (Player player : players) {
            player.calculateCards();
            player.getPlayerManager().sendMessage(player.toString());
            System.out.println(player);
            player.getPlayerManager().sendMessage("What do you want to do? ");
        }
        System.out.println(dealer);
    }

    public void round(Player player) {
        if (player.getHandValue() > 21) {
            player.getPlayerManager().sendMessage("Bust!");
            player.setBust(true);
            player.setStanding(true);
        }
        if (player.getHandValue() == 21 || player.isStanding()) {
            player.getPlayerManager().sendMessage("Standing...");
            player.setStanding(true);
        } else {
            player.getPlayerManager().sendMessage("What do you want to do? ");
        }
    }

    public boolean allStanding() {
        for (Player player : players) {
            if (!player.isStanding()) {
                return false;
            }
        }
        return true;
    }

    public synchronized void handlePlayerMessage(Player player, String message) {
        System.out.println("Received message from player " + player.getName() + ": " + message);
        player.getPlayerManager().sendMessage("Acknowledged: " + message);
        player.performAction(message, this);
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
                player.addCardToHand(deck.deal());
            }
            dealer.addCardToHand(deck.deal());
        }
    }

    public void dealCardToPlayer(Player player) {
        player.addCardToHand(deck.deal());
    }

    public void stopGame() {
        running = false;
    }

    public void broadcastToAllPlayers(String message) {
        for (Player player : players) {
            player.getPlayerManager().sendMessage(message);
        }
    }


}
