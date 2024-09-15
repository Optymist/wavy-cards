package blackjack.Client;

import java.io.*;
import java.net.*;
import java.util.*;

import blackjack.MultiserverManager;

public class Client {
    private static final String SERVER_IP = MultiserverManager.IP;
    private static final int PORT = MultiserverManager.PORT;

    private Socket socket;
    private BufferedWriter out;
    private BufferedReader in;

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

                    System.out.println(serverMessage);



                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    closeEverything(socket, in, out);
                    break;
                }
            }
        }).start();
    }

    public void sendRequest(Scanner scanner) {
        while (socket.isConnected()) {
            String messageToSend = scanner.nextLine();

            sendMessage(messageToSend);
        }
    }

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
