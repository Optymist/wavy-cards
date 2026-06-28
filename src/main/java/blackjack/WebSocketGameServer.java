package blackjack;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class WebSocketGameServer extends WebSocketServer {
    private final Play game;
    private final Map<WebSocket, WebSocketClientHandler> clients = new ConcurrentHashMap<>();

    public WebSocketGameServer(InetSocketAddress address, Play game) {
        super(address);
        this.game = game;
        setReuseAddr(true);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        WebSocketClientHandler handler = new WebSocketClientHandler(conn, game);
        clients.put(conn, handler);
        handler.onOpen();
        System.out.println("WebSocket client connected: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        WebSocketClientHandler handler = clients.get(conn);
        if (handler != null) handler.onMessage(message);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        WebSocketClientHandler handler = clients.remove(conn);
        if (handler != null) handler.onClose();
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.out.println("WebSocket error on " + (conn != null ? conn.getRemoteSocketAddress() : "unknown") + ": " + ex.getMessage());
    }

    @Override
    public void onStart() {
        System.out.println("WebSocket game server started.");
    }
}
