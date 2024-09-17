package blackjack.Client.Gui.Frames;

import javax.swing.JFrame;

import blackjack.Client.Gui.Panels.GamePanel;

/**
 * GameFrame
 */
public class GameFrame extends JFrame {

    private volatile GamePanel currentPanel;

    public GameFrame() {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("WavyCards");
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
        currentPanel.gameThread.interrupt();
        this.remove(currentPanel);
        this.pack();
    }

    public GamePanel currentPanel() {
        return currentPanel();
    }

    
}
