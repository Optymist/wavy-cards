package blackjack.Client.Gui;

import java.net.Socket;

import blackjack.Client.Client;
import blackjack.Client.Gui.Frames.GameFrame;
import blackjack.Client.Gui.MainMenu.MainMenu;

/**
 * GuiClient
 */
public class GuiClient {

    // private GameFrame window;

    // public GuiClient() {
    // this.window = new GameFrame();
    // this.window.setPanel(new MainMenu(window));
    // }

    // public GameFrame getFrame() {
    // return this.window;
    // }

    public static void main(String[] args) {
        GameFrame frame = new GameFrame();

        MainMenu menu = new MainMenu(frame);
        frame.setPanel(menu);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
