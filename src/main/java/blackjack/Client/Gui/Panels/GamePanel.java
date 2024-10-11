package blackjack.Client.Gui.Panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Button;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import blackjack.Client.Gui.Entities.GameEntity;
import blackjack.Client.Gui.Frames.GameFrame;

/**
 * GamePanel
 */
public abstract class GamePanel extends JPanel implements Runnable {

    protected Dimension resolution = new Dimension(1600, 900);
    protected Thread gameThread;
    public final static int fps = 60;
    private final int lambda = 1000000000;
    protected GameFrame window;
    private ArrayList<GameEntity> entities = new ArrayList<>();

    public GamePanel() {
        super();
    }

    public GamePanel(final GameFrame window, final Color colour) {
        if (window != null) {
            this.window = window;
        }
        this.setBackgroundColor(colour);
        this.setPreferredSize(resolution);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
    }

    public GamePanel(final GameFrame gameFrame) {
        if (window != null) {
            this.window = gameFrame;
        }
        this.setPreferredSize(resolution);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
    }
    
    /**
     * Called just before Game Panel's Loop, use to setup panel.
     */
    public abstract void packPanel();

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void setBackgroundColor(final Color color) {
        this.setBackground(color);
    }

    public void setBackgroundImage(final Image image) {
        // final GameBackgroundPanel backgroundPanel = new GameBackgroundPanel(image);
        // window.add(backgroundPanel);
        window.pack();
    }

    /**
     * The run method runs the frame updating of the main menu.
     */
    @Override
    public void run() {
        // Game loop

        double drawInterval = lambda / fps;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime = 0;
        long timer = 0;
        int drawCount = 0;

        packPanel();

        while (gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                // update screen

                repaint();
                delta--;
                drawCount++;
            }

            // Fps Counter in terminal
            /*
             * if (timer >= 1000000000) {
             * System.out.print(String.format("\033[2J"));
             * System.out.println("FPS: " + drawCount);
             * drawCount = 0;
             * timer = 0;
             * }
             */

        }

    }

    public static Button createButton(String name, int[] cords, ActionListener eventListener) {
        Button button = new Button(name);
        button.setBounds(cords[0], cords[1], cords[2], cords[3]);
        button.addActionListener(eventListener);
        return button;
    }

    public void addEntity(GameEntity t) {
        this.entities.add(t);
    }

    public ArrayList<GameEntity> entities() {
        return new ArrayList<GameEntity>(entities);
    }

    public Thread getGameThread() {
        return this.gameThread;
    }

}