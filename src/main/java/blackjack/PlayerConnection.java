package blackjack;

public interface PlayerConnection {
    void sendMessage(String message);
    void disconnect();
}
