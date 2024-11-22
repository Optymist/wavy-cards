package blackjack.Client.Gui.Panels.JoinGamePanels.Buttons;

import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.fasterxml.jackson.core.JsonProcessingException;

import blackjack.Client.ClientThread;
import blackjack.Client.Gui.Frames.GameFrame;
import blackjack.Client.Gui.Panels.JoinGamePanels.CreateNamePanel;
import blackjack.Client.Gui.Panels.JoinGamePanels.PlayGamePanel;
import blackjack.protocol.DecryptJson;

/**
 * CreateNameButton
 * Gui Name Validation
 */
public class CreateNameButton implements ActionListener {

    private ClientThread client;
    private GameFrame window;

    private TextField nameField;
    private CreateNamePanel panel;
    private int currentCount;


    public CreateNameButton(ClientThread client, GameFrame window) {
        super();
        this.window = window;
        this.client = client;
        this.panel = (CreateNamePanel) window.currentPanel();
        this.currentCount = client.getResponseCount();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        client.notifyServer(panel.getName());

        if (currentCount < client.getResponseCount()) {
            try {
                if (DecryptJson.isConnectedToGame(client.lastMessage())) {
                    this.window.replaceCurrentPanel(new PlayGamePanel(window, client));
                } else {
                    panel.add(
                            new Label("Name Already Taken"));
                }

            } catch (JsonProcessingException e1) {
                e1.printStackTrace();
            }
        }
    }

}
