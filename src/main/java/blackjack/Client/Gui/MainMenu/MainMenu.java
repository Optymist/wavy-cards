package blackjack.Client.Gui.MainMenu;

import blackjack.Client.Gui.Frames.GameFrame;
import blackjack.Client.Gui.MainMenu.Buttons.ExitButtonListener;
import blackjack.Client.Gui.MainMenu.Buttons.JoinGameButtonListener;
import blackjack.Client.Gui.Panels.GamePanel;
import blackjack.Client.Gui.Panels.TitlePanel;

import java.awt.Button;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

/**
 * MainMenu
 */
public class MainMenu extends GamePanel {

    private GameFrame window;

    public MainMenu(GameFrame frame) {
        super(frame);
        this.window = frame;
        this.setBackgroundColor(Color.GRAY);
        this.setLayout(new GridBagLayout());
    }


	@Override
	public void packPanel() {
        TitlePanel titlePanel = new TitlePanel("WavyCards");
        Button createGameButton = new Button("Create Game");
        Button joinGameButton = new Button("Join Game");
        Button exitGameButton = new Button("ExitGame");

        joinGameButton.addActionListener(new JoinGameButtonListener(window));
        exitGameButton.addActionListener(new ExitButtonListener(window));

        GridBagConstraints constraints = new GridBagConstraints();
        this.add(titlePanel, constraints);
        this.add(createGameButton, constraints);
        this.add(joinGameButton, constraints);
        this.add(exitGameButton, constraints);






	}

    
}
