package blackjack.Client.Gui.Panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JPanel;

import blackjack.Client.Gui.Entities.GameEntity;
import blackjack.Client.Gui.Frames.GameFrame;

/**
 * MenuPanel
 */
public abstract class MenuPanel extends JPanel {

    protected final GameFrame window; 
    private ArrayList<GameEntity> entities = new ArrayList<>();
    protected Dimension resolution = new Dimension(1600, 900);

    public MenuPanel(GameFrame window, Dimension res, Color color) {
        super();
        this.window = window;
        this.resolution = res;
        this.setBackgroundColor(color);
        this.setPreferredSize(this.resolution);
    }
    
    public MenuPanel(GameFrame window, Color color) {
        this(window, new Dimension(1600, 900), color);
    }

    public MenuPanel(GameFrame window) {
        this(window, Color.GRAY);
    }


    /**
     * Initialize the panel by adding components
     */
    public abstract void init();

    /**
     * Destroy the panel by removing components
     */
    public abstract void dispose();

    public void setBackgroundColor(final Color color) {
        this.setBackground(color);
    }

    public void setBackgroundImage(final Image image) {
        // final GameBackgroundPanel backgroundPanel = new GameBackgroundPanel(image);
        // window.add(backgroundPanel);
        window.pack();
    }

    public void addEntity(GameEntity entity) {
        this.entities.add(entity);
    }

    public ArrayList<GameEntity> entities() {
        ArrayList<GameEntity> entitiesCopy = new ArrayList<>();
        Collections.copy(entitiesCopy, entities);
        return entitiesCopy;
    }

}
