package blackjack;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private static ServerSocket serverSocket;
    private static Play game;
    private static boolean gameOn = false;
    private static int playerNum;
    private static final int PORT = MultiserverManager.PORT;

    public Server(ServerSocket socket) {
        serverSocket = socket;
    }

    public static void main(String[] args) {
        playerNum = Integer.parseInt(args[0]);
        game = new Play(playerNum);
        try {
            ServerSocket socket = new ServerSocket(PORT);
            Server server = new Server(socket);

            Thread serverThread = new Thread(server::start);
            serverThread.start();
            Thread consoleThread = new Thread(Server::serverConsole);
            consoleThread.start();

            serverThread.join();
            consoleThread.join();

        } catch (InterruptedException | IOException i) {
            System.out.println(i.getMessage());
            System.out.println("Failed to start server threads.");
            closeServerSocket();
        }
    }

    public static void serverConsole() {
        String endServerPrompts = "quit off bye";
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("Enter a server command: ");
            String serverCommand = scanner.nextLine().toLowerCase();
            if (endServerPrompts.contains(serverCommand)) {
                System.out.println("Shutting down...");
                System.exit(0);
                break;
            }
            else {
                System.out.println(serverCommand);
            }
        }
    }

    public static void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException i) {
            System.out.println(i.getMessage());
            System.out.println("Failed to close the server socket.");
        }
    }

    public void start() {
        try {

            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                PlayerManager playerManager = new PlayerManager(socket, playerNum);
                Thread handlerThread = new Thread(playerManager);
                handlerThread.start();
                if (playerNum == playerManager.getPlayers().size()) {
                    gameOn = true;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to accept a connection to the socket.");
        }
    }

    public static boolean getGameOn() {
        return gameOn;
    }

    public static Play getGame() {
        return game;
    }
}
