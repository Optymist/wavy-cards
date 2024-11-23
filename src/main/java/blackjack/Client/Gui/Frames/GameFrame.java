package blackjack.Client.Gui.Frames;

import java.net.Socket;

import javax.swing.JFrame;

import blackjack.Client.Gui.Panels.GamePanel;
import blackjack.Client.Gui.Panels.MenuPanel;

/**
 * GameFrame
 */
public class GameFrame extends JFrame {

    private volatile MenuPanel currentPanel;

    public GameFrame() {
        this("WavyCards");
    }

    public GameFrame(String title) {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle(title);
    }

    public void setPanel(MenuPanel newPanel) {
        if (newPanel != null) {
            this.currentPanel = newPanel;
            this.add(currentPanel);
            this.pack();
            this.currentPanel.init();
            this.pack();
        }
    }

    public void removePanel() {
        this.currentPanel.dispose();
        this.remove(currentPanel);
        this.pack();
    }

    public void replaceCurrentPanel(MenuPanel newPanel) {
        removePanel();
        setPanel(newPanel);
    }

    public MenuPanel currentPanel() {
        return currentPanel;
    }

    
}
