package blackjack.Client.Gui.MainMenu.Buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import blackjack.Client.Gui.Frames.GameFrame;

/**
 * ExitButtonListener
 */
public class ExitButtonListener implements ActionListener {

    private GameFrame window;

    public ExitButtonListener(GameFrame mainFrame) {
        super();
        this.window = mainFrame;
    }

	@Override
	public void actionPerformed(ActionEvent e) {
        System.exit(0);
	}

    
}
