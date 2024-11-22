package blackjack.Client.Gui.Panels;

import blackjack.Client.ClientThread;
import blackjack.Client.Gui.Frames.GameFrame;


/**
 * PlayGamePanel
 */
public class LobbyPanel extends MenuPanel {

    private GameFrame window;
    private Thread ClientThread;


    public LobbyPanel(GameFrame mainFrame, ClientThread clientThread) {
        super(mainFrame);
        this.window = mainFrame;
        this.ClientThread = (Thread) clientThread;
    }


	@Override
	public void init() {
	}


	@Override
	public void dispose() {
	}

    
}
