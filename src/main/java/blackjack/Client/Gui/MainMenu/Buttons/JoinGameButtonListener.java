package blackjack.Client.Gui.MainMenu.Buttons;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

import blackjack.Client.ClientThread;
import blackjack.Client.Exceptions.UnsuccessfulConnectionException;
import blackjack.Client.Gui.GuiClient;
import blackjack.Client.Gui.Frames.GameFrame;
import blackjack.Client.Gui.Panels.LobbyPanel;
import blackjack.Client.Gui.Panels.JoinGamePanels.CreateNamePanel;
import blackjack.Client.Gui.Panels.JoinGamePanels.JoinGamePanel;
import blackjack.Client.Gui.Panels.JoinGamePanels.PlayGamePanel;

/**
 * JoinGameButtonListener
 */
public class JoinGameButtonListener implements ActionListener {

    private GameFrame window;
    private JoinGamePanel joinPanel;

    public JoinGameButtonListener(GameFrame mainFrame) {
        super();
        this.window = mainFrame;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {

        joinPanel = (JoinGamePanel) window.currentPanel();

        try {

            Socket connectionSocket = joinPanel.connectToHost();

            ClientThread clientThread = new ClientThread(connectionSocket);
            GuiClient.setClientThread(clientThread);
            GuiClient.startClientThread();
            // LobbyPanel playPanel = new LobbyPanel(window, clientThread);

            this.window.replaceCurrentPanel(new CreateNamePanel(window));
            // this.window.setPanel(playPanel);

        } catch (UnsuccessfulConnectionException e) {

            joinPanel.comps().add(
                    new Label(e.getMessage()));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
