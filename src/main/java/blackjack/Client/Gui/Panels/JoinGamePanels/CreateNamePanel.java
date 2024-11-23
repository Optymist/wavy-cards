package blackjack.Client.Gui.Panels.JoinGamePanels;

import blackjack.Client.ClientThread;
import blackjack.Client.Gui.GuiClient;
import blackjack.Client.Gui.Frames.GameFrame;
import blackjack.Client.Gui.Panels.GamePanel;
import blackjack.Client.Gui.Panels.MenuPanel;
import blackjack.Client.Gui.Panels.JoinGamePanels.Buttons.CreateNameButton;

import java.awt.TextField;

import javax.swing.JPanel;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Label;

/**
 * CreateNamePanel
 */
public class CreateNamePanel extends MenuPanel {

    private Label createNameLabel;
    private TextField nameField;
    private Button createNameButton;

    private ClientThread client;

    public CreateNamePanel(GameFrame window) {
        super(window);
        this.client = GuiClient.getClientThread();

        System.out.println("Initialized CreateNamePanel");
    }

    public void createNameLabel() {
        this.createNameLabel = new Label("Enter Name:");
        this.createNameLabel.setSize(new Dimension(100, 200));
    }

    public void createNameField() {
        this.nameField = new TextField();
        this.nameField.setText("Name");
        this.nameField.setSize(200, 200);
    }

    public void createNameButton() {
        this.createNameButton = new Button();
        this.createNameButton.setLabel("Join Game");
        this.createNameButton.setSize(200, 200);
        // this.createNameButton.setLocation(600, 800);
        this.createNameButton.addActionListener(new CreateNameButton(window));
    }

    public void init() {
        this.createNameLabel();
        this.createNameField();
        this.createNameButton();
        // this.setBackground(Color.GREEN);

        GridBagConstraints constraints = new GridBagConstraints();
        this.add(createNameButton, constraints);
        this.add(nameField, constraints);
        this.add(createNameButton, constraints);

        System.out.println("Added to CreateNamePanel");
    }

    @Override
    public void dispose() {

    }

    public String getName() {
        return nameField.getText();
    }

    
}
