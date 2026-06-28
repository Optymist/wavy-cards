package blackjack;

import blackjack.player.Player;
import blackjack.protocol.GenerateJson;
import org.java_websocket.WebSocket;

public class WebSocketClientHandler implements PlayerConnection {
    private final WebSocket conn;
    private final Play game;
    private Player player = null;
    private boolean hasChosenName = false;

    public WebSocketClientHandler(WebSocket conn, Play game) {
        this.conn = conn;
        this.game = game;
    }

    public void onOpen() {
        sendMessage(GenerateJson.generateGeneralMessage("Wanna play some BlackJack? \nPick a name first: "));
    }

    public void onMessage(String message) {
        if (!hasChosenName) {
            handleNameChoice(message.trim());
            return;
        }
        if (player == null) return;
        if (player.isTurn()) {
            player.setTurnResponse(message);
        } else if (player.getIsChoosingBet()) {
            player.setBetResponse(message);
        }
    }

    public void onClose() {
        if (player != null) {
            game.removePlayer(player);
            PlayerManager.takenNames.remove(player.getName());
            System.out.println(player.getName() + " (WS) has left the game.");
        }
    }

    private void handleNameChoice(String name) {
        if (!PlayerManager.validateName(name)) {
            sendMessage(GenerateJson.generateGeneralMessage("Name already taken. Pick again: "));
            return;
        }
        hasChosenName = true;
        PlayerManager.takenNames.add(name);
        player = new Player(name, this);
        sendMessage(GenerateJson.generateConnectedUpdate(player));

        synchronized (Server.class) {
            if (!Server.getGameOn()) {
                Server.setGameOn(true);
                new Thread(game).start();
            }
        }
    }

    @Override
    public void sendMessage(String message) {
        if (conn.isOpen()) {
            conn.send(message);
        }
    }
}
