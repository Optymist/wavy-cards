package blackjack;

import blackjack.player.Player;
import blackjack.protocol.GenerateJson;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Player manager that handles the sending and receiving of messages to anf from the client.
 */
public class PlayerManager implements Runnable {
    public static ArrayList<PlayerManager> players = new ArrayList<>();
    private int maxPlayers;
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private String name;
    private Player player;
    private final Play game;

    /**
     * Initialize the player's manager.
     * @param socket --> that the client is connected to
     * @param maxPlayers --> max number of players chosen by the server admin.
     */
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

    /**
     * Run method that determines whether the table is full or not and if not,
     * It starts the game and reads input from the client.
     */
    @Override
    public void run() {
        String clientMessage;

        try {
            if (players.size() > maxPlayers) {
                sendMessage(GenerateJson.generateGeneralMessage("Table full."));
                closeEverything(socket, in, out);
            }

            chooseName();

            while (true) {
                if (Server.getGameOn()) {
                    new Thread(game).start();
                }

                while ((clientMessage = in.readLine()) != null) {
                    System.out.println(clientMessage);
                    if (player.isTurn()) {
                        System.out.println(clientMessage);
                        player.setTurnResponse(clientMessage);
                    } else if (player.getIsChoosingBet()){
                        player.setBetResponse(clientMessage);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to read input in the player manager.");
        } finally {
            closeEverything(socket, in, out); // Ensure resources are closed and player is removed
        }
    }

    /**
     * Method to validate and set the name chosen by the client.
     * Creates the instance of the player in the game.
     */
    private void chooseName() {
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

    /**
     * Check whether there is another player with the same name.
     * @param requestedName --> chosen name.
     * @return boolean --> true if it is available.
     */
    public static boolean validateName(String requestedName) {
        for (PlayerManager player : players) {
            if (!(player.name == null) && player.name.equals(requestedName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Remove the client.
     */
    public void removeClient() {
        players.remove(this);
        if (!(game == null)) {
            game.removePlayer(player);
        }
        System.out.println(name + " has left the game.");
    }

    /**
     * Write out a message to the client.
     * @param message --> Json String to send.
     */
    public void sendMessage(String message) {
        try {
            out.write(message);
            out.newLine();
            out.flush();
        } catch (IOException i) {
            System.out.println("There was an issue with delivering the output.");
        }
    }

    /**
     * Close the connection to the server.
     * @param socket --> that the client is connected to
     * @param in --> bufferedReader for input
     * @param out --> bufferedWriter for output
     */
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

    public String getName() {
        return this.name;
    }
}
