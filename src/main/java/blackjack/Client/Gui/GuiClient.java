package blackjack.Client.Gui;

import blackjack.Client.ClientThread;
import blackjack.Client.Gui.Frames.GameFrame;
import blackjack.Client.Gui.MainMenu.MainMenu;

/**
 * GuiClient
 */
public class GuiClient {

    private static ClientThread CLIENT_THREAD;
    private static Thread GUI_THREAD;

    // private GameFrame window;

    // public GuiClient() {
    // this.window = new GameFrame();
    // this.window.setPanel(new MainMenu(window));
    // }

    // public GameFrame getFrame() {
    // return this.window;
    // }

    public static void main(String[] args) {
        GUI_THREAD = new Thread(() -> {
            GameFrame frame = new GameFrame();

            MainMenu menu = new MainMenu(frame);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.setPanel(menu);
        });
        GUI_THREAD.start();

    }

    public static void setClientThread(ClientThread client){
        GuiClient.CLIENT_THREAD = client;
    }

    public static ClientThread getClientThread() {
        return CLIENT_THREAD;
    }

    public static void startClientThread() {
        CLIENT_THREAD.start();
    }

}
