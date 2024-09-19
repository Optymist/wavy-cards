package blackjack.Client.Gui.MainMenu;

import blackjack.Client.Gui.Frames.GameFrame;
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

    public MainMenu(final GameFrame frame) {
        super(frame);
        this.window = frame;
        this.setBackgroundColor(Color.GRAY);
        this.setLayout(new GridBagLayout());
    }


	@Override
	public void packPanel() {
        TitlePanel titlePanel = new TitlePanel("WavyCards");
        Button createGameButton = new Button("Create Game");
        Button JoinGameButton = new Button("Join Game");
        Button exitGameButton = new Button("ExitGame");

        GridBagConstraints constraints = new GridBagConstraints();
        this.add(titlePanel, constraints);






	}

    
}
