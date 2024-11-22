package blackjack.Client.Gui.Panels.JoinGamePanels;

import java.awt.Button;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.TextField;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import blackjack.Client.Exceptions.UnsuccessfulConnectionException;
import blackjack.Client.Gui.Frames.GameFrame;
import blackjack.Client.Gui.MainMenu.Buttons.JoinGameButtonListener;
import blackjack.Client.Gui.Panels.GamePanel;
import blackjack.Client.Gui.Panels.MenuPanel;
import blackjack.Client.Gui.Panels.JoinGamePanels.Buttons.BackButton;

/**
 * JoinGamePanel
 */
public class JoinGamePanel extends MenuPanel {

    private GameFrame window;
    private TextField ipField;
    private TextField portField;

    private List<Component> comps;

    public JoinGamePanel(GameFrame mainFrame) {
        super(mainFrame);
        this.window = mainFrame;
        comps = new ArrayList<>();
        createIpField();
        createSocketField();
        createJoinGameButton();
        createBackButton();
    }

    private TextField createIpField() {
        ipField = new TextField();
        ipField.setText("localhost");
        ipField.setSize(200, 200);
        comps.add(ipField);
        return ipField;
    }

    private TextField createSocketField() {
        portField = new TextField();
        portField.setText("2345");
        portField.setSize(200, 200);
        comps.add(portField);
        return portField;
    }

    private Button createJoinGameButton() {
        Button button = new Button();
        button.setLabel("Join Game");
        button.addActionListener(new JoinGameButtonListener(window));
        comps.add(button);
        return button;
    }

    private Button createBackButton() {
        Button button = new Button();
        button.setLabel("Back Button");
        button.addActionListener(new BackButton(window));
        comps.add(button);
        return button;
    }

    public Socket connectToHost() throws UnsuccessfulConnectionException {

        String host = getHost();
        int port = getPort();

        try {

            Socket socket = new Socket(host, port);
            return socket;

        } catch (IOException e) {

            throw new UnsuccessfulConnectionException(
                    String.format("Unable to connect to host %s with port %d", host, port));
        }

    }

    public String getHost() {
        return ipField.getText();
    }

    public int getPort() {
        return Integer.parseInt(portField.getText());
    }

    public List<Component> comps() {
        return this.comps;
    }

    @Override
    public void init() {
        comps.forEach(comp -> this.add(comp));
    }

    @Override
    public void dispose() {

    }

}
