package blackjack.Client.Gui.Panels.JoinGamePanels;

import java.awt.Button;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.TextField;
import java.util.ArrayList;
import java.util.List;

import blackjack.Client.Gui.Frames.GameFrame;
import blackjack.Client.Gui.Panels.GamePanel;
import blackjack.Client.Gui.Panels.JoinGamePanels.Buttons.BackButton;
import blackjack.Client.Gui.Panels.JoinGamePanels.Buttons.JoinGameButton;

/**
 * JoinGamePanel
 */
public class JoinGamePanel extends GamePanel {

    private TextField ipTextField;
    private TextField socketTextField;
    private Button joinGameButton;
    private Button backButton;
    private GameFrame window;

    private List<Component> comps;

    public JoinGamePanel(GameFrame mainFrame) {
        super(mainFrame);
        this.window = mainFrame;
        comps = new ArrayList<>();
        ipTextField = createIpField();
        socketTextField = createSocketField();
        joinGameButton = createJoinGameButton();
        backButton = createBackButton();
    }

    private TextField createIpField() {
        TextField field = new TextField();
        field.setText("localhost");
        field.setSize(200, 200);
        comps.add(field);
        return field;
    }

    private TextField createSocketField() {
        TextField field = new TextField();
        field.setText("2345");
        field.setSize(200, 200);
        comps.add(field);
        return field;
    }

    private Button createJoinGameButton() {
        Button button = new Button();
        button.setLabel("Join Game");
        button.addActionListener(new JoinGameButton(window));
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

    @Override
    public void packPanel() {
        // comps.forEach(comp -> this.add(comp));
        this.add(ipTextField);
        this.add(socketTextField);
        this.add(joinGameButton);
        this.add(backButton);
        // this.add(ipTextField, new GridBagConstraints());
        // this.add(socketTextField, new GridBagConstraints());
    }

}
