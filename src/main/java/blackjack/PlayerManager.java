package blackjack;

import blackjack.player.Player;
import blackjack.protocol.GenerateJson;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class PlayerManager implements Runnable {
    public static ArrayList<PlayerManager> players = new ArrayList<>();
    private int maxPlayers;
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private String name;
    private Player player;
    private final Play game;

    public PlayerManager(Socket socket, int maxPlayers) {
        game = Server.getGame();
        try {
            this.socket = socket;
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.maxPlayers = maxPlayers;
            players.add(this);
        } catch (IOException e) {
            System.out.println("Failed to start player manager.");
            closeEverything(socket, in, out);
        }
    }

    @Override
    public void run() {
        String clientMessage;

        try {
            if (players.size() > maxPlayers) {
                sendMessage(GenerateJson.generateGeneralMessage("Table full."));
                closeEverything(socket, in, out);
            }

            chooseName();

            chooseBet();

            while (true) {

                if (Server.getGameOn() && allPlayersChosenBet()) {
                    new Thread(game).start();
                }

                while ((clientMessage = in.readLine()) != null && !game.allComplete()) {
                    if (player.isTurn()) {
                        System.out.println(clientMessage);
                        player.setTurnResponse(clientMessage);
                    }
                }

                if (clientMessage == null) {
                    removeClient();
                    break;
                }

                broadcastMessage(GenerateJson.generateGeneralMessage("All players completed.\n"));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to read input in the player manager.");
        } finally {
            closeEverything(socket, in, out); // Ensure resources are closed and player is removed
        }
    }

    private void chooseBet() {
        sendMessage(GenerateJson.generateGeneralMessage("Your starting amount is $2500. Please choose a bet amount: "));

        try {
            String clientMessage = in.readLine();
            int bet = Integer.parseInt(clientMessage);
            player.setBet(bet);
            sendMessage(GenerateJson.generateGeneralMessage("Your bet is $" + bet + "."));
        } catch (IOException e){
            System.out.println("An error occurred.");
        } catch (NumberFormatException n) {
            System.out.println("Please choose a valid number.");
            chooseBet();
        }
    }

    public void chooseName() {
        sendMessage(GenerateJson.generateGeneralMessage("Wanna play some BlackJack? \nPick a name first: "));

        try {
            String clientMessage = in.readLine();

            if (clientMessage == null) { // Client disconnected
                removeClient();
            }

            while (!validateName(clientMessage)) {
                sendMessage(GenerateJson.generateGeneralMessage("Name already taken. Pick again: "));
                clientMessage = in.readLine();
                if (clientMessage == null) { // Client disconnected
                    removeClient();
                }
            }

            if (clientMessage == null) { // Client disconnected
                removeClient();
            }

            name = clientMessage;
            player = new Player(name, this);
            sendMessage(GenerateJson.generateConnectedUpdate(player));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            closeEverything(socket, in, out);
        }
    }

    public static boolean validateName(String requestedName) {
        for (PlayerManager player : players) {
            if (!(player.name == null) && player.name.equals(requestedName)) {
                return false;
            }
        }
        return true;
    }

    public void removeClient() {
        players.remove(this);
        if (!(game == null)) {
            game.removePlayer(player);
        }
        System.out.println(name + " has left the game.");
    }

    public void broadcastMessage(String message) {
        for (PlayerManager player : players) {
            try {
                if (socket.isConnected() && !player.name.equals(name)) {
                    player.out.write(message);
                    player.out.newLine();
                    player.out.flush();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.out.println("Failed to write message out.");
            }
        }
    }

    private boolean allPlayersChosenBet() {
        List<Player> players = game.getPlayers();

        for (Player player : players) {
            if (player.getBet() == 0.0) {
                return false;
            }
        }
        return true;
    }

    public void sendMessage(String message) {
        try {
            out.write(message);
            out.newLine();
            out.flush();
        } catch (IOException i) {
            System.out.println("There was an issue with delivering the output.");
        }
    }

    public void closeEverything(Socket socket, BufferedReader in, BufferedWriter out) {
        removeClient();
        try {
            if (in != null)
                in.close();
            if (socket != null)
                socket.close();
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<PlayerManager> getPlayers() {
        return players;
    }
}
