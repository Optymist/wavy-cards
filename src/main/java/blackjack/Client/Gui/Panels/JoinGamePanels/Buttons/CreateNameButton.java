package blackjack.Client.Gui.Panels.JoinGamePanels.Buttons;

import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.fasterxml.jackson.core.JsonProcessingException;

import blackjack.Client.ClientThread;
import blackjack.Client.Gui.GuiClient;
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


    public CreateNameButton(GameFrame window) {
        super();
        this.window = window;
        this.client = GuiClient.getClientThread();
        this.panel = (CreateNamePanel) window.currentPanel();
        this.currentCount = client.getResponseCount();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(panel.getName());
        client.notifyServer(panel.getName());

        if (currentCount < client.getResponseCount()) {
            try {
                System.out.println(client.lastMessage());
                if (DecryptJson.isConnectedToGame(client.lastMessage())) {
                    this.window.replaceCurrentPanel(new PlayGamePanel(window));
                } else {
                    // TODO check that this works
                    panel.add(
                            new Label("Name Already Taken"));
                    window.pack();
                }

            } catch (JsonProcessingException e1) {
                e1.printStackTrace();
            }
        }
    }

}
