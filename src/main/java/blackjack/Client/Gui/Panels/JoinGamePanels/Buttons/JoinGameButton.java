package blackjack.Client.Gui.Panels.JoinGamePanels.Buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

import blackjack.Client.Exceptions.UnsuccessfulConnectionException;
import blackjack.Client.Gui.Frames.GameFrame;
import blackjack.Client.Gui.MainMenu.MainMenu;
import blackjack.Client.Gui.Panels.GamePanel;
import blackjack.Client.Gui.Panels.JoinGamePanels.JoinGamePanel;

/**
 * JoinGameButtonListener 
 * Need to setJoinGamePanel()
 */
public class JoinGameButton implements ActionListener {

    private GameFrame window;

    public JoinGameButton(GameFrame mainFrame) {
        super();
        this.window = mainFrame;
    }

	@Override
	public void actionPerformed(ActionEvent arg0) {
        this.window.replaceCurrentPanel(new JoinGamePanel(window));
	}

    
}
