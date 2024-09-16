package blackjack;

import java.net.SocketException;
import java.util.Scanner;

import blackjack.Client.Client;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;


public class MultiserverManager {
    public static String IP;
    public static int PORT;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        mainMenu();
        String choice = scanner.nextLine();

        switch(choice) {
            case("1"):
                System.out.println("How many players would you like to host: ");
                String players = scanner.nextLine();

                while (!players.matches("[1-6]+")) {
                    System.out.println("Not a valid input.");
                    System.out.println("Enter a number from 1 to 6:");
                    players = scanner.nextLine();
                }
                setupServer(scanner);
                System.out.println("Game creation in progress...");
                loadingScreenMessage();
                Server.main(new String[]{players});
                break;
            case("2"):
                joinServer(scanner);
                if (IP.isEmpty()) return;
                System.out.println("Attempting to join game hosted by " + IP + " at port " + PORT + ".");
                Client.main(new String[]{});
                break;
            default:
                System.out.println("Not a valid choice. Try again.");
                main(new String[]{});
        }

        scanner.close();
    }

    private static void mainMenu() {
        System.out.println("Let's Play Some BlackJack!");
        System.out.println("Choose an option [1/2]: ");
        System.out.println("1. Create a game");
        System.out.println("2. Join a game");
    }

    private static void loadingScreenMessage(){
        System.out.println("Share these with the people you'd like to play with: ");
        System.out.println("Server IP address: "+ IP);
        System.out.println("Server PORT number: "+ PORT);
    }

    private static void setupServer(Scanner scanner) {
        try {
            IP = getIp();
        } catch (SocketException e) {
            e.getMessage();
        }
        PORT = getPort(scanner, "Enter the port number you would like to use for your server (0 for the default): ");
    }

    /**
     * Attempt to get the IP address of the user trying to create a game.
     * @return String message --> either IP or error message
     * @throws SocketException
     */
    private static String getIp() throws SocketException {
        String message;

        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof java.net.Inet4Address) {
                        message = inetAddress.getHostAddress();
                        return message;  // Return the first non-loopback IPv4 address
                    }
                }
            }
            message = "No suitable network interface found.";
        } catch (SocketException e) {
            message = e.getMessage();
        }
        return message;
    }

    private static int getPort(Scanner scanner, String prompt) {
        int value;
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                value = Integer.parseInt(input);
                if (value == 0){
                    return 2345;
                }

                if (value >= 2000 && value <= 65535) {
                    break;
                } else {
                    System.out.println("Port number must be between " + 2000 + " and " + 65535 + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, enter a numeric value.");
            }
        }
        return value;
    }

    private static void joinServer(Scanner scanner){
        System.out.print("\nEnter the IP address of the server you want to join: (leave empty to use localhost): ");
        String ipAddress = scanner.nextLine().trim().strip();
        if (ipAddress.isEmpty()) {
            IP = "localhost";
        } else if((ipAddress.split("\\.").length != 4)){
            System.out.println("Invalid ip address");
            IP = "";
            return;
        } else {
            IP = ipAddress;
        }

        System.out.print("Enter the PORT number of the server you want to join: ");
        PORT = Integer.parseInt(scanner.nextLine().trim().strip());

    }


}
