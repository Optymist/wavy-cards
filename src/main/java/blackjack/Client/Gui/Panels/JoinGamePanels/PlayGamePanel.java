package blackjack.Client.Gui.Panels.JoinGamePanels;

import blackjack.Client.ClientThread;
import blackjack.Client.Gui.Frames.GameFrame;
import blackjack.Client.Gui.Panels.GamePanel;
import blackjack.Client.Gui.Panels.MenuPanel;

/**
 * PlayGamePanel
 */
public class PlayGamePanel extends GamePanel {

    private GameFrame window;
    private ClientThread clientThread;
    private CreateNamePanel createNamePanel;


    public PlayGamePanel(GameFrame mainFrame, ClientThread clientThread) {
        super(mainFrame);
        this.window = mainFrame;
        this.clientThread = clientThread;
    }


	@Override
	public void init() {

	}

    @Override
    public void dispose() {

    }

    public void removeCreateNamePanel() {
        this.remove(createNamePanel);
        this.startGameThread();
    }


	@Override
	public void drawScreen() {

	}
    
}
