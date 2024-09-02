package blackjack;

import blackjack.player.Player;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

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
            while (name == null) {
                if (players.size() > maxPlayers) {
                    sendMessage("Table full.");
                    break;
                }

                sendMessage("Wanna play some BlackJack? \nPick a name first: ");
                clientMessage = in.readLine();

                if (clientMessage == null) { // Client disconnected
                    break;
                }

                while (!validateName(clientMessage)) {
                    sendMessage("Name already taken. Pick again: ");
                    clientMessage = in.readLine();
                    if (clientMessage == null) { // Client disconnected
                        break;
                    }
                }

                if (clientMessage == null) { // Client disconnected
                    break;
                }

                name = clientMessage;
                player = new Player(name, this);
                sendMessage("Welcome " + name + "!");

                if (Server.getGameOn()) {
                    new Thread(game).start();
                }

                while ((clientMessage = in.readLine()) != null || !game.allStanding()) {
                    game.handlePlayerMessage(player, clientMessage);
                    game.round(player);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to read input in the player manager.");
        } finally {
            closeEverything(socket, in, out); // Ensure resources are closed and player is removed
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
        if (!(game==null)) {
            game.removePlayer(player);
        }
        broadcastMessage(name + " has left the game.");
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
        }
    }

    public ArrayList<PlayerManager> getPlayers() {
        return players;
    }
}
