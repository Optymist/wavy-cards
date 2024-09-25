package blackjack.Client.Gui.Panels.JoinGamePanels.Buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import blackjack.Client.Gui.Frames.GameFrame;
import blackjack.Client.Gui.MainMenu.MainMenu;
import blackjack.Client.Gui.Panels.JoinGamePanels.JoinGamePanel;

/**
 * JoinGameButtonListener
 */
public class JoinGameButton implements ActionListener {

    private GameFrame window;

    public JoinGameButton(GameFrame mainFrame) {
        super();
        this.window = mainFrame;
    }

	@Override
	public void actionPerformed(ActionEvent arg0) {
        // TODO Attempt to join game and if no game is found then proceed to 
        // let the user know that the game cannot be found

        // this.window.removePanel();
        // this.window.setPanel(new MainMenu(window));
	}

    
}
