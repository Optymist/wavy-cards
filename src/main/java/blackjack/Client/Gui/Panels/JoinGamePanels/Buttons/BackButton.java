package blackjack.Client.Gui.Panels.JoinGamePanels.Buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import blackjack.Client.Gui.Frames.GameFrame;
import blackjack.Client.Gui.MainMenu.MainMenu;
import blackjack.Client.Gui.Panels.JoinGamePanels.JoinGamePanel;

/**
 * JoinGameButtonListener
 */
public class BackButton implements ActionListener {

    private GameFrame window;

    public BackButton(GameFrame mainFrame) {
        super();
        this.window = mainFrame;
    }

	@Override
	public void actionPerformed(ActionEvent arg0) {
        this.window.removePanel();
        this.window.setPanel(new MainMenu(window));
	}

    
}
