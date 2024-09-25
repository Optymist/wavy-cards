package blackjack.Client.Gui.MainMenu.Buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import blackjack.Client.Gui.Frames.GameFrame;
import blackjack.Client.Gui.Panels.JoinGamePanels.JoinGamePanel;

/**
 * JoinGameButtonListener
 */
public class JoinGameButtonListener implements ActionListener {

    private GameFrame window;

    public JoinGameButtonListener(GameFrame mainFrame) {
        super();
        this.window = mainFrame;
    }

	@Override
	public void actionPerformed(ActionEvent arg0) {
        this.window.removePanel();
        this.window.setPanel(new JoinGamePanel(window));
	}

    
}
