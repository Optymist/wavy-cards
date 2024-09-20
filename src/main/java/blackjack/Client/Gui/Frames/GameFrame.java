package blackjack.Client.Gui.Frames;

import javax.swing.JFrame;

import blackjack.Client.Gui.Panels.GamePanel;

/**
 * GameFrame
 */
public class GameFrame extends JFrame {

    private volatile GamePanel currentPanel;

    public GameFrame() {
        this("WavyCards");
    }

    public GameFrame(String title) {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle(title);
    }

    public void setPanel(GamePanel newPanel) {
        if (newPanel != null) {
            this.currentPanel = newPanel;
            this.add(currentPanel);
            this.pack();
            this.currentPanel.startGameThread();
        }
    }

    public void removePanel() {
        this.currentPanel.getGameThread().interrupt();
        this.remove(currentPanel);
        this.pack();
    }

    public GamePanel currentPanel() {
        return currentPanel();
    }

    
}
