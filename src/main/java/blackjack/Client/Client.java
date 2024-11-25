package blackjack.Client;

import java.io.*;
import java.net.*;
import java.util.*;

import blackjack.Client.protocol.Generate;
import blackjack.MultiserverManager;
import com.fasterxml.jackson.databind.JsonNode;

import static blackjack.Client.protocol.Decrypt.*;

/**
 * The Client class handles communication with a Blackjack game server.
 * It connects via a socket, processes server messages, and sends appropriate responses.
 */
public class Client {
    private static final String SERVER_IP = MultiserverManager.IP;
    private static final int PORT = MultiserverManager.PORT;

    private Socket socket;
    private BufferedWriter out;
    private BufferedReader in;
    private String name;
    private String serverRequestType;

    /**
     * Constructor for the Client class.
     *
     * @param socket the socket used to connect to the server.
     */
    public Client(Socket socket) {
        try {
            this.socket = socket;
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            closeEverything(socket, in, out);
        }
    }

    /**
     * The entry point of the client application.
     * Connects to the server, starts listening for responses, and handles user input.
     *
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            Socket socket = new Socket(SERVER_IP, PORT);
            Client client = new Client(socket);

            client.handleResponse();
            client.sendRequest(scanner);

            scanner.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to find server.");
        }
    }

    /**
     * Continuously listens for server responses on a separate thread.
     * Parses messages and takes appropriate actions based on the server's protocol.
     */
    public void handleResponse() {
        new Thread(() -> {
            while (socket.isConnected()) {
                try {
                    String serverMessage = in.readLine();

                    if (serverMessage == null) {
                        System.out.println("Connection to server lost.");
                        closeEverything(socket, in, out);
                        break;
                    }

                    JsonNode messageNode = decryptServerMessage(serverMessage);

                    switch (messageNode.get("protocolType").asText()) {
                        case ("general"):
                            serverRequestType = "general";
                            System.out.println(messageNode.get("message").asText());
                            break;
                        case ("connectedUpdate"):
                            serverRequestType = "connectedUpdate";
                            this.name = messageNode.get("playerName").asText();
                            System.out.println(messageNode.get("message").asText());
                            break;
                        case ("turnRequest"):
                            serverRequestType = "turnRequest";
                            System.out.println(getAvailableActions(messageNode));
                            break;
                        case ("update"):
                            serverRequestType = "update";
                            System.out.println(getPlayerCardInfo(messageNode, name));
                            break;
                        case ("betRequest"):
                            serverRequestType = "betRequest";
                            System.out.println(messageNode.get("message").asText());
                            break;
                        case ("lobbyUpdate"):
                            serverRequestType = "lobbyUpdate";
                            break;
                        default:
                            serverRequestType = "unknown";
                            System.out.println("Unknown protocol type.");
                            break;
                    }


                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    closeEverything(socket, in, out);
                    break;
                }
            }
        }).start();
    }

    /**
     * Reads user input from the console and sends appropriate requests to the server.
     *
     * @param scanner a Scanner object for reading user input.
     */
    public void sendRequest(Scanner scanner) {
        while (socket.isConnected()) {
            String messageToSend = scanner.nextLine();

            if (serverRequestType.equals("turnRequest")) {
                sendMessage(Generate.generateTurnResponse(name, messageToSend));
            } else if (serverRequestType.equals("betRequest")) {
                checkBet(messageToSend);
            } else {
                sendMessage(messageToSend);
            }
        }
    }

    /**
     * Validates and sends a betting request to the server.
     *
     * @param bet the betting amount entered by the user.
     */
    private void checkBet(String bet) {
        try {
            int betValue = Integer.parseInt(bet);
            sendMessage(Generate.generateBetResponse(name, betValue));
        } catch (NumberFormatException e) {
            sendMessage(Generate.generateBetResponse(name, null));
        }
    }

    /**
     * Sends a raw message to the server.
     *
     * @param message the message to send.
     */
    private void sendMessage(String message) {
        try {
            out.write(message);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println();
        }
    }

    /**
     * Closes the socket and associated I/O streams.
     * Ensures all resources are released and terminates the client.
     *
     * @param socket the socket to close.
     * @param in     the BufferedReader to close.
     * @param out    the BufferedWriter to close.
     */
    private void closeEverything(Socket socket, BufferedReader in, BufferedWriter out) {
        try {
            socket.close();
            in.close();
            out.close();
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Couldn't close the connection.");
        }
    }
}
